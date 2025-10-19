#!/bin/bash

# CoralKita 后端 Docker 构建测试脚本

echo "🐠 CoralKita 后端 Docker 构建测试"
echo "=================================="

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo "❌ Docker 未安装，请先安装 Docker"
    exit 1
fi

echo "✅ Docker 已安装"

# 检查Docker是否运行
if ! docker info &> /dev/null; then
    echo "❌ Docker 未运行，请启动 Docker 服务"
    exit 1
fi

echo "✅ Docker 服务正在运行"

# 检查必要文件是否存在
echo "📁 检查项目文件..."

required_files=(
    "Dockerfile"
    "pom.xml"
    "common/pom.xml"
    "pojo/pom.xml"
    "server/pom.xml"
    "common/src"
    "pojo/src"
    "server/src"
)

for file in "${required_files[@]}"; do
    if [ ! -e "$file" ]; then
        echo "❌ 缺少必要文件: $file"
        exit 1
    fi
done

echo "✅ 所有必要文件都存在"

# 清理旧的镜像和容器
echo "🧹 清理旧的镜像和容器..."
docker stop coralkita-backend-test 2>/dev/null || true
docker rm coralkita-backend-test 2>/dev/null || true
docker rmi coralkita-backend:test 2>/dev/null || true

# 构建Docker镜像
echo "🔨 开始构建 Docker 镜像..."
if docker build -t coralkita-backend:test .; then
    echo "✅ Docker 镜像构建成功"
else
    echo "❌ Docker 镜像构建失败"
    exit 1
fi

# 检查镜像大小
echo "📊 镜像信息:"
docker images coralkita-backend:test

# 测试运行容器
echo "🚀 测试运行容器..."
if docker run -d --name coralkita-backend-test -p 8081:8080 coralkita-backend:test; then
    echo "✅ 容器启动成功"
    
    # 等待应用启动
    echo "⏳ 等待应用启动..."
    sleep 10
    
    # 检查容器状态
    if docker ps | grep -q coralkita-backend-test; then
        echo "✅ 容器正在运行"
        
        # 检查应用健康状态
        echo "🏥 检查应用健康状态..."
        if curl -f http://localhost:8081/actuator/health 2>/dev/null; then
            echo "✅ 应用健康检查通过"
        else
            echo "⚠️  应用健康检查失败，但容器仍在运行"
        fi
        
        # 显示容器日志
        echo "📋 容器日志 (最后20行):"
        docker logs --tail 20 coralkita-backend-test
        
    else
        echo "❌ 容器启动失败"
        echo "📋 容器日志:"
        docker logs coralkita-backend-test
        exit 1
    fi
else
    echo "❌ 容器启动失败"
    exit 1
fi

# 清理测试容器
echo "🧹 清理测试容器..."
docker stop coralkita-backend-test
docker rm coralkita-backend-test

echo ""
echo "🎉 Docker 构建和运行测试完成！"
echo "📝 镜像标签: coralkita-backend:test"
echo "🔗 如需部署，请使用: docker run -d --name coralkita-backend -p 8080:8080 coralkita-backend:test"
