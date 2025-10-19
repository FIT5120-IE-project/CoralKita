<template>
  <div class="ai-tools-selector-container">
    <!-- Top Navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <img :src="appIconUrl" alt="logo" class="nav-logo" @click="goToHome" />
        <h1 class="logo" @click="goToHome">{{ $t('nav.logo') }}</h1>
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
          <span class="nav-item active">{{ $t('nav.aiClassification') }}</span>
          <span class="nav-item" @click="goToGovernment">{{ $t('nav.government') }}</span>
          <span class="nav-item" @click="goToFAQ">{{ $t('nav.faq') }}</span>
          <LanguageSwitcher />
        </div>
      </div>
    </div>

    <!-- Header -->
    <div class="ai-tools-header">
      <h1>🤖 {{ $t('aiTools.hub.title') }}</h1>
      <p>{{ $t('aiTools.hub.subtitle') }}</p>
    </div>

    <!-- Main Content - 4 Tool Cards -->
    <div class="ai-tools-content">
      <div class="tools-grid">
        <!-- AI Image Classification -->
        <div class="tool-card" @click="goToImageClassification">
          <div class="tool-icon">📸</div>
          <h3>{{ $t('aiTools.tools.imageClassification.title') }}</h3>
          <p>{{ $t('aiTools.tools.imageClassification.description') }}</p>
          <div class="tool-features">
            <div class="feature-item">✓ {{ $t('aiTools.tools.imageClassification.features.intelligent') }}</div>
            <div class="feature-item">✓ {{ $t('aiTools.tools.imageClassification.features.fast') }}</div>
            <div class="feature-item">✓ {{ $t('aiTools.tools.imageClassification.features.formats') }}</div>
          </div>
          <button class="tool-button">
            <span>{{ $t('aiTools.common.getStarted') }}</span>
          </button>
        </div>

        <!-- Comprehensive AI Assistant -->
        <div class="tool-card" @click="goToComprehensiveAssistant">
          <div class="tool-icon">🤖</div>
          <h3>{{ $t('aiTools.tools.comprehensiveAssistant.title') }}</h3>
          <p>{{ $t('aiTools.tools.comprehensiveAssistant.description') }}</p>
          <div class="tool-features">
            <div class="feature-item">✓ {{ $t('aiTools.tools.comprehensiveAssistant.features.knowledge') }}</div>
            <div class="feature-item">✓ {{ $t('aiTools.tools.comprehensiveAssistant.features.dataAnalysis') }}</div>
            <div class="feature-item">✓ {{ $t('aiTools.tools.comprehensiveAssistant.features.travelPlanning') }}</div>
          </div>
          <button class="tool-button">
            <span>{{ $t('aiTools.common.getStarted') }}</span>
          </button>
        </div>
      </div>
    </div>

    <!-- Footer -->
    <footer class="main-footer">
      <div class="footer-content">
        {{ $t('footer.copyright') }}
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
  </div>
</template>

<script>
import ossService from '@/utils/ossService.js'
import LanguageSwitcher from './LanguageSwitcher.vue'

