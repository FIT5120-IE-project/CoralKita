const path = require('path');
const { VueLoaderPlugin } = require('vue-loader');
const HtmlWebpackPlugin = require('html-webpack-plugin');

// 后端服务器配置
const BACKEND_URL = 'http://localhost:8080';
const FRONTEND_PORT = 3001;

module.exports = (env, argv) => {
  const isProduction = argv.mode === 'production';
  
  return {
    mode: isProduction ? 'production' : 'development', // 根据构建模式设置
  entry: './src/main.js', // 入口文件
  output: {
    path: path.resolve(__dirname, 'dist'), // 打包目录
    filename: 'bundle.js'
  },
  devServer: {
    static: {
      directory: path.join(__dirname, 'public') // 静态资源
    },
    port: FRONTEND_PORT,  // 使用配置的端口
    hot: true,   // 热更新
    open: true,  // 自动打开浏览器
    proxy: {     // 代理配置，解决跨域问题
      '/api': {
        target: BACKEND_URL,  // 使用配置的后端地址
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      },
      '/quiz': {
        target: BACKEND_URL,  // 代理测验相关的API
        changeOrigin: true
      },
      '/checklist': {
        target: BACKEND_URL,  // 代理清单相关的API
        changeOrigin: true
      },
      '/oss': {
        target: BACKEND_URL,  // 代理OSS相关的API
        changeOrigin: true
      }
    }
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: 'vue-loader'
      },
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader'
        }
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader']
      },
      {
        test: /\.less$/,
        use: ['style-loader', 'css-loader', 'less-loader']
      },
      {
        test: /\.(png|jpg|gif|svg)$/,
        type: 'asset/resource'
      },
      {
        test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)$/,
        type: 'asset/resource',
        generator: {
          filename: 'media/[name][hash][ext][query]'
        }
      }
    ]
  },
  plugins: [
    new VueLoaderPlugin(),
    new HtmlWebpackPlugin({
      template: './index.html', // 自动注入打包后的 js
      filename: 'index.html'
    })
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src') // @ 代表 src 目录
    },
    extensions: ['.js', '.vue', '.json']
  }
  };
};