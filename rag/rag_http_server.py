#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
RAG HTTP服务器 - 为Java后端提供HTTP接口服务
"""

import os
import json
import time
from typing import List, Dict, Any
from flask import Flask, request, jsonify
from flask_cors import CORS

# 设置输出编码为UTF-8
import io
import sys
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')

# 添加当前目录到Python路径
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

# 全局变量存储RAG组件
rag_components = None

def load_rag_system():
    """加载RAG系统组件"""
    global rag_components
    try:
        import chromadb
        from sentence_transformers import SentenceTransformer
        from sentence_transformers import CrossEncoder
        from dotenv import load_dotenv
        from openai import OpenAI  # DeepSeek API使用OpenAI兼容接口
        
        # 加载环境变量（优先从.env文件，然后从系统环境变量）
        load_dotenv()
        
        # 检查DEEPSEEK_API_KEY环境变量，如果没有则使用提供的API密钥
        api_key = os.getenv('DEEPSEEK_API_KEY', 'sk-61c06f303045475796eda944341c4a01')
        if not api_key:
            raise ValueError("DEEPSEEK_API_KEY environment variable is not set. Please set it in system environment variables or .env file")
        
        # 初始化ChromaDB
        chromadb_client = chromadb.PersistentClient(path="chroma_db")
        
        # 尝试获取集合，如果不存在则创建
        try:
            chromadb_collection = chromadb_client.get_collection(name="coralkita_reports")
            print("找到现有集合: coralkita_reports")
        except Exception:
            print("集合不存在，正在创建新集合: coralkita_reports")
            chromadb_collection = chromadb_client.create_collection(name="coralkita_reports")
        
        # 设置Hugging Face镜像源
        os.environ['HF_ENDPOINT'] = 'https://hf-mirror.com'
        
        try:
            # 尝试初始化embedding模型
            #embedding_model = SentenceTransformer("sentence-transformers/all-mpnet-base-v2")
            embedding_model = SentenceTransformer("shibing624/text2vec-base-chinese")
            print("Embedding模型加载成功")
        except Exception as e:
            print(f"Embedding模型加载失败: {e}")
            # 使用更简单的模型作为备选
            embedding_model = SentenceTransformer("paraphrase-MiniLM-L6-v2")
        
        try:
            # 尝试初始化重排序模型
            #cross_encoder = CrossEncoder('cross-encoder/ms-marco-MiniLM-L-6-v2')
            cross_encoder = CrossEncoder('cross-encoder/mmarco-mMiniLMv2-L12-H384-v1')
            print("重排序模型加载成功")
        except Exception as e:
            print(f"重排序模型加载失败: {e}")
            # 使用更简单的模型作为备选
            cross_encoder = CrossEncoder('cross-encoder/ms-marco-MiniLM-L-2-v2')
        
        # 初始化DeepSeek客户端（使用OpenAI兼容接口）
        deepseek_client = OpenAI(
            api_key=api_key,
            base_url="https://api.deepseek.com"  # DeepSeek API端点
        )
        
        rag_components = {
            'chromadb_collection': chromadb_collection,
            'embedding_model': embedding_model,
            'cross_encoder': cross_encoder,
            'deepseek_client': deepseek_client  # DeepSeek客户端
        }
        
        print("RAG系统加载成功")
        return rag_components
    except Exception as e:
        print(f"ERROR: Failed to load RAG system: {e}", file=sys.stderr)
        raise e

def embed_chunk(chunk: str, embedding_model) -> List[float]:
    """生成文本嵌入向量"""
    embedding = embedding_model.encode(chunk, normalize_embeddings=True)
    return embedding.tolist()

def retrieve_documents(query: str, top_k: int, chromadb_collection, embedding_model):
    """检索相关文档"""
    # 改进查询策略：为时间相关查询添加额外关键词
    enhanced_query = query
    
    # 如果查询包含年份，添加相关关键词
    if '2024' in query or '2024年' in query:
        enhanced_query = f"{query} 2024 annual survey report latest"
    elif '2023' in query or '2023年' in query:
        enhanced_query = f"{query} 2023 annual survey report"
    elif '2022' in query or '2022年' in query:
        enhanced_query = f"{query} 2022 annual survey report"
    
    print(f"原始查询: {query}")
    print(f"增强查询: {enhanced_query}")
    
    query_embedding = embed_chunk(enhanced_query, embedding_model)
    results = chromadb_collection.query(
        query_embeddings=[query_embedding],
        n_results=top_k * 3  # 检索更多文档用于重排序
    )
    
    # 添加调试信息
    print(f"检索到的文档数量: {len(results['documents'][0])}")
    print("检索到的文档信息:")
    for i, (doc, meta) in enumerate(zip(results['documents'][0][:5], results['metadatas'][0][:5])):
        print(f"  {i+1}. {meta.get('source_file', 'N/A')} ({meta.get('year', 'N/A')}) - 距离: {results['distances'][0][i]:.3f}")
    
    return results['documents'][0], results['metadatas'][0], results['distances'][0]

def rerank_documents(query: str, documents: List[str], metadatas: List[Dict], top_k: int, cross_encoder):
    """重排序文档"""
    pairs = [(query, doc) for doc in documents]
    scores = cross_encoder.predict(pairs)

    # 组合文档、元数据和分数
    scored_items = list(zip(documents, metadatas, scores))
    
    # 改进重排序：优先考虑年份匹配
    def custom_sort_key(item):
        doc, meta, score = item
        base_score = score
        
        # 如果查询包含年份，给匹配年份的文档加分
        if '2024' in query or '2024年' in query:
            if meta.get('year') == '2024' or '2024' in meta.get('source_file', ''):
                base_score += 2.0  # 增加年份匹配加分
                print(f"  ✅ 2024年文档加分: {meta.get('source_file', 'N/A')} (原分数: {score:.3f} -> 新分数: {base_score:.3f})")
        elif '2023' in query or '2023年' in query:
            if meta.get('year') == '2023' or '2023' in meta.get('source_file', ''):
                base_score += 2.0
        elif '2022' in query or '2022年' in query:
            if meta.get('year') == '2022' or '2022' in meta.get('source_file', ''):
                base_score += 2.0
        
        return base_score
    
    #scored_items.sort(key=lambda x: x[2], reverse=True)
    scored_items.sort(key=custom_sort_key, reverse=True)

    # 分离排序后的结果
    reranked_docs = [item[0] for item in scored_items[:top_k]]
    reranked_metas = [item[1] for item in scored_items[:top_k]]
    reranked_scores = [item[2] for item in scored_items[:top_k]]
    
    # 打印重排序结果用于调试
    print(f"重排序后的前{min(3, len(reranked_metas))}个文档:")
    for i, meta in enumerate(reranked_metas[:3]):
        print(f"  {i+1}. {meta.get('source_file', 'N/A')} ({meta.get('year', 'N/A')}) - 分数: {reranked_scores[i]:.3f}")
    
    # 检查是否有2024年文档
    has_2024 = any('2024' in meta.get('source_file', '') or meta.get('year') == '2024' for meta in reranked_metas)
    print(f"重排序后是否包含2024年文档: {'是' if has_2024 else '否'}")
    
    return reranked_docs, reranked_metas, reranked_scores

def is_coral_related(query: str, deepseek_client) -> bool:  # 修改参数名：google_client -> deepseek_client
    """判断问题是否与马来西亚珊瑚相关"""
    prompt = f"""Please determine if the following question is related to Malaysian coral reefs. Please only answer "yes" or "no".

