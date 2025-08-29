import Vue from 'vue' // 导入Vue
import Router from 'vue-router' // 导入vue-router 库
import VideoBackground from '@/component/VideoBackground' // 导入VideoBackground.vue 组件
import VideoBackgroundSimple from '@/component/VideoBackgroundSimple' // 导入简化版视频背景组件
Vue.use(Router) //全局注册Router组件，它会绑定到Vue实例里面。
export default new Router({ // 创建 router 实例，然后传 `routes` 配置
  routes: [
    {
      path: '/',  //主页路径
      name: 'VideoBackgroundSimple', // 路径名
      component: VideoBackgroundSimple //访问的组件,简化版视频背景主页
    },
    {
      path: '/video',  //完整视频页面路径
      name: 'VideoBackground', // 路径名
      component: VideoBackground //访问的组件,完整版视频背景
    }
  ]
})
