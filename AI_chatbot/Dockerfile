# Use Python 3.11 as base image
FROM python:3.11-slim

# Set working directory
WORKDIR /app

# Configure Debian to use Aliyun mirror sources
RUN sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list.d/debian.sources && \
    sed -i 's|security.debian.org/debian-security|mirrors.aliyun.com/debian-security|g' /etc/apt/sources.list.d/debian.sources

# Install system dependencies
RUN apt-get update && apt-get install -y \
    build-essential \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copy requirements.txt and install Python dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple

# Copy application code
COPY . .

# Create necessary directories
RUN mkdir -p faiss_db

# Expose Streamlit default port
EXPOSE 8506

# Set environment variables
ENV PYTHONUNBUFFERED=1
ENV STREAMLIT_SERVER_PORT=8506
ENV STREAMLIT_SERVER_ADDRESS=0.0.0.0
ENV STREAMLIT_SERVER_HEADLESS=true
ENV STREAMLIT_BROWSER_GATHER_USAGE_STATS=false
ENV STREAMLIT_SERVER_BASE_URL_PATH=agent

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8506/agent/_stcore/health || exit 1

# Startup command
CMD ["streamlit", "run", "chatbot.py", "--server.port=8506", "--server.address=0.0.0.0", "--server.baseUrlPath=agent"]

