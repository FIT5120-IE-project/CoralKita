@echo off
echo 启动前端开发服务器...
echo 前端将在 http://localhost:3001 运行
echo 后端接口代理到 http://localhost:8080
echo.
echo 注意：如需修改服务器地址，请编辑 src/config/config.js 文件
echo.
npx webpack serve --config webpack.config.js
pause
