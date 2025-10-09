<template>
  <div class="map-container">
    <!-- 背景图片加载占位符 -->
    <div class="bg-placeholder" v-if="!backgroundLoaded">
      <div class="progress-container">
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: loadingProgress + '%' }"></div>
        </div>
        <p class="loading-text">{{ loadingText }}</p>
      </div>
    </div>
    
    <!-- 独立的进度条容器 -->
    <div class="top-progress-container" v-if="!backgroundLoaded">
      <div class="progress-bar">
        <div class="progress-fill" :style="{ width: loadingProgress + '%' }"></div>
      </div>
      <p class="loading-text">{{ loadingText }}</p>
    </div>
    
    <!-- Top Navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <!-- Left side logo -->
        <img :src="appIconUrl" alt="logo" class="nav-logo" @click="goToHome" />
        <h1 class="logo" @click="goToHome">{{ $t('nav.logo') }}</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <div class="nav-item-dropdown" @mouseenter="showTravelDropdown = true" @mouseleave="showTravelDropdown = false">
            <div class="nav-item-wrapper">
              <span class="nav-item map-rec-item active">
                <span class="nav-text-line">{{ $t('nav.mapRecommendation.line1') }}</span>
                <span class="nav-text-line">{{ $t('nav.mapRecommendation.line2') }}</span>
              </span>
            </div>
            <div class="dropdown-menu" v-show="showTravelDropdown">
              <div 
                v-for="island in travelIslands" 
                :key="island"
                class="dropdown-item"
                @click="goToIslandDetail(island)"
              >
                <span>{{ island }}</span>
              </div>
            </div>
          </div>
          <div class="nav-item-dropdown" @mouseenter="showEducationDropdown = true" @mouseleave="showEducationDropdown = false">
            <span class="nav-item" @click="goToEducation">{{ $t('nav.education') }}</span>
            <div class="dropdown-menu" v-show="showEducationDropdown">
              <div class="dropdown-item" @click="goToEducation">
                <span>{{ $t('education.dropdown.tourismHub') }}</span>
              </div>
              <div class="dropdown-item" @click="goToTravelChecklist">
                <span>{{ $t('education.dropdown.tourismChecklist') }}</span>
              </div>
            </div>
          </div>
          <span class="nav-item" @click="goToAITools">{{ $t('nav.aiClassification') }}</span>
          <span class="nav-item" @click="goToGovernment">{{ $t('nav.government') }}</span>
          <span class="nav-item" @click="goToFAQ">{{ $t('nav.faq') }}</span>
          <LanguageSwitcher />
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <!-- Game Mode Toggle -->
      <div class="mode-toggle">
        <button 
          class="toggle-btn" 
          :class="{ active: showGame }" 
          @click="showGame = true"
        >
          {{ $t('map.gameMode') }}
        </button>
        <button 
          class="toggle-btn" 
          :class="{ active: !showGame }" 
          @click="showGame = false"
        >
          {{ $t('map.mapMode') }}
        </button>
      </div>

      <!-- Game Content -->
      <div v-if="showGame" class="game-content">
        <div class="game-placeholder">
          <h2>{{ $t('map.game.title') }}</h2>
          <p>{{ $t('map.game.unavailable') }}</p>
          <p>{{ $t('map.game.switchBack') }}</p>
        </div>
      </div>

      <!-- Map Content -->
      <div v-else class="coming-soon-card">
        <div class="icon-container">
          <div class="map-icon">🗺️</div>
        </div>
        <h1>{{ $t('map.comingSoon.title') }}</h1>
        <h2>{{ $t('map.comingSoon.subtitle') }}</h2>
        <p>{{ $t('map.comingSoon.description') }}</p>
        
        <div class="features-preview">
          <h3>{{ $t('map.comingSoon.featuresTitle') }}</h3>
          <ul>
            <li>{{ $t('map.comingSoon.features.locations') }}</li>
            <li>{{ $t('map.comingSoon.features.visualization') }}</li>
            <li>{{ $t('map.comingSoon.features.health') }}</li>
            <li>{{ $t('map.comingSoon.features.info') }}</li>
            <li>{{ $t('map.comingSoon.features.mobile') }}</li>
          </ul>
        </div>

        <button class="back-button" @click="goBack">
          {{ $t('map.comingSoon.backButton') }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import ossService from '@/utils/ossService.js'
import LanguageSwitcher from './LanguageSwitcher.vue'

export default {
  name: 'Map',
  components: {
    LanguageSwitcher
  },
  data() {
    return {
      backgroundLoaded: false, // 背景图片加载状态
      loadingProgress: 0, // 加载进度
      loadingText: this.$t('map.loading.loading'), // 加载文本
      showGame: true, // 默认显示游戏
      appIconUrl: null, // 应用图标URL
      backgroundImageUrl: null, // 背景图片URL
      showTravelDropdown: false,
      showEducationDropdown: false,
      travelIslands: ['Mertang', 'P Singa', 'Sipadan', 'Pulau Lima', 'Seri Buat']
    }
  },
  mounted() {
    // 立即开始预加载背景图片
    this.preloadBackgroundImage();
    
    // 加载应用图标
    this.loadAppIcon();
    
    // 加载背景图片
    this.loadBackgroundImage();
    
    // Set global refresh detection timestamp for verification system
    localStorage.setItem('lastPageRefresh', Date.now().toString());
  },
  methods: {
    /**
     * 加载应用图标
     */
    async loadAppIcon() {
      try {
        this.appIconUrl = await ossService.getFileUrl('assets/icon.png')
      } catch (error) {
        console.warn('加载应用图标失败，使用默认图标:', error)
        this.appIconUrl = null
      }
    },

    /**
     * 加载背景图片
     */
    async loadBackgroundImage() {
      try {
        this.backgroundImageUrl = await ossService.getFileUrl('bg_login5.webp')
        // 设置CSS变量
        document.documentElement.style.setProperty('--bg-image', `url(${this.backgroundImageUrl})`)
      } catch (error) {
        console.warn('加载背景图片失败，使用默认图片:', error)
        this.backgroundImageUrl = null
      }
    },

    /**
     * 预加载背景图片
     */
    preloadBackgroundImage() {
      // 创建高优先级预加载链接元素
      const preloadLink = document.createElement('link');
      preloadLink.rel = 'preload';
      preloadLink.as = 'image';
      preloadLink.href = this.backgroundImageUrl;
      preloadLink.fetchPriority = 'high'; // 高优先级
      
      // 添加到head中
      document.head.appendChild(preloadLink);
      
      // 模拟加载进度
      this.simulateLoadingProgress();
      
      // 预加载图片到浏览器缓存
      const img = new Image();
      img.src = this.backgroundImageUrl;
      img.onload = () => {
        console.log('Map background image preloaded to cache');
        this.loadingProgress = 100;
        this.loadingText = this.$t('map.loading.success');
        setTimeout(() => {
          this.backgroundLoaded = true;
        }, 500);
      };
      img.onerror = () => {
        console.warn('Failed to preload Map background image');
        this.loadingProgress = 100;
        this.loadingText = this.$t('map.loading.backup');
        setTimeout(() => {
          this.backgroundLoaded = true; // 即使失败也隐藏占位符
        }, 500);
      };
      
      console.log('Map background image preload started');
    },

    /**
     * 模拟加载进度
     */
    simulateLoadingProgress() {
      const progressSteps = [
        { progress: 20, text: this.$t('map.loading.connecting') },
        { progress: 40, text: this.$t('map.loading.loading') },
        { progress: 60, text: this.$t('map.loading.locating') },
        { progress: 80, text: this.$t('map.loading.generating') },
        { progress: 95, text: this.$t('map.loading.almostReady') }
      ];

      let currentStep = 0;
      const updateProgress = () => {
        if (currentStep < progressSteps.length) {
          const step = progressSteps[currentStep];
          this.loadingProgress = step.progress;
          this.loadingText = step.text;
          currentStep++;
          setTimeout(updateProgress, 800);
        }
      };

      updateProgress();
    },

    goToHome() {
      window.location.href = '/';
    },

    goToEducation() {
      // 主页面导航，不设置标记，应该显示验证
      this.$router.push('/education');
    },

    goToTravelChecklist() {
      this.$router.push('/travel-checklist').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToIslandDetail(island) {
      this.$router.push(`/travel/${island}`);
    },

    goToAITools() {
      this.$router.push('/ai-tools');
    },

    goToActionHub() {
      this.$router.push('/action-hub').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToPolicies() {
      this.$router.push('/policies').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToFAQ() {
      this.$router.push('/faq').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goBack() {
      // Go back to previous page
      if (window.history.length > 1) {
        this.$router.go(-1);
      } else {
        // Fallback to home page
        this.goToHome();
      }
    },

  }
}
</script>

<style scoped>
.map-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow-x: hidden;
  padding-top: 80px;
  /* 优化背景图片加载 */
  will-change: transform;      /* 提示浏览器优化 */
  transform: translateZ(0);     /* 启用硬件加速 */
}

/* 海洋主题背景加载占位符样式 */
.bg-placeholder {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #0f4c75 0%, #3282b8 25%, #0f4c75 50%, #1e3a8a 75%, #0f4c75 100%);
  background-size: 400% 400%;
  animation: oceanWave 8s ease-in-out infinite;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  color: white;
  font-size: 18px;
  overflow: hidden;
}

.bg-placeholder::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="waves" x="0" y="0" width="100" height="100" patternUnits="userSpaceOnUse"><path d="M0,50 Q25,30 50,50 T100,50 L100,100 L0,100 Z" fill="rgba(255,255,255,0.1)"/></pattern></defs><rect width="100" height="100" fill="url(%23waves)"/></svg>');
  animation: waveMotion 6s ease-in-out infinite;
}



@keyframes oceanWave {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

@keyframes waveMotion {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-10px) rotate(1deg); }
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-15px) rotate(5deg); }
}


