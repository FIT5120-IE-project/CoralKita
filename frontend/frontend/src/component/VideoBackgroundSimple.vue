<template>
  <div class="main-container">
    <!-- Top Navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <!-- Left side logo -->
        <img src="@/assets/icon.png" alt="logo" class="nav-logo" />
        <h1 class="logo">CoralKita</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <div class="nav-item-wrapper">
            <span class="nav-item map-rec-item" @click="goToMap">
              <span class="nav-text-line">Map &</span>
              <span class="nav-text-line">Recommendation</span>
            </span>
          </div>
          <div class="nav-item-dropdown" @mouseenter="showTravelDropdown = true" @mouseleave="showTravelDropdown = false">
            <span class="nav-item">Island</span>
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
          <span class="nav-item" @click="goToEducation">Education</span>
          <span class="nav-item">AI Tool</span>
          <span class="nav-item">About Us</span>
          <!-- Language Toggle Button -->
          <div class="language-toggle">
            <button class="lang-btn" @click="toggleLanguage">
              {{ currentLanguage === 'en' ? 'EN' : '中' }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 滚动内容区域 -->
    <div class="scroll-content">
      
      
      <!-- 珊瑚礁状态展示区域 -->
      <div class="coral-status-sections">
        <!-- 顶部区域：彩色珊瑚礁 -->
        <div class="status-section top-section">
          <div class="section-content">
            <div class="image-container">
              <img src="@/assets/mainpage_colorful.jpg" alt="Colorful Coral Reef" class="status-image" />
            </div>
            <div class="text-container">
              <h3 class="status-title">Is this the coral reef you remember — bright, colorful, and alive?</h3>
            </div>
          </div>
      </div>
      
        <!-- 中部区域：白化过程 -->
        <div class="status-section middle-section">
          <div class="section-content">
            <div class="text-container">
              <h3 class="status-title">Today, most reefs are fading, struggling under climate change and human pressure.</h3>
            </div>
            <div class="image-container">
              <img src="@/assets/mainpage_bleading.png" alt="Bleaching Coral" class="status-image" />
            </div>
          </div>
        </div>
        
        <!-- 底部区域：完全白化 -->
        <div class="status-section bottom-section">
          <div class="section-content">
            <div class="image-container">
              <img :src="require('@/assets/mainpage_bleacheedCoral.webp')" alt="Bleached Coral" class="status-image" />
            </div>
            <div class="text-container">
              <h3 class="status-title">Without action, they may soon become lifeless white skeletons like this.</h3>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 底部内容区域 -->
      <div class="bottom-content">
        <!-- 主标题 -->
        <div class="intro-section">
          <h2>Plan Smart. Travel Green. Protect Reefs.</h2>
          <p>CoralKita guides your trip with real reef data — helping you choose the right island, at the right time, the right way, so you can enjoy Malaysia's reefs while protecting their beauty.</p>
        </div>
        
        <!-- 特点标签 -->
        <div class="features-section">
          <div class="feature-tag">
            <span class="feature-icon">📊</span>
            <span class="feature-text">Data-Driven</span>
          </div>
          <div class="feature-tag">
            <span class="feature-icon">🌱</span>
            <span class="feature-text">Responsible</span>
          </div>
          <div class="feature-tag">
            <span class="feature-icon">👥</span>
            <span class="feature-text">User-friendly</span>
          </div>
        </div>
        
        <!-- 探索按钮 -->
        <div class="explore-section">
          <button class="explore-btn" @click="goToMap">
            Let's Explore
          </button>
        </div>
       </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'VideoBackgroundSimple',
  data() {
    return {
      currentLanguage: 'en',
      // Island dropdown related
      showTravelDropdown: false,
      travelIslands: ['Mertang', 'P Singa', 'Sipadan', 'Pulau Lima', 'Seri Buat']
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
    
    goToMap() {
      console.log('Navigating to Map page');
      this.$router.push('/map');
    },

    goToEducation() {
      console.log('Navigating to Education page');
      this.$router.push('/education');
    },

    goToIslandDetail(islandName) {
      console.log('导航到岛屿详情页面:', islandName);
      this.showTravelDropdown = false;
      
      this.$nextTick(() => {
        this.$router.push(`/travel/${encodeURIComponent(islandName)}`).catch(err => {
          if (err.name !== 'NavigationDuplicated') {
            console.error('Navigation error:', err);
          }
        });
      });
    },

    toggleLanguage() {
      this.currentLanguage = this.currentLanguage === 'en' ? 'zh' : 'en';
      console.log('Language switched to:', this.currentLanguage);
      // 这里可以添加语言切换的逻辑
    },

  },
  mounted() {
    console.log('VideoBackgroundSimple 组件已挂载');
    
    // Set global refresh detection timestamp for verification system
    localStorage.setItem('lastPageRefresh', Date.now().toString());
  }
}
</script>

<style scoped>
.main-container {
  min-height: 100vh;     /* ✅ 改成 min-height，而不是 height */
  display: flex;              /* 用 flex 布局 */
  flex-direction: column;     /* 垂直排列 */
  align-items: center;        /* 水平居中 */
  justify-content: flex-start;/* 顶部对齐，让内容自然往下排 */
  padding-bottom: 100px;      /* 页面底部强制留 100px 空白 */
}

/* 用伪元素承载背景图 */
.main-container::after {
  content: "";                        /* 必须加 */
  position: fixed;                    /* 固定在屏幕，不随滚动动 */
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;

  background-image: url('@/assets/bg_mainpage.jpg');
  background-repeat: no-repeat;
  background-position: top center;    /* 上边居中 */
  background-size: 100% auto;         /* 宽度撑满，高度按比例 */
  
  z-index: -1;                        /* 放在内容后面 */
  pointer-events: none;               /* 不影响点击操作 */
}

/* 自定义滚动条样式 */
.main-container::-webkit-scrollbar {
  width: 12px;
}

.main-container::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 6px;
}

