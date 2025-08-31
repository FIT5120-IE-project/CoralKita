import Vue from 'vue' // 导入Vue
import Router from 'vue-router' // 导入vue-router 库
import VideoBackground from '@/component/VideoBackground' // 导入VideoBackground.vue 组件
import VideoBackgroundSimple from '@/component/VideoBackgroundSimple' // 导入简化版视频背景组件
import Education from '@/component/Education' // 导入教育组件
Vue.use(Router) //全局注册Router组件，它会绑定到Vue实例里面。

const router = new Router({ // 创建 router 实例，然后传 `routes` 配置
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
    },
    {
      path: '/trends',  //趋势可视化页面路径
      name: 'TrendsVisualization', // 路径名
      component: () => import(/* webpackChunkName: "trends" */ '@/component/TrendsVisualization.vue'), // 懒加载趋势可视化组件
      meta: { 
        title: 'Trends',
        keepAlive: true // 启用组件缓存
      }
    },
    {
      path: '/education',  //教育页面路径
      name: 'Education', // 路径名
      component: Education //访问的组件,教育中心
    }
  ]
})

// 路由守卫 - 暂时不需要特殊处理
router.beforeEach((to, from, next) => {
  next()
})

export default router
