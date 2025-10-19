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
    publicPath: '/', // Can be modified as needed for dev/prod
    clean: false,    // Can also be handled by dev/prod config
  },

  resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      '@': resolve('src'),
      'vue$': 'vue/dist/vue.esm.js', // Use when template compiler is needed
    },
  },

  module: {
    rules: [
      // Handle .vue single file components
      {
        test: /\.vue$/,
        loader: 'vue-loader',
      },

      // Handle ES6+ JS
      {
        test: /\.js$/,
        include: [resolve('src')],
        use: {
          loader: 'babel-loader',
        },
      },

      // Handle regular CSS
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

      // Handle images/fonts and other assets (webpack5 built-in asset modules)
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
