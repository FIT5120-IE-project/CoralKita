<template>
  <div class="video-background-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-content">
        <div class="loading-spinner"></div>
        <p>Loading CoralKita...</p>
      </div>
    </div>
    
    <!-- 视频背景 -->
    <video 
      v-if="!loading && videoSrc"
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
import axios from 'axios'

export default {
  name: 'VideoBackground',
  data() {
    return {
      videoSrc: '',
      loading: true
    }
  },
  async mounted() {
    // 从OSS获取主题视频URL
    await this.loadThemeVideo();
    
    // 确保视频自动播放
    this.$nextTick(() => {
      const video = this.$refs.backgroundVideo;
      if (video && this.videoSrc) {
        video.play().catch(err => {
          console.log('视频自动播放失败:', err);
        });
      }
    });
  },
  methods: {
    async loadThemeVideo() {
      try {
        const response = await axios.get('/oss/video/url', {
          params: {
            videoFileName: 'themecac38de5a31ddc2259ae.mp4',
            expireSeconds: 7200 // 2小时过期
          }
        });
        
        if (response.data.code === 1) {
          this.videoSrc = response.data.data;
        } else {
          console.error('Failed to get theme video URL:', response.data.msg);
          // 使用本地fallback或隐藏视频
          this.videoSrc = null;
        }
      } catch (error) {
        console.error('Error loading theme video:', error);
        // 使用本地fallback或隐藏视频
        this.videoSrc = null;
      } finally {
        this.loading = false;
      }
    }
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

/* 加载状态样式 */
.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.loading-content {
  text-align: center;
  color: white;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top: 4px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-content p {
  font-size: 18px;
  font-weight: 300;
  margin: 0;
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
