#!/usr/bin/env python3
"""
预下载模型脚本 - 匹配实际使用的模型
"""
import os
from sentence_transformers import SentenceTransformer
from sentence_transformers import CrossEncoder

# 设置Hugging Face镜像源
os.environ['HF_ENDPOINT'] = 'https://hf-mirror.com'

# 实际使用的模型列表
embedding_models = [
    "shibing624/text2vec-base-chinese",  # 主要中文模型
    "paraphrase-MiniLM-L6-v2"  # 备选模型
]

cross_encoder_models = [
    "cross-encoder/mmarco-mMiniLMv2-L12-H384-v1",  # 主要重排序模型
    "cross-encoder/ms-marco-MiniLM-L-2-v2"  # 备选模型
]

print("=== 预下载Embedding模型 ===")
for model_name in embedding_models:
    print(f"Prefetch: {model_name}")
    try:
        model = SentenceTransformer(model_name)
        print(f"✅ OK: {model_name}")
    except Exception as e:
        print(f"❌ Error loading {model_name}: {e}")

print("\n=== 预下载重排序模型 ===")
for model_name in cross_encoder_models:
    print(f"Prefetch: {model_name}")
    try:
        model = CrossEncoder(model_name)
        print(f"✅ OK: {model_name}")
    except Exception as e:
        print(f"❌ Error loading {model_name}: {e}")

print("\n🎉 所有模型预下载完成！")
