import Vue from 'vue'   //引入vue模块
import App from './App.vue'  //引入vue组件
import router from './router' // 引入路由配置文件

Vue.config.productionTip = false // 关闭生产模式下给出的提示

new Vue({  // 创建一个 Vue 的根实例
  router, // 注入路由配置
  render: h => h(App) // 使用 render 函数渲染 App 组件
}).$mount('#app') // 挂载到 #app 元素