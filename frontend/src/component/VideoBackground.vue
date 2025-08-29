<template>
  <div class="video-background-container">
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
    <div class="video-overlay"></div>
    
    <!-- 主要内容 -->
    <div class="main-content">
      <div class="hero-section">
        <h1 class="main-title">CoralKita</h1>
        <p class="subtitle">Protect marine ecosystems, experience the beauty of nature</p>
        <p class="description">
            Explore the wondrous world of coral reefs, understand the importance of marine conservation, and participate in sustainable tourism practices.
        </p>
        <div class="action-buttons">
          <button class="btn primary">Begin exploring</button>
          <button class="btn secondary">Find out more</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'VideoBackground',
  data() {
    return {
      videoSrc: require('../assets/theme.mp4')
    }
  },
  mounted() {
    // 确保视频自动播放
    this.$nextTick(() => {
      const video = this.$refs.backgroundVideo;
      if (video) {
        video.play().catch(err => {
          console.log('视频自动播放失败:', err);
        });
      }
    });
  }
}
</script>

<style scoped>
.video-background-container {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.background-video {
  position: absolute;
  top: 50%;
  left: 50%;
  min-width: 100%;
  min-height: 100%;
  width: auto;
  height: auto;
  transform: translateX(-50%) translateY(-50%);
  z-index: -2;
  object-fit: cover;
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    135deg,
    rgba(0, 100, 150, 0.3) 0%,
    rgba(0, 50, 100, 0.4) 50%,
    rgba(0, 30, 80, 0.5) 100%
  );
  z-index: -1;
}

.main-content {
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.hero-section {
  text-align: center;
  color: white;
  max-width: 800px;
  padding: 0 20px;
}

.main-title {
  font-size: 3.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);
  animation: fadeInUp 1s ease-out;
}

.subtitle {
  font-size: 1.5rem;
  font-weight: 300;
  margin-bottom: 1.5rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.7);
  animation: fadeInUp 1s ease-out 0.3s both;
}

.description {
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 2.5rem;
  opacity: 0.9;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.7);
  animation: fadeInUp 1s ease-out 0.6s both;
}

.action-buttons {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
  animation: fadeInUp 1s ease-out 0.9s both;
}

.btn {
  padding: 12px 30px;
  border: none;
  border-radius: 30px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
}

.btn.primary {
  background: linear-gradient(135deg, #00a8cc, #0077b6);
  color: white;
  box-shadow: 0 4px 15px rgba(0, 168, 204, 0.3);
}

.btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 168, 204, 0.4);
}

.btn.secondary {
  background: transparent;
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.8);
}

.btn.secondary:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: white;
  transform: translateY(-2px);
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

/* 响应式设计 */
@media (max-width: 768px) {
  .main-title {
    font-size: 2.5rem;
  }
  
  .subtitle {
    font-size: 1.2rem;
  }
  
  .description {
    font-size: 1rem;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .btn {
    width: 200px;
  }
}

@media (max-width: 480px) {
  .main-title {
    font-size: 2rem;
  }
  
  .hero-section {
    padding: 0 15px;
  }
}
</style>
