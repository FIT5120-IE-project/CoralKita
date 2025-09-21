FROM python:3.11-slim

WORKDIR /app

# 设置pip使用国内镜像源
RUN pip config set global.index-url https://pypi.tuna.tsinghua.edu.cn/simple

# 分步安装依赖（避免超时）
RUN pip install flask flask-cors python-dotenv
RUN pip install openai  # DeepSeek API使用OpenAI兼容接口
RUN pip install sentence-transformers
RUN pip install chromadb
RUN pip install pypdf tqdm  # 添加PDF处理和进度条依赖

# 复制文件
COPY . .

# 设置Hugging Face镜像源
ENV HF_ENDPOINT=https://hf-mirror.com
ENV HF_HUB_ENABLE_HF_TRANSFER=0

# 预下载模型（可选，减少启动时间）
RUN python prefetch_models.py || echo "模型预下载失败，将在运行时下载"

# 暴露端口
EXPOSE 5001

# 启动命令
CMD ["python", "rag_http_server.py"]