.main-container::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 6px;
  border: 2px solid transparent;
  background-clip: content-box;
}

.main-container::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.5);
  background-clip: content-box;
}

/* 创建一个滚动内容区域 */
.scroll-content {
  position: relative;
  height: 300vh; /* 与背景图片高度一致 */
  z-index: 1;
}

/* 创建渐变遮罩层，增强珊瑚礁健康到白化的视觉效果 */
.scroll-content::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;

  z-index: 1;
  pointer-events: none;
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
  transform-origin: center center;
  will-change: transform;
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
  flex-wrap: wrap;
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

.nav-item {
  padding: 8px 16px;
  color: rgba(255, 255, 255, 0.7);
  border-bottom: 2px solid transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  font-size: 1rem;
  white-space: nowrap;
}

.nav-item:hover {
  color: white;
  border-bottom-color: rgba(255, 255, 255, 0.5);
}

.nav-item.active {
  color: white;
  border-bottom-color: #63b3ed;
}

/* Language Toggle */
.language-toggle {
  margin-left: 16px;
}

.lang-btn {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  font-size: 0.9rem;
  min-width: 40px;
}

.lang-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
  transform: scale(1.05);
}

/* Island 下拉菜单样式 */
.nav-item-dropdown {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 8px);
  left: 50%;
  transform: translateX(-50%);
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(25px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15), 0 4px 16px rgba(0, 0, 0, 0.1);
  min-width: 220px;
  z-index: 1000;
  overflow: hidden;
  animation: dropdownSlideIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 添加小箭头指向Island */
.dropdown-menu::before {
  content: '';
  position: absolute;
  top: -6px;
  left: 50%;
  transform: translateX(-50%);
  width: 12px;
  height: 12px;
  background: rgba(255, 255, 255, 0.98);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-bottom: none;
  border-right: none;
  transform: translateX(-50%) rotate(45deg);
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
  font-weight: 500;
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-item:hover {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.08), rgba(16, 185, 129, 0.08));
  color: #1e40af;
  transform: translateX(2px);
}





/* 珊瑚礁状态展示区域样式 */
.coral-status-sections {
  position: relative;
  width: 100%;
  z-index: 10;
  margin: 100px 0;
  transform-origin: center center;
  will-change: transform;
}

.status-section {
  width: 100%;
  min-height: 70vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 40px;
  margin: 40px 0;
  transform-origin: center center;
  will-change: transform;
}

.section-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  max-width: 1700px;
  gap: 10px;
  flex-wrap: nowrap;
  min-width: 0;
  transform-origin: center center;
  will-change: transform;
}

.image-container {
  flex: 0.9;
  max-width: 650px;
  min-width: 300px;
  position: relative;
  flex-shrink: 0;
  transform-origin: center center;
  will-change: transform;
}

.status-image {
  width: 100%;
  height: auto;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.status-image:hover {
  transform: scale(1.05);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.4);
}

.text-container {
  flex: 1.1;
  max-width: 700px;
  min-width: 250px;
  padding: 40px;
  margin-right: -40px;
  min-height: 2.4em; /* 最小高度为两行 */
  flex-shrink: 0;
  transform-origin: center center;
  will-change: transform;
}

