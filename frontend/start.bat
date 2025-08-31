@echo off
echo 启动前端开发服务器...
echo 前端将在 http://localhost:3000 运行
echo 后端接口代理到 http://localhost:8080
echo.
npx webpack serve --config webpack.config.js
pause