Question: {query}

Criteria:
- If the question involves Malaysian coral reefs, coral health, marine conservation, coral monitoring, etc., answer "yes"
- If the question is just a simple greeting, thanks, farewell, etc., answer "no"
- If the question is unrelated to coral (such as weather, other organisms, technical issues, etc.), answer "no"

Please only answer "yes" or "no":"""

    try:
        # 使用DeepSeek API
        response = deepseek_client.chat.completions.create(
            model="deepseek-chat",  # DeepSeek模型
            messages=[{"role": "user", "content": prompt}],
            max_tokens=10,  # 只需要yes/no回答
            temperature=0.1  # 低温度确保一致性
        )
        result = response.choices[0].message.content.strip().lower()
        return "yes" in result
    except Exception as e:
        # If judgment fails, default to RAG retrieval
        return True

def generate_direct_answer(query: str, deepseek_client) -> str:  # 修改参数名：google_client -> deepseek_client
    """对于非珊瑚相关问题，直接生成回答"""
    prompt = f"""You are a friendly AI assistant. Please answer the user's question concisely.

User question: {query}

Please provide a concise and friendly answer. If the question is unrelated to coral reefs, you can politely explain that you are CoralKita's coral knowledge assistant and mainly answer questions about coral reefs."""

    try:
        # 使用DeepSeek API
        response = deepseek_client.chat.completions.create(
            model="deepseek-chat",  # DeepSeek模型
            messages=[{"role": "user", "content": prompt}],
            max_tokens=500,  # 适中的回答长度
            temperature=0.7  # 适中的创造性
        )
        return response.choices[0].message.content
    except Exception as e:
        return "Sorry, I cannot answer this question. I am CoralKita's coral knowledge assistant and mainly answer questions about Malaysian coral reefs."

