<template>
  <div class="app-wrapper">
    <div class="main-container">
    <!-- Top Navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <!-- Left side logo -->
        <img :src="imageUrls.appIcon" alt="logo" class="nav-logo" />
        <h1 class="logo">{{ $t('nav.logo') }}</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <span class="nav-item" @click="goToMap">{{ $t('nav.mapRecommendation.line1') }}</span>
          <div class="nav-item-dropdown" @mouseenter="showTravelDropdown = true" @mouseleave="showTravelDropdown = false">
            <span class="nav-item">{{ $t('nav.topIsland') }}</span>
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

    <!-- Scrollable content area -->
    <div class="scroll-content">
      
      <!-- Top content area - only keep text description -->
      <div class="top-content">
        <!-- Brand area (with white glass container) -->
        <div class="brand-glass">
          <div class="brand-section">
            <h1 class="brand-title">CoralKita</h1>
          </div>
        </div>
        
        <!-- Main title -->
        <h2 class="main-title">{{ $t('home.intro.title') }}</h2>
      </div>
      
      <!-- Scroll down arrow -->
      <div class="scroll-arrow top-scroll-arrow" 
           @click="scrollToBottomContent" 
           @dblclick="quickJumpToBottomContent"
           :title="$t('home.scrollHint')">
        <img :src="imageUrls.arrowIcon" alt="Scroll Down" class="arrow-icon" />
      </div>
      
      <!-- Coral reef status display area -->
      <div id="coral-section" class="coral-status-sections">
        <!-- Top area: Colorful coral reef -->
        <div class="status-section top-section">
          <div class="section-content">
            <div class="image-container">
              <img :src="imageUrls.colorfulCoral" alt="Colorful Coral Reef" class="status-image" />
            </div>
            <div class="text-container">
              <h3 class="status-title">{{ $t('home.coral.healthy') }}</h3>
            </div>
          </div>
      </div>
      
      <!-- First arrow: Jump to middle area -->
      <div class="scroll-arrow arrow-1" @click="scrollToSection('middle-section')">
        <img :src="imageUrls.arrowIcon" alt="Scroll Down" class="arrow-icon" />
      </div>
      
        <!-- Middle area: Bleaching process -->
        <div id="middle-section" class="status-section middle-section">
          <div class="section-content">
            <div class="text-container">
              <h3 class="status-title">{{ $t('home.coral.fading') }}</h3>
            </div>
            <div class="image-container">
              <img :src="imageUrls.bleachingCoral" alt="Bleaching Coral" class="status-image" />
            </div>
          </div>
        </div>
        
        <!-- Second arrow: Jump to bottom area -->
        <div class="scroll-arrow arrow-2" @click="scrollToSection('bottom-section')">
          <img :src="imageUrls.arrowIcon" alt="Scroll Down" class="arrow-icon" />
          </div>
        
        <!-- Bottom area: Completely bleached -->
        <div id="bottom-section" class="status-section bottom-section">
          <div class="section-content">
            <div class="image-container">
              <img :src="imageUrls.bleachedCoral" alt="Bleached Coral" class="status-image" />
            </div>
            <div class="text-container">
              <h3 class="status-title">{{ $t('home.coral.bleached') }}</h3>
            </div>
          </div>
        </div>
        
        <!-- Third arrow: Jump to website introduction section -->
        <div class="scroll-arrow arrow-3" @click="scrollToSection('bottom-content-section')">
          <img :src="imageUrls.arrowIcon" alt="Scroll Down" class="arrow-icon" />
        </div>
      </div>
      
      <!-- Bottom content area - feature tags and buttons -->
      <div id="bottom-content-section" class="bottom-content">
        <!-- Description text -->
        <div class="description-section">
          <p>{{ $t('home.intro.description') }}</p>
        </div>
        
        <!-- Feature tags -->
        <div class="features-section">
          <div class="feature-tag">
            <span class="feature-icon">📊</span>
            <span class="feature-text">{{ $t('home.features.dataDriven') }}</span>
          </div>
          <div class="feature-tag">
            <span class="feature-icon">🌱</span>
            <span class="feature-text">{{ $t('home.features.responsible') }}</span>
          </div>
          <div class="feature-tag">
            <span class="feature-icon">👥</span>
            <span class="feature-text">{{ $t('home.features.userFriendly') }}</span>
          </div>
        </div>
        
        <!-- Explore button -->
        <div class="explore-section">
          <button class="explore-btn" @click="goToMap">
            {{ $t('home.exploreButton') }}
          </button>
        </div>
      </div>
      
    </div>
    
    </div>
    
    <!-- Footer -->
    <footer class="main-footer">
      <div class="footer-content">
        {{ $t('home.footer.copyright') }}
        <span class="footer-links">
          <div class="contact-info">
            <div class="contact-item">
              <span class="contact-icon">🌐</span>
              <span class="contact-text">{{ $t('footer.website') }}</span>
            </div>
            <div class="contact-item">
              <span class="contact-icon">✉️</span>
              <span class="contact-text">{{ $t('footer.email') }}</span>
            </div>
          </div>
        </span>
      </div>
    </footer>
    
    <!-- Chat Widget -->
    <ChatWidget />
  </div>