/* 海洋主题进度条样式 */
.progress-container {
  position: absolute;
  top: -40px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1001;
  width: 300px;
  text-align: center;
}

/* 顶部独立进度条样式 */
.top-progress-container {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1002;
  width: 300px;
  text-align: center;
  background: rgba(0, 0, 0, 0.3);
  padding: 10px 20px;
  border-radius: 10px;
  backdrop-filter: blur(10px);
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 15px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.3);
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #00d4ff 0%, #00a8cc 50%, #0077be 100%);
  border-radius: 10px;
  transition: width 0.8s ease-in-out;
  position: relative;
  overflow: hidden;
}

.progress-fill::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
  animation: shimmer 2s infinite;
}

.loading-text {
  color: white;
  font-size: 16px;
  font-weight: 500;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
  margin: 0;
  animation: textGlow 2s ease-in-out infinite alternate;
}

@keyframes shimmer {
  0% { left: -100%; }
  100% { left: 100%; }
}

@keyframes textGlow {
  0% { text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5); }
  100% { text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5), 0 0 10px rgba(0, 212, 255, 0.3); }
}

.map-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: var(--bg-image, url('@/assets/bg_login5.webp'));
  background-repeat: no-repeat;
  background-attachment: fixed;   /* 页面滚动时固定 */
  background-position: center;   /* 居中显示 */
  background-size: cover;         /* 覆盖整个容器，保持比例 */
  opacity: 0.3;
  z-index: 1;
}

