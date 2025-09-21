#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试RAG查询过程，调试为什么没有找到2024年数据
"""

import os
import sys
import requests
import json

def test_rag_query_debug():
    """测试RAG查询并调试"""
    
    base_url = "http://localhost:5001"
    
    print("=== RAG查询调试 ===")
    
    # 测试问题
    test_questions = [
        "马来西亚2024年珊瑚礁情况如何？",
        "2024年珊瑚礁健康状况",
        "最新的珊瑚礁调查报告",
        "recent coral reef survey",
        "2024 coral reef status"
    ]
    
    # 首先检查服务是否运行
    try:
        health_response = requests.get(f"{base_url}/health", timeout=5)
        if health_response.status_code == 200:
            print("✅ RAG服务运行正常")
        else:
            print(f"❌ 服务健康检查失败: {health_response.status_code}")
            return
    except Exception as e:
        print(f"❌ 无法连接到RAG服务: {e}")
        print("请确保RAG服务正在运行")
        return
    
    # 测试每个问题
    for i, question in enumerate(test_questions, 1):
        print(f"\n📝 测试问题 {i}: {question}")
        
        try:
            # 发送RAG查询
            response = requests.post(
                f"{base_url}/rag/query",
                json={
                    "question": question,
                    "top_k": 10,  # 增加检索数量
                    "include_sources": True
                },
                timeout=30
            )
            
            if response.status_code == 200:
                data = response.json()
                
                if data.get('success'):
                    print(f"✅ 查询成功")
                    print(f"📄 是否与珊瑚相关: {data.get('is_coral_related', 'N/A')}")
                    
                    # 显示来源信息
                    sources = data.get('sources', [])
                    if sources:
                        print(f"📚 找到 {len(sources)} 个相关文档:")
                        for j, source in enumerate(sources, 1):
                            print(f"  {j}. {source.get('source_file', 'N/A')} ({source.get('year', 'N/A')}) - 分数: {source.get('score', 'N/A')}")
                            print(f"     内容: {source.get('content', '')[:100]}...")
                    else:
                        print("⚠️ 没有找到相关文档")
                    
                    # 显示答案预览
                    answer = data.get('answer', '')
                    if answer:
                        print(f"💬 答案预览: {answer[:300]}...")
                    else:
                        print("⚠️ 没有生成答案")
                        
                else:
                    print(f"❌ 查询失败: {data.get('error', '未知错误')}")
            else:
                print(f"❌ HTTP错误: {response.status_code}")
                print(f"响应: {response.text}")
                
        except Exception as e:
            print(f"❌ 请求失败: {e}")

if __name__ == "__main__":
    test_rag_query_debug()