</template>

<script>
import ChatWidget from './ChatWidget.vue'
import LanguageSwitcher from './LanguageSwitcher.vue'
import ossService from '@/utils/ossService.js'

export default {
  name: 'VideoBackgroundSimple',
  components: {
    ChatWidget,
    LanguageSwitcher
  },
  data() {
    return {
      currentLanguage: 'en',
      // Island dropdown related
      showTravelDropdown: false,
      showEducationDropdown: false,
      travelIslands: ['Mertang', 'P Singa', 'Sipadan', 'Pulau Lima', 'Seri Buat'],
      // OSS image URLs
      imageUrls: {
        background: '',
        colorfulCoral: '',
        bleachingCoral: '',
        bleachedCoral: '',
        arrowIcon: '',
        appIcon: ''
      }
    }
  },
  mounted() {
    // Load OSS images
    this.loadOssImages();
  },
  methods: {
    /**
     * Load all OSS image URLs
     */
    async loadOssImages() {
      try {
        console.log('Starting to load OSS images...')
        
        // Load all image URLs in parallel
        const [
          background,
          colorfulCoral,
          bleachingCoral,
          bleachedCoral,
          arrowIcon,
          appIcon
        ] = await Promise.all([
          ossService.getMainPageBackgroundUrl(),
          ossService.getColorfulCoralUrl(),
          ossService.getBleachingCoralUrl(),
          ossService.getBleachedCoralUrl(),
          ossService.getArrowIconUrl(),
          ossService.getAppIconUrl()
        ])

        // Update image URLs
        this.imageUrls = {
          background,
          colorfulCoral,
          bleachingCoral,
          bleachedCoral,
          arrowIcon,
          appIcon
        }

        // Dynamically set background image
        this.setBackgroundImage(background)

        console.log('OSS images loaded successfully:', this.imageUrls)
      } catch (error) {
        console.error('Failed to load OSS images:', error)
        // If OSS loading fails, use local images as fallback
        this.loadFallbackImages()
      }
    },

    /**
     * Load fallback local images - removed, completely use CDN
     */
    loadFallbackImages() {
      console.log('CDN loading failed, but not using local image fallback')
      // No longer use require(), completely rely on CDN
      // If CDN fails, images will show as empty or loading failed state
    },

    /**
     * Dynamically set background image
     * @param {string} imageUrl - Image URL
     */
    setBackgroundImage(imageUrl) {
      if (imageUrl) {
        // Dynamically set CSS variables through JavaScript
        document.documentElement.style.setProperty('--main-bg-image', `url('${imageUrl}')`)
      }
    },

    navigateToTrends() {
      // Preload trends component
      this.preloadTrends();
      // Delay navigation to give preloading some time
      setTimeout(() => {
        this.$router.push('/trends');
      }, 100);
    },
    
    preloadTrends() {
      // Preload trends component
      const trendsComponent = () => import('@/component/TrendsVisualization.vue');
      trendsComponent();
    },
    
    goToMap() {
      console.log('Navigating to Map page');
      this.$router.push('/map').then(() => {
        // Directly position to top of page after navigation
        this.$nextTick(() => {
          window.scrollTo(0, 0);
        });
      }).catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToEducation() {
      console.log('Navigating to Education page');
      this.$router.push('/education').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToTravelChecklist() {
      console.log('Navigating to Travel Checklist page');
      this.$router.push('/travel-checklist').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToAITools() {
      console.log('Navigating to AI Tools page');
      this.$router.push('/ai-tools').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToActionHub() {
      console.log('Navigating to Action Hub page');
      this.$router.push('/action-hub').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToPolicies() {
      console.log('Navigating to Policies page');
      this.$router.push('/policies').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToGovernment() {
      console.log('Navigating to Government page');
      this.$router.push('/government').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToFAQ() {
      console.log('Navigating to FAQ page');
      this.$router.push('/faq').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    scrollToSection(sectionId) {
      const element = document.getElementById(sectionId);
      if (element) {
        element.scrollIntoView({ 
          behavior: 'smooth',
          block: 'start'
        });
      }
    },

    scrollToCoralSection() {
      const element = document.getElementById('coral-section');
      if (element) {
        element.scrollIntoView({ 
          behavior: 'smooth',
          block: 'start'
        });
      }
    },

    // Scroll to colorful coral reef area
    scrollToBottomContent() {
      const element = document.getElementById('coral-section');
      if (element) {
        element.scrollIntoView({ 
          behavior: 'smooth',
          block: 'start'
        });
      }
    },

    // Quick jump method (double-click triggered)
    quickJumpToCoralSection() {
      const element = document.getElementById('coral-section');
      if (element) {
        // Immediate jump, no animation
        element.scrollIntoView({ 
          behavior: 'auto',
          block: 'start'
        });
      }
    },

    // Quick jump to colorful coral reef area (double-click triggered)
    quickJumpToBottomContent() {
      const element = document.getElementById('coral-section');
      if (element) {
        // Immediate jump, no animation
        element.scrollIntoView({ 
          behavior: 'auto',
          block: 'start'
        });
      }
    },

    goToIslandDetail(islandName) {
      console.log('Navigate to island detail page:', islandName);
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
      // Language switching logic can be added here
    },


  },
  mounted() {
    console.log('VideoBackgroundSimple component mounted');
    
    // Set global refresh detection timestamp for verification system
    localStorage.setItem('lastPageRefresh', Date.now().toString());
    
    // Load OSS images
    this.loadOssImages();
  }
}
</script>

<style scoped>
.app-wrapper {
  width: 100%;
  min-height: 100vh;
  position: relative;
}


.main-container {
  min-height: 100vh;
  position: relative;
  width: 100%;
  overflow: visible;   /* ✅ 允许内容自然溢出，由 body 滚动 */
}

/* Use pseudo-element to carry background image */
.main-container::after {
  content: "";                        /* Must add */
  position: fixed;                    /* Fixed on screen, not moving with scroll */
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;

  background-image: var(--main-bg-image, url('@/assets/bg_mainpage.webp'));
  background-repeat: no-repeat;
  background-position: center;        /* Center display */
  background-size: cover;              /* Cover entire container, maintain aspect ratio */
  
  z-index: -1;                        /* Place behind content */
  pointer-events: none;               /* Don't affect click operations */
}

/* Custom scrollbar styles */
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

/* Create a scrollable content area */
.scroll-content {
  position: relative;
  min-height: 100vh;   /* ✅ At least one screen height */
  height: auto;        /* ✅ Content determines height itself */
  z-index: 1;
}

/* Create gradient mask layer to enhance visual effect from healthy to bleached coral reef */
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

/* Map & Recommendation 下拉菜单样式 */
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
  min-width: 160px;
  z-index: 1000;
  overflow: hidden;
  animation: dropdownSlideIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Remove small arrow, use figure 2 style */

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





/* Coral reef status display area styles */
.coral-status-sections {
  position: relative;
  width: 100%;
  z-index: 10;
  margin: 60px 0 100px 0;         /* Adjust top margin to leave space for scroll arrows */
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
  min-height: 2.4em; /* Minimum height for two lines */
  flex-shrink: 0;
  transform-origin: center center;
  will-change: transform;
}

.status-title {
  font-size: 2.2rem;
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

/* Top area: Image on left, text on right */
.top-section .section-content {
  flex-direction: row;
}

/* Middle area: Text on left, image on right */
.middle-section .section-content {
  flex-direction: row;
}

/* Bottom area: Image on left, text on right */
.bottom-section .section-content {
  flex-direction: row;
}

/* Prevent layout breaking during scaling */
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

/* Responsive design */
@media (max-width: 1024px) {
  .section-content {
    gap: 15px;
  }
  
  .status-title {
    font-size: 2.0rem;
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
  font-size: 1.8rem;
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
    font-size: 1.6rem;
  }
  
  .text-container {
    padding: 25px;
  }
}

/* Top content area styles */
.top-content {
  margin: 260px auto 60px;       /* Further shorten distance from navigation bar */
  width: fit-content;
  max-width: 900px;
  min-width: 600px;
  padding: 40px 50px 35px;
  z-index: 10;
  height: auto;
  min-height: 150px;              /* Reduce minimum height since only contains text */
  text-align: center;
}



/* Bottom content area styles */
.bottom-content {
  margin: 60px auto 200px;       /* Adjust margins */
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
  height: auto;
  min-height: 200px;
}

/* Brand area styles */
.brand-section {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40px;
}

.brand-title {
  font-size: 6.0rem;
  font-weight: 600;
  line-height: 1.2;
  margin: 0;
  text-align: center;
  display: inline-block;
  word-wrap: break-word;
  word-break: break-word;
 
  /* Blue to dark blue gradient text */
  background: linear-gradient(90deg, rgba(38, 68, 77, 0.95) 0%, rgba(63, 155, 198, 0.95) 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  color: transparent;
 
  /* Key: use drop-shadow for "outer glow" (unaffected by transparent fill) */
  filter:

    drop-shadow(0 0 4px rgba(255, 255, 255, 0.75));
 

 
  /* Optional small optimization to reduce rendering artifacts */
  will-change: filter;
  transform: translateZ(0);
}

/* Main title styles */
.main-title {
  font-size: 2.4rem;
  font-weight: 600;
  line-height: 1.2;
  margin: 0;
  text-align: center;
  display: inline-block;      /* Ensure filter is calculated based on text outline */
  word-wrap: break-word;
  word-break: break-word;
 
  /* Blue to dark blue gradient text */
  background: linear-gradient(135deg, rgb(51, 102, 116) 0%, #224f68 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent; /* Gradient only shows on text */
  color: transparent;
 
  /* Key: use drop-shadow for "outer glow" (unaffected by transparent fill) */
  filter:

    drop-shadow(0 0 5px rgba(255, 255, 255, 0.75));
 

 
  /* Optional small optimization to reduce rendering artifacts */
  will-change: filter;
  transform: translateZ(0);
}

/* Target introduction styles */
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

/* Description text styles */
.description-section {
  text-align: center;
  margin-bottom: 30px;
}

.description-section p {
  font-size: 1.2rem;
  color: rgba(17, 39, 58, 0.8);
  line-height: 1.6;
  max-width: 800px;
  margin: 0 auto;
  text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.1);
}

/* Feature tag styles */
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

/* Explore button styles */
.explore-section {
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  margin-top: 20px;              /* Add spacing between feature tags and button */
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

/* Hover glow effect */
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

/* Removed no longer needed button and animation styles */

/* Responsive */
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
  
  /* Divider arrow responsive */
  .section-arrow {
    width: 55px;
    height: 55px;
    top: -35px;
  }
  
  .arrow-icon {
    font-size: 1.6rem;
  }
  
  /* Top content responsive */
  .top-content {
    padding: 35px 40px 30px;
    margin: 100px auto 60px;
    min-width: 500px;
    max-width: 700px;
    min-height: 120px;              /* Adjust minimum height */
    text-align: center;
  }
  
  /* Bottom content responsive */
  .bottom-content {
    padding: 35px 40px 30px;
    margin: 60px auto 150px;
    min-width: 500px;
    max-width: 700px;
    min-height: 180px;
  }
  
  .brand-title {
    font-size: 2rem;
  }
  
  
  .main-title {
    font-size: 2rem;
  }
  
  .description-section p {
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
  
  /* Mobile scrollbar style adjustments */
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
  
  /* Map & Recommendation line break display */
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
  
  /* Small screen divider arrows */
  .section-arrow {
    width: 50px;
    height: 50px;
    top: -25px;
  }
  
  .arrow-icon {
    font-size: 1.4rem;
  }
  
  /* Small screen top content */
  .top-content {
    padding: 25px 30px 20px;
    margin: 60px auto 40px;
    min-width: 350px;
    max-width: 600px;
    min-height: 100px;              /* Adjust minimum height */
    text-align: center;
  }
  
  /* Small screen bottom content */
  .bottom-content {
    padding: 25px 30px 20px;
    margin: 40px auto 120px;
    min-width: 350px;
    max-width: 600px;
    min-height: 150px;
  }
  
  .brand-title {
    font-size: 1.8rem;
  }
  
  .brand-section {
    margin-bottom: 80px;
  }
  
  .main-title {
    font-size: 1.8rem;
  }
  
  .description-section p {
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

  /* Mobile arrow style adjustments */
  .arrow-icon {
    width: 30px;
    height: 30px;
  }
  
  .scroll-arrow {
    margin: 20px 0;
  }

}

/* Footer Styles */
.main-footer {
  width: 100%;
  background: rgba(26, 43, 66, 1);
  color: #e0e7ef;
  text-align: center;
  padding: 18px 0 12px 0;
  font-size: 15px;
  font-weight: 400;
  letter-spacing: 0.02em;
  margin-top: 40px;
  box-shadow: 0 -2px 12px rgba(59,130,246,0.08);
  z-index: 10;
}

.footer-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}

.footer-links {
  margin-top: 4px;
  font-size: 14px;
}

.footer-links a {
  color: #a5d8ff;
  text-decoration: none;
  margin: 0 6px;
  transition: color 0.2s;
}

.footer-links a:hover {
  color: #fff;
  text-decoration: underline;
}

/* Contact info styles */
.contact-info {
  display: flex;
  flex-direction: row;
  gap: 20px;
  align-items: center;
  justify-content: center;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #fff;
  font-size: 14px;
}

.contact-icon {
  font-size: 16px;
  opacity: 0.8;
}

.contact-text {
  user-select: all;
  cursor: text;
}

/* Scroll arrow styles */
.scroll-arrow {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 40px 0;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 20;
}

/* Top scroll arrow styles */
.top-scroll-arrow {
  margin: 20px 0 40px 0;          /* Adjust margins to maintain appropriate distance from top content area */
  animation: bounce 2s infinite;  /* Add bounce animation to prompt user click */
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}


/* Visual feedback during quick jump */
.top-scroll-arrow:active {
  transform: translateY(2px) scale(0.95);
  transition: all 0.1s ease;
}

.scroll-arrow:hover {
  transform: translateY(-5px);
}

.scroll-arrow:hover .arrow-icon {
  transform: scale(1.1);
  filter: brightness(1.2);
}

.arrow-icon {
  width: 40px;
  height: 40px;
  transition: all 0.3s ease;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
}

/* Arrow animation effects */
.scroll-arrow {
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

.scroll-arrow:hover {
  animation: none;
}

</style>
