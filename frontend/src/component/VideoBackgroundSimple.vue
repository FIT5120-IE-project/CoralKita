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
      <h1>CoralKita</h1>
      <p>Protect marine ecosystems, experience the beauty of nature</p>
      <p>Explore the wondrous world of coral reefs, understand the importance of marine conservation, and participate in sustainable tourism practices.</p>
      <div class="buttons">
        <button class="btn-primary">Begin exploring</button>
        <button class="btn-secondary">Learn more</button>
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

.content h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);
}

.content p {
  font-size: 1.2rem;
  margin-bottom: 1rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.7);
}

.buttons {
  margin-top: 2rem;
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
  background: linear-gradient(135deg, #00a8cc, #0077b6);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 168, 204, 0.4);
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

/* 响应式 */
@media (max-width: 768px) {
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
</style>