export default {
  name: 'AIToolsSelector',
  components: {
    LanguageSwitcher
  },
  data() {
    return {
      showTravelDropdown: false,
      showEducationDropdown: false,
      travelIslands: ['Mertang', 'P Singa', 'Sipadan', 'Pulau Lima', 'Seri Buat'],
      appIconUrl: null,
      backgroundImageUrl: null
    }
  },
  methods: {
    goToHome() {
      this.$router.push('/').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToMap() {
      this.$router.push('/map').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToEducation() {
      this.$router.push('/education').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToTravelChecklist() {
      this.$router.push('/travel-checklist').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToGovernment() {
      this.$router.push('/government').catch(err => {
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
    
    goToIslandDetail(island) {
      this.$router.push(`/travel/${island}`).catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    // Navigate to AI Image Classification page (Frontend page)
    goToImageClassification() {
      this.$router.push('/ai-classification').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    // Navigate to Comprehensive AI Assistant (Streamlit page)
    goToComprehensiveAssistant() {
      // Open Comprehensive AI Assistant in new tab
      window.open('http://8.148.202.1:8505', '_blank');
    },

    /**
     * Load app icon
     */
    async loadAppIcon() {
      try {
        this.appIconUrl = await ossService.getAppIconUrl()
      } catch (error) {
        console.warn('Failed to load app icon, using default icon:', error)
        this.appIconUrl = null
      }
    },

    /**
     * Load background image
     */
    async loadBackgroundImage() {
      try {
        // Prefer WebP format, fallback to JPG
        const webpUrl = await ossService.getFileUrl('ai_interface.webp')
        this.backgroundImageUrl = webpUrl
        document.documentElement.style.setProperty('--ai-bg-image', `url('${webpUrl}')`)
        console.log('AI background image loaded (WebP):', webpUrl)
      } catch (webpError) {
        console.warn('WebP loading failed, trying JPG:', webpError)
        try {
          const jpgUrl = await ossService.getAIToolsBackgroundUrl()
          this.backgroundImageUrl = jpgUrl
          document.documentElement.style.setProperty('--ai-bg-image', `url('${jpgUrl}')`)
          console.log('AI background image loaded (JPG):', jpgUrl)
        } catch (jpgError) {
          console.error('All background loading methods failed:', jpgError)
        }
      }
    }
  },
  
  mounted() {
    console.log('AI Tools Selector page loaded')
    this.loadAppIcon()
    this.loadBackgroundImage()
  }
}
</script>

<style>
.ai-tools-selector-container {
  margin: 0;
  min-height: 100vh;
  position: relative;
  overflow-x: hidden;
  padding: 0;
  background-image: var(--ai-bg-image, url('../assets/ai_interface.jpg'));
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
  background-attachment: fixed;
}

/* Use pseudo-element to carry background image */
.ai-tools-selector-container::after {
  content: "" !important;                        /* Must add */
  position: fixed !important;                    /* Fixed on screen, not moving with scroll */
  top: 0 !important;
  left: 0 !important;
  width: 100vw !important;
  height: 100vh !important;

  background-image: var(--ai-bg-image, url('../assets/ai_interface.jpg')) !important;
  background-repeat: no-repeat !important;
  background-position: center !important;        /* Center display */
  background-size: cover !important;              /* Cover entire container, maintain aspect ratio */
  
  z-index: -1 !important;                        /* Place behind content */
  pointer-events: none !important;               /* Don't affect click operations */
}

/* Navigation bar styles */
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
  gap: 12px;
}

.nav-logo {
  height: 40px;
  width: auto;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.nav-logo:hover {
  transform: scale(1.05);
}

.logo {
  font-size: 1.5rem;
  font-weight: 700;
  color: white;
  cursor: pointer;
  transition: color 0.3s ease;
}

.logo:hover {
  color: #bbe1fa;
}

.nav-right {
  display: flex;
  align-items: center;
}

.nav-items {
  display: flex;
  gap: 32px;
  align-items: center;
  flex-wrap: wrap;
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

.nav-item-dropdown {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
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
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-item:hover {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.08), rgba(16, 185, 129, 0.08));
  color: #1e40af;
  transform: translateX(2px);
}

/* Header styles */
.ai-tools-header {
  position: relative;
  z-index: 2;
  text-align: center;
  color: white;
  padding: 120px 20px 40px;
  margin-bottom: 40px;
}

.ai-tools-header h1 {
  font-size: 3rem;
  margin-bottom: 15px;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  font-weight: 700;
}

.ai-tools-header p {
  font-size: 1.3rem;
  opacity: 0.95;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

/* Tool cards area */
.ai-tools-content {
  padding: 40px 20px;
  max-width: 1600px;
  margin: 0 auto;
}

.tools-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 40px;
  padding: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.tool-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 25px;
  padding: 45px 35px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15), 0 4px 20px rgba(14, 165, 233, 0.1);
  backdrop-filter: blur(10px);
  border: 2px solid rgba(14, 165, 233, 0.3);
  cursor: pointer;
  transition: all 0.4s ease;
  text-align: center;
  position: relative;
  overflow: hidden;
  min-height: 400px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.tool-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: linear-gradient(90deg, #0ea5e9, #38bdf8, #0ea5e9);
  background-size: 200% 100%;
  animation: shimmer 3s linear infinite;
  opacity: 1;
  transition: opacity 0.3s ease;
}

@keyframes shimmer {
  0% { background-position: -200% 0; }
  100% { background-position: 200% 0; }
}

.tool-card:hover::before {
  opacity: 1;
  height: 8px;
}

.tool-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 60px rgba(14, 165, 233, 0.3);
  border-color: rgba(14, 165, 233, 0.5);
}

.tool-icon {
  font-size: 5rem;
  margin-bottom: 20px;
  animation: float 3s ease-in-out infinite;
  filter: drop-shadow(0 4px 8px rgba(14, 165, 233, 0.3));
  transition: all 0.3s ease;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.tool-card h3 {
  font-size: 1.8rem;
  color: #0f4c75;
  margin-bottom: 15px;
  font-weight: 700;
}

.tool-card p {
  font-size: 1.1rem;
  color: #666;
  line-height: 1.6;
  margin-bottom: 25px;
  min-height: 50px;
  flex-grow: 1;
}

.tool-features {
  margin: 25px 0;
  padding: 20px;
  background: linear-gradient(135deg, rgba(14, 165, 233, 0.05), rgba(56, 189, 248, 0.05));
  border-radius: 15px;
  border: 1px solid rgba(14, 165, 233, 0.1);
}

.feature-item {
  color: #0284c7;
  font-size: 0.95rem;
  padding: 8px 0;
  font-weight: 500;
  text-align: left;
}

.tool-button {
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  color: white;
  border: none;
  padding: 15px 30px;
  border-radius: 25px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tool-button:hover {
  background: linear-gradient(135deg, #0284c7, #0369a1);
  transform: scale(1.02);
  box-shadow: 0 8px 25px rgba(14, 165, 233, 0.4);
}


.tool-card:hover .tool-icon {
  filter: drop-shadow(0 6px 12px rgba(14, 165, 233, 0.5));
  transform: scale(1.1);
}

/* Footer styles */
.main-footer {
  width: 100%;
  background: rgba(26, 43, 66, 1);
  color: #e0e7ef;
  text-align: center;
  padding: 18px 0 12px 0;
  font-size: 15px;
  font-weight: 400;
  letter-spacing: 0.02em;
  margin-top: 60px;
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

/* Responsive design */
@media (max-width: 768px) {
  .ai-tools-header h1 {
    font-size: 2.2rem;
  }
  
  .ai-tools-header p {
    font-size: 1.1rem;
  }
  
  .tools-grid {
    grid-template-columns: 1fr;
    gap: 30px;
    padding: 15px;
  }
  
  .tool-card {
    padding: 30px 20px;
  }
  
  .tool-icon {
    font-size: 4rem;
  }
}

@media (max-width: 480px) {
  .ai-tools-header h1 {
    font-size: 1.8rem;
  }
  
  .tool-icon {
    font-size: 3.5rem;
  }
  
  .tool-card h3 {
    font-size: 1.5rem;
  }
  
  .tool-card p {
    font-size: 1rem;
  }
}
</style>