/* Top Navigation */
.top-nav {
  background: linear-gradient(90deg, rgba(26, 29, 37, 0.95) 0%, rgba(1, 162, 235, 0.95) 100%);
  backdrop-filter: blur(15px);
  color: white;
  padding: 12px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.3);
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.nav-left .logo {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 8px 12px;
  border-radius: 8px;
}

.nav-left .logo:hover {
  background: rgba(255, 255, 255, 0.1);
  transform: scale(1.05);
  color: #63b3ed;
}

.nav-logo {
  height: 46px;
  cursor: pointer;
}

/* Navigation Items */
.nav-items {
  display: flex;
  gap: 32px;
  align-items: center;
}

.nav-item {
  padding: 8px 16px;
  color: rgba(255, 255, 255, 0.7);
  border-bottom: 2px solid transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  font-size: 1rem;
}

.nav-item:hover {
  color: white;
  border-bottom-color: rgba(255, 255, 255, 0.5);
}

.nav-item.active {
  color: white;
  border-bottom-color: #63b3ed;
}

.nav-item-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.map-rec-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  line-height: 1.1;
}

.nav-text-line {
  display: block;
}

.nav-item-dropdown {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.nav-item-dropdown .nav-item-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.nav-item-dropdown::before {
  content: '';
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  height: 12px;
  background: transparent;
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 4px);
  left: 50%;
  transform: translateX(-50%);
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(25px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15), 0 4px 16px rgba(0, 0, 0, 0.1);
  min-width: 120px;
  max-width: 140px;
  z-index: 1001;
  overflow: hidden;
  animation: dropdownSlideIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes dropdownSlideIn {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(-8px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0) scale(1);
  }
}

