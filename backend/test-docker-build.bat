@echo off
chcp 65001 >nul

REM CoralKita 后端 Docker 构建测试脚本 (Windows版本)

echo 🐠 CoralKita 后端 Docker 构建测试
echo ==================================

REM 检查Docker是否安装
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker 未安装，请先安装 Docker Desktop
    pause
    exit /b 1
)

echo ✅ Docker 已安装

REM 检查Docker是否运行
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker 未运行，请启动 Docker Desktop
    pause
    exit /b 1
)

echo ✅ Docker 服务正在运行

REM 检查必要文件是否存在
echo 📁 检查项目文件...

if not exist "Dockerfile" (
    echo ❌ 缺少必要文件: Dockerfile
    pause
    exit /b 1
)

if not exist "pom.xml" (
    echo ❌ 缺少必要文件: pom.xml
    pause
    exit /b 1
)

if not exist "common\pom.xml" (
    echo ❌ 缺少必要文件: common\pom.xml
    pause
    exit /b 1
)

if not exist "pojo\pom.xml" (
    echo ❌ 缺少必要文件: pojo\pom.xml
    pause
    exit /b 1
)

if not exist "server\pom.xml" (
    echo ❌ 缺少必要文件: server\pom.xml
    pause
    exit /b 1
)

if not exist "common\src" (
    echo ❌ 缺少必要文件: common\src
    pause
    exit /b 1
)

if not exist "pojo\src" (
    echo ❌ 缺少必要文件: pojo\src
    pause
    exit /b 1
)

if not exist "server\src" (
    echo ❌ 缺少必要文件: server\src
    pause
    exit /b 1
)

echo ✅ 所有必要文件都存在

REM 清理旧的镜像和容器
echo 🧹 清理旧的镜像和容器...
docker stop coralkita-backend-test >nul 2>&1
docker rm coralkita-backend-test >nul 2>&1
docker rmi coralkita-backend:test >nul 2>&1

REM 构建Docker镜像
echo 🔨 开始构建 Docker 镜像...
docker build -t coralkita-backend:test .
if %errorlevel% neq 0 (
    echo ❌ Docker 镜像构建失败
    pause
    exit /b 1
)

echo ✅ Docker 镜像构建成功

REM 检查镜像大小
echo 📊 镜像信息:
docker images coralkita-backend:test

REM 测试运行容器
echo 🚀 测试运行容器...
docker run -d --name coralkita-backend-test -p 8081:8080 coralkita-backend:test
if %errorlevel% neq 0 (
    echo ❌ 容器启动失败
    pause
    exit /b 1
)

echo ✅ 容器启动成功

REM 等待应用启动
echo ⏳ 等待应用启动...
timeout /t 10 /nobreak >nul

REM 检查容器状态
docker ps | findstr coralkita-backend-test >nul
if %errorlevel% neq 0 (
    echo ❌ 容器启动失败
    echo 📋 容器日志:
    docker logs coralkita-backend-test
    pause
    exit /b 1
)

echo ✅ 容器正在运行

REM 显示容器日志
echo 📋 容器日志 (最后20行):
docker logs --tail 20 coralkita-backend-test

REM 清理测试容器
echo 🧹 清理测试容器...
docker stop coralkita-backend-test
docker rm coralkita-backend-test

echo.
echo 🎉 Docker 构建和运行测试完成！
echo 📝 镜像标签: coralkita-backend:test
echo 🔗 如需部署，请使用: docker run -d --name coralkita-backend -p 8080:8080 coralkita-backend:test

pause
