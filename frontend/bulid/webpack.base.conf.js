// build/webpack.base.conf.js
const path = require('path');
const { VueLoaderPlugin } = require('vue-loader');

function resolve(dir) {
  return path.join(__dirname, '..', dir);
}

module.exports = {
  entry: {
    app: './src/main.js',
  },

  output: {
    path: path.resolve(__dirname, '../dist'),
    filename: '[name].js',
    publicPath: '/', // 开发/生产都可按需修改
    clean: false,    // 交给 dev/prod 配置处理也可以
  },

  resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      '@': resolve('src'),
      'vue$': 'vue/dist/vue.esm.js', // 需要模板编译器时使用
    },
  },

  module: {
    rules: [
      // 处理 .vue 单文件组件
      {
        test: /\.vue$/,
        loader: 'vue-loader',
      },

      // 处理 ES6+ JS
      {
        test: /\.js$/,
        include: [resolve('src')],
        use: {
          loader: 'babel-loader',
        },
      },

      // 处理普通 CSS
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader'],
      },

      // ✅ 处理 Less
      {
        test: /\.less$/,
        use: [
          'style-loader',
          { loader: 'css-loader', options: { importLoaders: 1 } },
          'less-loader',
        ],
      },

      // 处理图片/字体等资源（webpack5 内置 asset modules）
      {
        test: /\.(png|jpe?g|gif|svg|webp|ico)(\?.*)?$/i,
        type: 'asset/resource',
        generator: {
          filename: 'img/[name][hash][ext][query]',
        },
      },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/i,
        type: 'asset/resource',
        generator: {
          filename: 'fonts/[name][hash][ext][query]',
        },
      },
    ],
  },

  plugins: [
    // vue-loader v15 必须的插件
    new VueLoaderPlugin(),
  ],
};