.dropdown-item {
  display: flex;
  align-items: center;
  padding: 14px 20px;
  color: #1f2937;
  cursor: pointer;
  transition: all 0.2s ease;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-item:hover {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.08), rgba(16, 185, 129, 0.08));
  color: #1e40af;
  transform: translateX(2px);
}

.dropdown-item:first-child:hover {
  border-radius: 16px 16px 0 0;
}

.dropdown-item:last-child:hover {
  border-radius: 0 0 16px 16px;
}

.dropdown-item:first-child:last-child:hover {
  border-radius: 16px;
}

/* Main Content */
.main-content {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: calc(100vh - 70px);
  padding: 40px 20px;
}

/* Mode Toggle */
.mode-toggle {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
  background: rgba(255, 255, 255, 0.1);
  padding: 5px;
  border-radius: 25px;
  backdrop-filter: blur(10px);
}

.toggle-btn {
  background: transparent;
  color: rgba(255, 255, 255, 0.7);
  border: none;
  padding: 12px 24px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.toggle-btn:hover {
  color: white;
  background: rgba(255, 255, 255, 0.1);
}

.toggle-btn.active {
  background: linear-gradient(135deg, #01A2EB, #1A1D25);
  color: white;
  box-shadow: 0 4px 15px rgba(1, 162, 235, 0.3);
}

/* Game Content */
.game-content {
  width: 100%;
  max-width: 800px;
  display: flex;
  justify-content: center;
}

.game-placeholder {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 25px;
  padding: 50px 40px;
  text-align: center;
  max-width: 600px;
  width: 100%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  animation: slideUp 0.6s ease-out;
}

.game-placeholder h2 {
  color: #1A1D25;
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 20px;
  background: linear-gradient(135deg, #01A2EB, #1A1D25);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.game-placeholder p {
  color: #555;
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 15px;
}

.coming-soon-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 25px;
  padding: 50px 40px;
  text-align: center;
  max-width: 600px;
  width: 100%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.icon-container {
  margin-bottom: 30px;
}

.map-icon {
  font-size: 4rem;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

.coming-soon-card h1 {
  color: #1A1D25;
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 10px;
  background: linear-gradient(135deg, #01A2EB, #1A1D25);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.coming-soon-card h2 {
  color: #01A2EB;
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 20px;
}

.coming-soon-card p {
  color: #555;
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 30px;
}

.features-preview {
  text-align: left;
  margin: 30px 0;
  padding: 25px;
  background: rgba(1, 162, 235, 0.1);
  border-radius: 15px;
  border-left: 4px solid #01A2EB;
}

.features-preview h3 {
  color: #1A1D25;
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 15px;
  text-align: center;
}

.features-preview ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.features-preview li {
  color: #555;
  font-size: 1rem;
  margin-bottom: 12px;
  padding-left: 10px;
  position: relative;
}

.back-button {
  background: linear-gradient(135deg, #01A2EB, #1A1D25);
  color: white;
  border: none;
  padding: 14px 28px;
  border-radius: 25px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(1, 162, 235, 0.3);
  margin-top: 20px;
}

.back-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(1, 162, 235, 0.4);
}

/* Responsive Design */
@media (max-width: 768px) {
  .nav-items {
    gap: 20px;
  }
  
  .mode-toggle {
    flex-direction: column;
    width: 100%;
    max-width: 300px;
  }
  
  .toggle-btn {
    text-align: center;
  }
  
  .coming-soon-card {
    padding: 40px 30px;
    margin: 20px;
  }
  
  .coming-soon-card h1 {
    font-size: 2rem;
  }
  
  .coming-soon-card h2 {
    font-size: 1.5rem;
  }
  
  .features-preview {
    padding: 20px;
  }
  
  .game-content {
    padding: 0 10px;
  }
}

@media (max-width: 480px) {
  .nav-items {
    gap: 15px;
  }
  
  .nav-item {
    font-size: 0.9rem;
    padding: 6px 12px;
  }
  
  .coming-soon-card {
    padding: 30px 20px;
  }
  
  .map-icon {
    font-size: 3rem;
  }
  
  .coming-soon-card h1 {
    font-size: 1.8rem;
  }
}
</style>
