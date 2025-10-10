<template>
  <div class="faq-container">
    <!-- Background -->
    <div class="faq-background"></div>

    <!-- Top Navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <img :src="logoUrl" alt="logo" class="nav-logo" @click="goToHome" />
        <h1 class="logo" @click="goToHome">{{ $t('nav.logo') }}</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <div class="nav-item-dropdown" @mouseenter="showIslandDropdown = true" @mouseleave="showIslandDropdown = false">
            <div class="nav-item-wrapper">
              <span class="nav-item map-rec-item" @click="goToMap">
                <span class="nav-text-line">{{ $t('nav.mapRecommendation.line1') }}</span>
                <span class="nav-text-line">{{ $t('nav.mapRecommendation.line2') }}</span>
              </span>
            </div>
            <div class="dropdown-menu" v-show="showIslandDropdown">
              <div 
                v-for="island in islands" 
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
          <span class="nav-item active">{{ $t('nav.faq') }}</span>
          <LanguageSwitcher />
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="faq-content">
      <!-- Page Title -->
      <div class="page-header">
        <h1>{{ $t('faq.title') }}</h1>
        <p class="subtitle">{{ $t('faq.subtitle') }}</p>
      </div>

      <!-- FAQ Sections -->
      <div class="faq-sections">
        <!-- General Information -->
        <div class="faq-section">
          <div class="section-header">
            <div class="section-icon">ℹ️</div>
            <h2>{{ $t('faq.generalInfo.title') }}</h2>
          </div>
          <div class="faq-items">
            <div 
              v-for="(item, index) in generalInfoItems" 
              :key="'general-' + index"
              class="faq-item"
              :class="{ 'active': activeItem === 'general-' + index }"
              @click="toggleItem('general-' + index)"
            >
              <div class="faq-question">
                <span class="question-text">{{ item.question }}</span>
                <span class="toggle-icon">{{ activeItem === 'general-' + index ? '−' : '+' }}</span>
              </div>
              <transition name="slide">
                <div v-if="activeItem === 'general-' + index" class="faq-answer">
                  <p>{{ item.answer }}</p>
                </div>
              </transition>
            </div>
          </div>
        </div>

        <!-- Data and Sources -->
        <div class="faq-section">
          <div class="section-header">
            <div class="section-icon">📊</div>
            <h2>{{ $t('faq.dataAndSources.title') }}</h2>
          </div>
          <div class="faq-items">
            <div 
              v-for="(item, index) in dataSourcesItems" 
              :key="'data-' + index"
              class="faq-item"
              :class="{ 'active': activeItem === 'data-' + index }"
              @click="toggleItem('data-' + index)"
            >
              <div class="faq-question">
                <span class="question-text">{{ item.question }}</span>
                <span class="toggle-icon">{{ activeItem === 'data-' + index ? '−' : '+' }}</span>
              </div>
              <transition name="slide">
                <div v-if="activeItem === 'data-' + index" class="faq-answer">
                  <p v-html="item.answer"></p>
                </div>
              </transition>
            </div>
          </div>
        </div>

        <!-- Disclaimer and Limitations -->
        <div class="faq-section">
          <div class="section-header">
            <div class="section-icon">⚠️</div>
            <h2>{{ $t('faq.disclaimer.title') }}</h2>
          </div>
          <div class="faq-items">
            <div 
              v-for="(item, index) in disclaimerItems" 
              :key="'disclaimer-' + index"
              class="faq-item"
              :class="{ 'active': activeItem === 'disclaimer-' + index }"
              @click="toggleItem('disclaimer-' + index)"
            >
              <div class="faq-question">
                <span class="question-text">{{ item.question }}</span>
                <span class="toggle-icon">{{ activeItem === 'disclaimer-' + index ? '−' : '+' }}</span>
              </div>
              <transition name="slide">
                <div v-if="activeItem === 'disclaimer-' + index" class="faq-answer">
                  <p>{{ item.answer }}</p>
                </div>
              </transition>
            </div>
          </div>
        </div>
      </div>

    </div>

    <!-- Footer -->
    <footer class="main-footer">
      <div class="footer-content">
        {{ $t('footer.copyright') }}
        <span class="footer-links">
          <a href="mailto:coralkita.service@gmail.com">{{ $t('footer.contact') }}</a>
        </span>
      </div>
    </footer>
  </div>
</template>

<script>
import LanguageSwitcher from './LanguageSwitcher.vue'
import ossService from '@/utils/ossService.js'