.status-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: rgba(24, 40, 58, 1);
  line-height: 1.2;
  text-shadow: 2px 2px 4px rgba(255, 255, 255, 0.5);
  margin: 0;
  text-align: left;
  max-width: none;
  display: block;
  word-wrap: break-word;
  word-break: break-word;
}

/* 顶部区域：图片在左，文字在右 */
.top-section .section-content {
  flex-direction: row;
}

/* 中部区域：文字在左，图片在右 */
.middle-section .section-content {
  flex-direction: row;
}

/* 底部区域：图片在左，文字在右 */
.bottom-section .section-content {
  flex-direction: row;
}

/* 防止缩放时布局破坏 */
@media (max-width: 1200px) {
  .section-content {
    gap: 8px;
  }
  
  .image-container {
    min-width: 280px;
  }
  
  .text-container {
    min-width: 220px;
    max-width: 600px;
  }
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .section-content {
    gap: 15px;
  }
  
  .status-title {
    font-size: 1.6rem;
  }
  
  .text-container {
    padding: 30px;
  }
  
  .image-container {
    max-width: 700px;
  }
}

@media (max-width: 768px) {
  .status-section {
    min-height: 60vh;
    padding: 80px 30px;
    margin: 40px 0;
  }
  
  .section-content {
    flex-direction: column !important;
    gap: 15px;
    text-align: center;
  }
  
  .status-title {
    text-align: center;
    font-size: 1.4rem;
  }
  
  .image-container,
  .text-container {
    max-width: 100%;
    flex: none;
  }
  
  .text-container {
    padding: 30px;
  }
}

@media (max-width: 480px) {
  .status-section {
    padding: 60px 20px;
    margin: 30px 0;
  }
  
  .section-content {
    gap: 10px;
  }
  
  .status-title {
    font-size: 1.3rem;
  }
  
  .text-container {
    padding: 25px;
  }
}

/* 底部内容区域样式 */
.bottom-content {
  margin: 120px auto 120px;       /* 顶部 120px 间距，底部由外层 padding 控制 */
  width: fit-content;
  max-width: 900px;
  min-width: 600px;
  padding: 40px 50px 35px;
  background: rgba(255, 255, 255, 0.46);
  backdrop-filter: blur(10px);
  z-index: 10;
  border-radius: 25px;
  border: 10px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  height: auto;              /* 不写死高度，让内容决定 */
  min-height: 500px;         /* 如果你想保持大概高度，用 min-height */
}

/* CoralKita简介样式 */
.intro-section {
  text-align: center;
  margin-bottom: 30px;
 
}

.intro-section h2 {
  font-size: 2.5rem;
  font-weight: 700;
  color: rgba(21, 48, 71, 0.8);
  background: linear-gradient(90deg, rgba(26, 29, 37, 0.95) 0%, rgba(43, 135, 179, 0.95) 100%);
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(255, 255, 255, 0.1);
  -webkit-background-clip: text;   /* 关键：裁剪背景到文字 */
  -webkit-text-fill-color: transparent; /* 透明文字，显示背景 */
}

.intro-section p {
  font-size: 1.2rem;
  color: rgba(17, 39, 58, 0.8);
  line-height: 1.6;
  max-width: 800px;
  margin: 0 auto;
  text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.1);
}

/* 目标介绍样式 */
.goals-section {
  text-align: center;
  margin-bottom: 50px;
}

.goals-section h3 {
  font-size: 2rem;
  font-weight: 600;
  color: rgba(26, 60, 89, 0.8);
  margin-bottom: 16px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);
}

.goals-section p {
  font-size: 1.1rem;
  color: rgba(26, 60, 89, 0.8);
  line-height: 1.6;
  max-width: 700px;
  margin: 0 auto;
  text-shadow: 1px 1px 2px rgba(83, 83, 83, 0.8);
  font-weight: bold;
}

/* 特点标签样式 */
.features-section {
  display: flex;
  justify-content: center;
  gap: 25px;
  margin-bottom: 35px;
  flex-wrap: wrap;
}

