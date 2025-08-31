<template>
  <div class="video-container">
    <!-- 视频背景 -->
    <video 
      ref="backgroundVideo"
      class="background-video" 
      autoplay 
      muted 
      loop 
      playsinline
    >
      <source :src="videoSrc" type="video/mp4">
      您的浏览器不支持视频播放。
    </video>
    
    <!-- 遮罩层 -->
    <div class="overlay"></div>
    
    <!-- 内容 -->
    <div class="content">
      <!-- Logo图片 -->
      <div class="logo-container">
        <img src="../assets/icon.png" alt="Logo" class="main-logo" />
      </div>
      <h1>CoralKita</h1>

      <!-- 新增描述容器 -->
      <div class="description-box">
        <p>Protect marine ecosystems and embrace nature's beauty</p>
        <p>Discover coral reefs, learn their importance, and support sustainable tourism</p>
      </div>
             <div class="buttons">
         <button class="btn-primary" @click="navigateToTrends">Reef Intelligence Hub</button>
         <button class="btn-secondary">Discover More</button>
       </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'VideoBackgroundSimple',
  data() {
    return {
      videoSrc: require('../assets/theme.mp4')
    }
  },
  methods: {
    navigateToTrends() {
      // 预加载trends组件
      this.preloadTrends();
      // 延迟导航，给预加载一些时间
      setTimeout(() => {
        this.$router.push('/trends');
      }, 100);
    },
    
    preloadTrends() {
      // 预加载trends组件
      const trendsComponent = () => import('@/component/TrendsVisualization.vue');
      trendsComponent();
    },
    

  },
  mounted() {
    console.log('VideoBackgroundSimple 组件已挂载');
    console.log('视频路径:', this.videoSrc);
    
    this.$nextTick(() => {
      const video = this.$refs.backgroundVideo;
      if (video) {
        console.log('找到视频元素');
        video.play().catch(err => {
          console.log('视频自动播放失败:', err);
        });
      }
    });
  }
}
</script>

<style scoped>
.video-container {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}



.background-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 1;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 50, 100, 0.4);
  z-index: 2;
}

.content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: white;
  z-index: 3;
  max-width: 800px;
  padding: 20px;
}

.logo-container {
  margin-bottom: 2rem;
  animation: fadeInDown 1s ease-out;
}

.main-logo {
  max-width: 150px;
  height: auto;
  filter: drop-shadow(2px 2px 8px rgba(0, 0, 0, 0.5));
  transition: transform 0.3s ease;
}

.main-logo:hover {
  transform: scale(1.05);
}

.content h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);
  animation: fadeInUp 1s ease-out 0.3s both;
}

.content p {
  font-size: 1.2rem;
  margin-bottom: 1rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.7);
}

/* 白色半透明圆角背景容器 */
.description-box {
  display: inline-block;
  background: rgba(255, 255, 255, 0.1);
  padding: 16px 24px;              
  border-radius: 16px;               
  margin: 1rem auto 1.5rem;          
  backdrop-filter: blur(3px);
  text-align: center;
}

.description-box p {
  margin: 6px 0;
  color: #ffffffff;       
  font-size: 1.1rem;
  font-weight: 500;
  line-height: 1.5;
}

.buttons {
  margin-top: 1rem;
}

.btn-primary, .btn-secondary {
  padding: 12px 30px;
  margin: 0 10px;
  border: none;
  border-radius: 25px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: linear-gradient(135deg, #264d87ff, #21526dff);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 168, 204, 0.4);
  background: linear-gradient(135deg, #1f4174ff, #1c4358ff);
}

.btn-secondary {
  background: transparent;
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.8);
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: white;
  transform: translateY(-2px);
}

/* 动画效果 */
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式 */
@media (max-width: 768px) {
  .main-logo {
    max-width: 120px;
  }
  
  .content h1 {
    font-size: 2rem;
  }
  
  .content p {
    font-size: 1rem;
  }
  
  .buttons {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  .btn-primary, .btn-secondary {
    margin: 5px 0;
    width: 200px;
  }
}

@media (max-width: 480px) {
  .main-logo {
    max-width: 100px;
  }
  
  .logo-container {
    margin-bottom: 1.5rem;
  }
}
</style>