export default {
  name: 'FAQ',
  components: {
    LanguageSwitcher
  },
  data() {
    return {
      logoUrl: 'http://static.coralkita.site/assets/icon.png',
      showIslandDropdown: false,
      showEducationDropdown: false,
      islands: ['Mertang', 'P Singa', 'Sipadan', 'Pulau Lima', 'Seri Buat'],
      activeItem: null,
      // 背景图片URL
      backgroundImageUrl: null
    }
  },
  computed: {
    generalInfoItems() {
      return [
        {
          question: this.$t('faq.generalInfo.q1.question'),
          answer: this.$t('faq.generalInfo.q1.answer')
        },
        {
          question: this.$t('faq.generalInfo.q2.question'),
          answer: this.$t('faq.generalInfo.q2.answer')
        }
      ]
    },
    dataSourcesItems() {
      return [
        {
          question: this.$t('faq.dataAndSources.q1.question'),
          answer: this.$t('faq.dataAndSources.q1.answer')
        },
        {
          question: this.$t('faq.dataAndSources.q2.question'),
          answer: this.$t('faq.dataAndSources.q2.answer')
        },
        {
          question: this.$t('faq.dataAndSources.q3.question'),
          answer: this.$t('faq.dataAndSources.q3.answer')
        }
      ]
    },
    disclaimerItems() {
      return [
        {
          question: this.$t('faq.disclaimer.q1.question'),
          answer: this.$t('faq.disclaimer.q1.answer')
        },
        {
          question: this.$t('faq.disclaimer.q2.question'),
          answer: this.$t('faq.disclaimer.q2.answer')
        }
      ]
    }
  },
  methods: {
    /**
     * 加载背景图片
     */
    async loadBackgroundImage() {
      try {
        this.backgroundImageUrl = await ossService.getFileUrl('ai_interface.webp')
        // 设置CSS变量
        document.documentElement.style.setProperty('--bg-image', `url(${this.backgroundImageUrl})`)
      } catch (error) {
        console.warn('加载背景图片失败，使用默认图片:', error)
        this.backgroundImageUrl = null
      }
    },
    toggleItem(itemId) {
      this.activeItem = this.activeItem === itemId ? null : itemId
    },
    goToHome() {
      window.location.href = '/'
    },
    goToMap() {
      this.$router.push('/map').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err)
        }
      })
    },
    goToEducation() {
      this.$router.push('/education').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err)
        }
      })
    },
    goToTravelChecklist() {
      this.$router.push('/travel-checklist').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err)
        }
      })
    },
    goToAITools() {
      this.$router.push('/ai-tools').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err)
        }
      })
    },
    goToActionHub() {
      this.$router.push('/action-hub').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err)
        }
      })
    },
    goToPolicies() {
      this.$router.push('/policies').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err)
        }
      })
    },
    goToIslandDetail(islandName) {
      this.showIslandDropdown = false
      this.$nextTick(() => {
        this.$router.push(`/travel/${encodeURIComponent(islandName)}`).catch(err => {
          if (err.name !== 'NavigationDuplicated') {
            console.error('Navigation error:', err)
          }
        })
      })
    }
  },
  
  mounted() {
    console.log('FAQ page loaded')
    
    // 加载背景图片
    this.loadBackgroundImage()
  }
}
</script>

<style scoped>
.faq-container {
  margin: 0;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
  background-image: var(--bg-image, url("http://static.coralkita.site/ai_interface.webp"));
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-position: center;
  background-size: cover;
  overflow-x: hidden;
  padding: 0;
}

.faq-background {
  display: none; /* Hide the old background */
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

.nav-logo {
  height: 46px;
  cursor: pointer;
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

.nav-items {
  display: flex;
  gap: 32px;
  align-items: center;
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
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  min-width: 160px;
  z-index: 1000;
  overflow: hidden;
  animation: dropdownSlideIn 0.3s ease;
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
.faq-content {
  flex: 1;
  max-width: 900px;
  width: 100%;
  margin: 0 auto;
  padding: 120px 20px 40px;
}

.page-header {
  text-align: center;
  margin-bottom: 50px;
}

.page-header h1 {
  font-size: 3rem;
  color: white;
  margin-bottom: 15px;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.8);
  font-weight: 700;
}

.subtitle {
  font-size: 1.3rem;
  color: rgba(255, 255, 255, 0.95);
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.6);
  margin-bottom: 25px;
}

/* FAQ Sections */
.faq-sections {
  display: flex;
  flex-direction: column;
  gap: 40px;
  margin-bottom: 50px;
}

.faq-section {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 2px solid rgba(255, 255, 255, 0.2);
}

.section-icon {
  font-size: 2rem;
}

.section-header h2 {
  color: #0f4c75;
  font-size: 1.8rem;
  margin: 0;
  font-weight: 600;
}

.faq-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.faq-item {
  background: rgba(15, 76, 117, 0.05);
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid rgba(15, 76, 117, 0.1);
}

.faq-item:hover {
  background: rgba(15, 76, 117, 0.1);
  border-color: rgba(15, 76, 117, 0.3);
  transform: translateY(-2px);
}

.faq-item.active {
  background: rgba(15, 76, 117, 0.15);
  border-color: rgba(15, 76, 117, 0.4);
}

.faq-question {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  cursor: pointer;
  user-select: none;
}

.question-text {
  color: #0f4c75;
  font-size: 1.1rem;
  font-weight: 600;
  flex: 1;
}

.toggle-icon {
  color: #0f4c75;
  font-size: 1.5rem;
  font-weight: 300;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(15, 76, 117, 0.1);
  border-radius: 50%;
  transition: all 0.3s ease;
}

.faq-item:hover .toggle-icon {
  background: rgba(15, 76, 117, 0.2);
  transform: scale(1.1);
}

.faq-answer {
  padding: 0 20px 20px 20px;
  color: #666;
  line-height: 1.8;
}

.faq-answer p {
  margin: 0;
  font-size: 1rem;
}

/* Slide transition */
.slide-enter-active, .slide-leave-active {
  transition: all 0.3s ease;
  max-height: 500px;
}

.slide-enter, .slide-leave-to {
  max-height: 0;
  opacity: 0;
}


/* Footer */
.main-footer {
  width: 100%;
  background: rgba(26, 43, 66, 1);
  color: #e0e7ef;
  text-align: center;
  padding: 18px 0;
  font-size: 15px;
  margin-top: auto;
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

/* Responsive Design */
@media (max-width: 768px) {
  .page-header h1 {
    font-size: 2rem;
  }

  .subtitle {
    font-size: 1rem;
  }

  .faq-section {
    padding: 20px;
  }

  .section-header h2 {
    font-size: 1.4rem;
  }

  .question-text {
    font-size: 1rem;
  }

  .contact-section {
    padding: 30px 20px;
  }

  .nav-items {
    gap: 16px;
  }

  .nav-item {
    font-size: 0.9rem;
    padding: 6px 12px;
  }
}
</style>