def generate_answer(query: str, documents: List[str], metadatas: List[Dict], deepseek_client):  # 修改参数名：google_client -> deepseek_client
    """生成AI答案"""
    # 构建带来源信息的prompt
    sources_info = []
    for i, (doc, meta) in enumerate(zip(documents, metadatas)):
        source_desc = f"Fragment {i+1} (Source: {meta['source_file']}, Year: {meta['year']})"
        sources_info.append(f"{source_desc}:\n{doc}")
    
    # 构建来源信息字符串
    sources_text = "\n\n".join(sources_info)
    
    prompt = f"""You are a coral knowledge assistant. Please generate an accurate answer based on the user's question and the following fragments.

User question: {query}

Relevant fragments:
{sources_text}

Please answer based on the above content and do not fabricate information. At the end of your answer, please list the main source files you referenced."""

    try:
        # 使用DeepSeek API
        response = deepseek_client.chat.completions.create(
            model="deepseek-chat",  # DeepSeek模型
            messages=[{"role": "user", "content": prompt}],
            max_tokens=1000,  # 较长的回答长度，因为包含来源信息
            temperature=0.3  # 较低温度确保准确性
        )
        return response.choices[0].message.content
    except Exception as e:
        return f"Error generating answer: {e}"

# 创建Flask应用
app = Flask(__name__)
CORS(app)  # 允许跨域请求

@app.route('/health', methods=['GET'])
def health_check():
    """健康检查接口"""
    return jsonify({
        'status': 'healthy',
        'message': 'RAG HTTP服务运行正常',
        'timestamp': time.time()
    })

@app.route('/rag/query', methods=['POST'])
def rag_query():
    """RAG查询接口"""
    try:
        # 检查RAG系统是否已加载
        if rag_components is None:
            return jsonify({
                'success': False,
                'error': 'RAG系统未初始化'
            }), 500
        
        # 获取请求参数
        data = request.get_json()
        if not data:
            return jsonify({
                'success': False,
                'error': '请求体不能为空'
            }), 400
        
        question = data.get('question', '').strip()
        if not question:
            return jsonify({
                'success': False,
                'error': '问题不能为空'
            }), 400
        
        top_k = data.get('top_k', 5)
        include_sources = data.get('include_sources', True)
        
        # 获取RAG组件
        deepseek_client = rag_components['deepseek_client']
        
        # 先判断问题是否与珊瑚相关
        if not is_coral_related(question, deepseek_client):
            # 不相关，直接生成回答
            answer = generate_direct_answer(question, deepseek_client)
            response_data = {
                'success': True,
                'answer': answer,
                'sources': [] if include_sources else None,
                'is_coral_related': False
            }
            return jsonify(response_data)
        
        # 相关，进行RAG检索
        # 检索文档
        print(f"开始检索文档，查询: {question}")
        documents, metadatas, distances = retrieve_documents(
            question, top_k, 
            rag_components['chromadb_collection'], 
            rag_components['embedding_model']
        )
        print(f"检索到 {len(documents)} 个文档")
        
        # 打印检索到的文档信息
        for i, (doc, meta) in enumerate(zip(documents[:3], metadatas[:3])):
            print(f"  检索结果 {i+1}: {meta.get('source_file', 'N/A')} ({meta.get('year', 'N/A')})")
        
        # 重排序
        reranked_docs, reranked_metas, reranked_scores = rerank_documents(
            question, documents, metadatas, top_k, 
            rag_components['cross_encoder']
        )
        
        # 生成答案
        answer = generate_answer(
            question, reranked_docs, reranked_metas, 
            deepseek_client
        )
        
        # 构建响应数据
        response_data = {
            'success': True,
            'answer': answer,
            'is_coral_related': True
        }
        
        if include_sources:
            sources = []
            for i, (meta, score) in enumerate(zip(reranked_metas, reranked_scores)):
                content = reranked_docs[i][:200] + "..." if len(reranked_docs[i]) > 200 else reranked_docs[i]
                sources.append({
                    'source_file': meta['source_file'],
                    'year': meta['year'],
                    'score': round(score, 3),
                    'content': content
                })
            response_data['sources'] = sources
        
        return jsonify(response_data)
        
    except Exception as e:
        print(f"RAG查询错误: {e}", file=sys.stderr)
        return jsonify({
            'success': False,
            'error': f'RAG查询失败: {str(e)}'
        }), 500

@app.route('/rag/init', methods=['POST'])
def init_rag():
    """初始化RAG系统接口"""
    try:
        global rag_components
        rag_components = load_rag_system()
        return jsonify({
            'success': True,
            'message': 'RAG系统初始化成功'
        })
    except Exception as e:
        return jsonify({
            'success': False,
            'error': f'RAG系统初始化失败: {str(e)}'
        }), 500

if __name__ == "__main__":
    # 启动时自动加载RAG系统
    try:
        print("正在初始化RAG系统...")
        load_rag_system()
        print("RAG系统初始化完成")
    except Exception as e:
        print(f"RAG系统初始化失败: {e}")
        print("服务仍将启动，但需要手动调用 /rag/init 接口初始化")
    
    # 启动Flask服务
    port = int(os.getenv('RAG_PORT', 5001))
    host = os.getenv('RAG_HOST', '0.0.0.0')
    
    print(f"RAG HTTP服务启动在 {host}:{port}")
    app.run(host=host, port=port, debug=False)
