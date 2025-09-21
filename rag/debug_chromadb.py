#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
调试ChromaDB数据存储问题
"""

import os
import sys
import chromadb
from collections import Counter

def debug_chromadb():
    """调试ChromaDB数据"""
    try:
        # 初始化ChromaDB客户端
        chromadb_client = chromadb.PersistentClient(path="chroma_db")
        
        # 获取集合
        try:
            collection = chromadb_client.get_collection(name="coralkita_reports")
            print("✅ 找到集合: coralkita_reports")
        except Exception as e:
            print(f"❌ 集合不存在: {e}")
            return
        
        # 获取所有数据
        print("\n📊 检查ChromaDB数据...")
        
        # 获取文档数量
        count = collection.count()
        print(f"📄 总文档数量: {count}")
        
        if count == 0:
            print("⚠️ 数据库为空，没有存储任何文档")
            return
        
        # 获取所有文档的元数据
        all_data = collection.get(include=['metadatas', 'documents'])
        
        # 分析年份分布
        years = []
        source_files = []
        
        for metadata in all_data['metadatas']:
            if 'year' in metadata:
                years.append(metadata['year'])
            if 'source_file' in metadata:
                source_files.append(metadata['source_file'])
        
        print(f"\n📅 年份分布:")
        year_counts = Counter(years)
        for year, count in sorted(year_counts.items()):
            print(f"  {year}: {count} 个文档")
        
        print(f"\n📁 源文件分布:")
        file_counts = Counter(source_files)
        for file, count in sorted(file_counts.items()):
            print(f"  {file}: {count} 个文档")
        
        # 检查是否有2024年的数据
        if '2024' in years:
            print(f"\n✅ 找到2024年数据: {year_counts['2024']} 个文档")
        else:
            print(f"\n❌ 没有找到2024年数据")
            print(f"📋 可用的年份: {sorted(set(years))}")
        
        # 显示一些示例文档
        print(f"\n📝 示例文档 (前5个):")
        for i, (doc, meta) in enumerate(zip(all_data['documents'][:5], all_data['metadatas'][:5])):
            print(f"\n文档 {i+1}:")
            print(f"  年份: {meta.get('year', 'N/A')}")
            print(f"  源文件: {meta.get('source_file', 'N/A')}")
            print(f"  内容预览: {doc[:200]}...")
        
        # 测试查询2024年相关数据
        print(f"\n🔍 测试查询2024年相关数据...")
        try:
            # 简单的文本查询
            results = collection.query(
                query_texts=["2024年", "2024", "recent", "latest"],
                n_results=10
            )
            
            if results['documents'] and results['documents'][0]:
                print(f"✅ 找到相关文档: {len(results['documents'][0])} 个")
                for i, (doc, meta) in enumerate(zip(results['documents'][0], results['metadatas'][0])):
                    print(f"  结果 {i+1}: {meta.get('year', 'N/A')} - {meta.get('source_file', 'N/A')}")
                    print(f"    内容: {doc[:100]}...")
            else:
                print("❌ 没有找到2024年相关文档")
                
        except Exception as e:
            print(f"⚠️ 查询测试失败: {e}")
        
        # 检查文件名中的年份
        print(f"\n🔍 检查文件名中的年份...")
        for file in source_files:
            if '2024' in file:
                print(f"✅ 找到包含2024的文件: {file}")
            elif '2023' in file:
                print(f"📅 找到2023年文件: {file}")
            elif '2022' in file:
                print(f"📅 找到2022年文件: {file}")
        
    except Exception as e:
        print(f"❌ 检查失败: {e}")

if __name__ == "__main__":
    print("=== ChromaDB数据调试 ===")
    debug_chromadb()
    print("\n=== 调试完成 ===")
