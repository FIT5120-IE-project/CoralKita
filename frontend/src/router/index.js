import Vue from 'vue' // Import Vue
import Router from 'vue-router' // Import vue-router library
import VideoBackground from '@/component/VideoBackground' // Import VideoBackground.vue component
import VideoBackgroundSimple from '@/component/VideoBackgroundSimple' // Import simplified video background component
import Education from '@/component/Education' // Import education component
import QuizPage from '@/component/QuizPage' // Import quiz page component
import TravelChecklist from '@/component/TravelChecklist' // Import travel checklist component
import Government from '@/component/Government' // Import government portal component
import IslandDetail from '@/component/IslandDetail' // Import island detail component
import AIToolsSelector from '@/component/AIToolsSelector' // Import AI tools selector component
import AIClassification from '@/component/AIClassification' // Import AI image classification component
import ActionHub from '@/component/ActionHub' // Import Action Hub component
import Policies from '@/component/Policies' // Import Policies component
import FAQ from '@/component/FAQ' // Import FAQ component
Vue.use(Router) // Globally register Router component, it will be bound to Vue instance.

const router = new Router({ // Create router instance, then pass `routes` configuration
  mode: 'history',
  scrollBehavior (to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { x: 0, y: 0 }
    }
  },
  routes: [
    {
      path: '/',  // Home page path
      name: 'VideoBackgroundSimple', // Route name
      component: VideoBackgroundSimple // Accessed component, simplified video background homepage
    },
    {
      path: '/video',  // Complete video page path
      name: 'VideoBackground', // Route name
      component: VideoBackground // Accessed component, complete video background
    },
    {
      path: '/trends',  // Redirect old trends path
      redirect: '/map'
    },
    {
      path: '/map',  // Map page path (original trends page)
      name: 'TrendsVisualization', // Route name
      component: () => import(/* webpackChunkName: "map" */ '@/component/TrendsVisualization.vue'), // Lazy load map component
      meta: { 
        title: 'Map',
        keepAlive: true // Enable component caching
      }
    },
    {
      path: '/education',  // Education page path
      name: 'Education', // Route name
      component: Education // Accessed component, education center
    },
    {
      path: '/quiz',  // Quiz page path
      name: 'QuizPage', // Route name
      component: QuizPage // Accessed component, quiz page
    },
    {
      path: '/travel-checklist',  // Travel checklist page path
      name: 'TravelChecklist', // Route name
      component: TravelChecklist // Accessed component, travel checklist
    },
    {
      path: '/government',  // Government portal page path
      name: 'Government', // Route name
      component: Government // Accessed component, government portal
    },
    {
      path: '/ai-tools',  // AI tools selector page path
      name: 'AIToolsSelector', // Route name
      component: AIToolsSelector // Accessed component, AI tools selector
    },
    {
      path: '/ai-classification',  // AI image classification page path
      name: 'AIClassification', // Route name
      component: AIClassification // Accessed component, AI image classification
    },
    {
      path: '/action-hub',  // Action Hub page path
      name: 'ActionHub', // Route name
      component: ActionHub // Accessed component, Action Hub
    },
    {
      path: '/policies',  // Policies page path
      name: 'Policies', // Route name
      component: Policies // Accessed component, Policies
    },
    {
      path: '/faq',  // FAQ page path
      name: 'FAQ', // Route name
      component: FAQ // Accessed component, FAQ
    },
    {
      path: '/travel/:islandName',  // Island detail page path
      name: 'IslandDetail', // Route name
      component: IslandDetail, // Accessed component, island detail
      props: true // Enable props to pass route parameters
    }
  ]
})

// Route guard - no special handling needed for now
router.beforeEach((to, from, next) => {
  next()
})

export default router
