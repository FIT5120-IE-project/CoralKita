# RAG智能问答API文档

## 概述

CoralKita项目集成了基于RAG（Retrieval-Augmented Generation）的智能问答系统，可以为用户提供关于马来西亚珊瑚礁的专业知识问答服务。

## 系统架构

```
前端 → Java后端 → Python RAG脚本 → ChromaDB向量数据库
```

## API接口

### 1. RAG智能问答

**接口地址**: `POST /rag/query`

**请求参数**:
```json
{
  "question": "马来西亚珊瑚礁2020年的健康状况如何？",
  "userId": 123,
  "topK": 5,
  "includeSources": true
}
```

**参数说明**:
- `question` (必填): 用户问题
- `userId` (可选): 用户ID，用于记录查询历史
- `topK` (可选): 返回结果数量，默认5
- `includeSources` (可选): 是否包含来源信息，默认true

**响应示例**:
```json
{
  "code": 1,
  "msg": null,
  "data": {
    "question": "马来西亚珊瑚礁2020年的健康状况如何？",
    "answer": "根据2020年马来西亚珊瑚礁年度调查报告...",
    "sources": [
      {
        "sourceFile": "2020AnnualSurveyReport.pdf",
        "year": "2020",
        "score": 0.856,
        "content": "2020年马来西亚珊瑚礁调查结果显示..."
      }
    ],
    "processingTime": 2341
  }
}
```

### 2. 快速问答

**接口地址**: `GET /rag/quick-query`

**请求参数**:
- `question` (必填): 用户问题

**响应示例**:
```json
{
  "code": 1,
  "msg": null,
  "data": "根据2020年马来西亚珊瑚礁年度调查报告..."
}
```

### 3. 系统状态检查

**接口地址**: `GET /rag/status`

**响应示例**:
```json
{
  "code": 1,
  "msg": null,
  "data": true
}
```

## 部署配置

### 1. 环境要求

- Java 17+
- Python 3.8+
- 已安装的Python依赖包（见rag/pyproject.toml）

### 2. 配置文件

在 `application.yml` 中配置：

```yaml
# RAG系统配置
rag:
  script:
    path: ../rag/rag_api.py
python:
  executable: python  # 或 python3，根据系统环境调整
```

### 3. 目录结构

```
backend/
├── server/
│   ├── src/main/java/com/sky/
│   │   ├── controller/RagController.java
│   │   ├── service/RagService.java
│   │   └── service/impl/RagServiceImpl.java
│   └── src/main/resources/application.yml
└── pojo/
    └── src/main/java/com/sky/
        ├── dto/RagQueryDTO.java
        └── vo/RagAnswerVO.java

rag/
├── rag_api.py
├── chroma_db/          # ChromaDB数据库目录
├── pyproject.toml
└── Annual Survey Report/
```

## 使用示例

### 前端调用示例

```javascript
// 完整问答
const response = await fetch('/rag/query', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    question: '马来西亚珊瑚礁2020年的健康状况如何？',
    userId: 123,
    topK: 5,
    includeSources: true
  })
});

const result = await response.json();
console.log(result.data.answer);

// 快速问答
const quickResponse = await fetch('/rag/quick-query?question=珊瑚白化的原因是什么？');
const quickResult = await quickResponse.json();
console.log(quickResult.data);
```

### Python脚本直接调用

```bash
cd rag
python rag_api.py "马来西亚珊瑚礁2020年的健康状况如何？" 5 true
```

## 故障排除

### 1. Python环境问题

```bash
# 检查Python版本
python --version

# 安装依赖
pip install pypdf chromadb google-genai python-dotenv sentence-transformers
```

### 2. ChromaDB数据库问题

确保 `rag/chroma_db` 目录存在且包含数据：
```bash
ls -la rag/chroma_db/
```

### 3. 权限问题

确保Java进程有权限执行Python脚本：
```bash
chmod +x rag/rag_api.py
```

### 4. 超时问题

如果问答超时，可以调整 `PythonScriptService` 中的超时时间：
```java
boolean finished = process.waitFor(60, TimeUnit.SECONDS); // 增加到60秒
```

## 性能优化

1. **缓存机制**: 可以考虑对常见问题进行缓存
2. **异步处理**: 对于复杂问题可以使用异步处理
3. **负载均衡**: 多个Python进程实例
4. **数据库优化**: ChromaDB索引优化

## 监控和日志

系统会记录以下日志：
- RAG查询请求和响应
- Python脚本执行状态
- 错误和异常信息
- 性能指标（处理时间等）

查看日志：
```bash
tail -f logs/application.log | grep RAG
```