.feature-tag {
  background: rgba(255, 255, 255, 0.48);

  backdrop-filter: blur(5px);
  border-radius: 20px;
  padding: 15px 15px;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.feature-tag:hover {
  background: linear-gradient(45deg, #427aba27, #87bbeb7d);
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.feature-icon {
  font-size: 1.5rem;
}

.feature-text {
  color: rgba(75, 112, 157, 1);
  font-weight: 600;
  font-size: 1rem;
  text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.1);
}

/* 探索按钮样式 */
.explore-section {
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.explore-btn {
  width: 120px;
  height: 120px;
  font-size: 16px;
  font-weight: bold;
  color: white;
  background: linear-gradient(45deg, #427abaff, #488fd2ff);
  border: none;
  border-radius: 50%;
  cursor: pointer;
  transition: 0.3s ease-in-out;
  box-shadow: 0 0 10px rgba(0, 114, 255, 0.5);
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.explore-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 0 20px rgba(0, 114, 255, 0.8);
}

/* 悬停时的光晕效果 */
.explore-btn::after {
  content: "";
  position: absolute;
  width: 200%;
  height: 300%;
  top: -100%;
  left: -50%;
  background: radial-gradient(circle, rgba(255,255,255,0.4) 10%, transparent 10.01%);
  background-size: 50px 50px;
  animation: glow 2s linear infinite;
  opacity: 0;
}

.explore-btn:hover::after {
  opacity: 1;
}

@keyframes glow {
  0% {
    background-position: 0 0;
  }
  100% {
    background-position: 100px 100px;
  }
}

/* 移除了不再需要的按钮和动画样式 */

/* 响应式 */
@media (max-width: 1024px) {
  .nav-items {
    gap: 20px;
  }
  
  .nav-item {
    font-size: 0.9rem;
    padding: 6px 12px;
  }
}

@media (max-width: 768px) {
  .top-nav {
    padding: 8px 16px;
  }
  
  .nav-left .logo {
    font-size: 1.3rem;
  }
  
  .nav-logo {
    height: 38px;
  }
  
  .nav-items {
    gap: 16px;
    flex-wrap: wrap;
  }
  
  .nav-item {
    font-size: 0.85rem;
    padding: 4px 8px;
  }
  
  .lang-btn {
    padding: 4px 8px;
    font-size: 0.8rem;
    min-width: 35px;
  }
  
  .top-scroll-hint {
    top: 90px;
  }
  
  .top-scroll-hint p {
    font-size: 1rem;
    padding: 10px 20px;
  }
  
  /* 分界线箭头响应式 */
  .section-arrow {
    width: 55px;
    height: 55px;
    top: -35px;
  }
  
  .arrow-icon {
    font-size: 1.6rem;
  }
  
  /* 底部内容响应式 */
  .bottom-content {
    padding: 35px 40px 30px;
    bottom: 120px;
    min-width: 500px;
    max-width: 700px;
  }
  
  .intro-section h2 {
    font-size: 2rem;
  }
  
  .intro-section p {
    font-size: 1.1rem;
  }
  
  .goals-section h3 {
    font-size: 1.6rem;
  }
  
  .goals-section p {
    font-size: 1rem;
  }
  
  .features-section {
    gap: 20px;
  }
  
  .feature-tag {
    padding: 15px 20px;
    flex: 1;
    min-width: 200px;
  }
  
  .feature-text {
    font-size: 0.9rem;
  }
  
  .explore-btn {
    width: 100px;
    height: 100px;
    font-size: 14px;
  }
  
  /* 移动端滚动条样式调整 */
  .main-container::-webkit-scrollbar {
    width: 8px;
  }
}

@media (max-width: 480px) {
  .nav-items {
    gap: 8px;
  }
  
  .nav-item {
    font-size: 0.75rem;
    padding: 4px 6px;
  }
  
  /* Map & Recommendation换行显示 */
  .nav-item-wrapper .nav-item {
    font-size: 0.7rem;
    text-align: center;
    line-height: 1.2;
    padding: 2px 4px;
  }
  
  .top-scroll-hint {
    top: 80px;
  }
  
  .top-scroll-hint p {
    font-size: 0.9rem;
    padding: 8px 16px;
  }
  
  /* 小屏幕分界线箭头 */
  .section-arrow {
    width: 50px;
    height: 50px;
    top: -25px;
  }
  
  .arrow-icon {
    font-size: 1.4rem;
  }
  
  /* 小屏幕底部内容 */
  .bottom-content {
    padding: 25px 30px 20px;
    bottom: 80px;
    min-width: 350px;
    max-width: 600px;
  }
  
  .intro-section h2 {
    font-size: 1.8rem;
  }
  
  .intro-section p {
    font-size: 1rem;
  }
  
  .goals-section h3 {
    font-size: 1.4rem;
  }
  
  .goals-section p {
    font-size: 0.95rem;
  }
  
  .features-section {
    flex-direction: column;
    gap: 15px;
  }
  
  .feature-tag {
    padding: 12px 16px;
    min-width: auto;
  }
  
  .feature-icon {
    font-size: 1.3rem;
  }
  
  .feature-text {
    font-size: 0.85rem;
  }
  
  .explore-btn {
    width: 80px;
    height: 80px;
    font-size: 12px;
  }

}
</style>
