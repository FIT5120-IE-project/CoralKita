<template>
  <div class="government-container">
    <!-- 背景图片加载占位符 -->
    <div class="bg-placeholder" v-if="!backgroundLoaded">
      <div class="progress-container">
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: loadingProgress + '%' }"></div>
        </div>
        <p class="loading-text">{{ loadingText }}</p>
      </div>
    </div>
    
    <!-- Top Navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <img :src="appIconUrl" alt="logo" class="nav-logo" @click="goToHome" />
        <h1 class="logo" @click="goToHome">CoralKita</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <div class="nav-item-dropdown" @mouseenter="showTravelDropdown = true" @mouseleave="showTravelDropdown = false">
            <div class="nav-item-wrapper">
              <span class="nav-item map-rec-item" @click="goToMap">
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
          <span class="nav-item active" @click="goToGovernment">{{ $t('nav.government') }}</span>
          <span class="nav-item" @click="goToFAQ">{{ $t('nav.faq') }}</span>
          <LanguageSwitcher />
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      
      <!-- Page Header -->
      <div class="page-header">
        <h1>🏛️ {{ $t('government.title') }}</h1>
        <p>{{ $t('government.subtitle') }}</p>
      </div>

      <!-- Government & Policy Updates Section -->
      <section class="policy-updates-section">
        <div class="section-header">
          <h2>📰 {{ $t('government.policyUpdates.title') }}</h2>
          <p>{{ $t('government.policyUpdates.subtitle') }}</p>
        </div>

        <div class="updates-timeline">
          <!-- Update 1 -->
          <div class="timeline-card">
            <div class="timeline-date">{{ $t('government.policyUpdates.updates.sunscreen.date') }}</div>
            <h4>{{ $t('government.policyUpdates.updates.sunscreen.title') }}</h4>
            <p>{{ $t('government.policyUpdates.updates.sunscreen.description') }}</p>
            <a href="https://marinepark.dof.gov.my/en/" target="_blank" class="source-link">
              <span class="source-icon">🔗</span>
              {{ $t('government.policyUpdates.updates.sunscreen.source') }}
            </a>
          </div>

          <!-- Update 2 -->
          <div class="timeline-card">
            <div class="timeline-date">{{ $t('government.policyUpdates.updates.fishing.date') }}</div>
            <h4>{{ $t('government.policyUpdates.updates.fishing.title') }}</h4>
            <p>{{ $t('government.policyUpdates.updates.fishing.description') }}</p>
            <a href="https://marinepark.dof.gov.my/en/" target="_blank" class="source-link">
              <span class="source-icon">🔗</span>
              {{ $t('government.policyUpdates.updates.fishing.source') }}
            </a>
          </div>

          <!-- Update 3 -->
          <div class="timeline-card">
            <div class="timeline-date">{{ $t('government.policyUpdates.updates.monitoring.date') }}</div>
            <h4>{{ $t('government.policyUpdates.updates.monitoring.title') }}</h4>
            <p>{{ $t('government.policyUpdates.updates.monitoring.description') }}</p>
            <a href="https://www.reefcheck.org/results-of-the-2024-reef-check-malaysia-surveys-challenges-and-opportunities" target="_blank" class="source-link">
              <span class="source-icon">🔗</span>
              {{ $t('government.policyUpdates.updates.monitoring.source') }}
            </a>
          </div>

          <!-- Update 4 -->
          <div class="timeline-card">
            <div class="timeline-date">{{ $t('government.policyUpdates.updates.restoration.date') }}</div>
            <h4>{{ $t('government.policyUpdates.updates.restoration.title') }}</h4>
            <p>{{ $t('government.policyUpdates.updates.restoration.description') }}</p>
            <a href="https://www.sabahparks.org.my/resource-centre/news/volunteers-building-over-1000-coral-reef-frames-in-kudat" target="_blank" class="source-link">
              <span class="source-icon">🔗</span>
              {{ $t('government.policyUpdates.updates.restoration.source') }}
            </a>
          </div>
        </div>
      </section>

      <!-- Support Reef Conservation Section -->
      <section class="conservation-section">
        <div class="section-header">
          <h2>💙 {{ $t('government.conservation.title') }}</h2>
          <p>{{ $t('government.conservation.subtitle') }}</p>
        </div>

        <div class="donation-grid">
          <!-- Reef Check Malaysia -->
          <div class="donation-card">
            <div class="card-header">
              <h3>{{ $t('government.conservation.ngos.reefCheck.name') }}</h3>
              <span class="badge verified">{{ $t('government.conservation.ngos.reefCheck.verified') }}</span>
            </div>
            <p class="card-description">
              {{ $t('government.conservation.ngos.reefCheck.description') }}
            </p>
            <div class="ngo-info">
              <p><strong>{{ $t('government.conservation.ngos.reefCheck.focusAreas') }}</strong> {{ $t('government.conservation.ngos.reefCheck.focusAreasText') }}</p>
              <p><strong>{{ $t('government.conservation.ngos.reefCheck.coverage') }}</strong> {{ $t('government.conservation.ngos.reefCheck.coverageText') }}</p>
            </div>
            <a href="https://reefcheck.org.my/donate/" target="_blank" class="donate-btn">
              {{ $t('government.conservation.ngos.reefCheck.button') }}
            </a>
          </div>

          <!-- WWF Malaysia -->
          <div class="donation-card">
            <div class="card-header">
              <h3>{{ $t('government.conservation.ngos.wwf.name') }}</h3>
              <span class="badge verified">{{ $t('government.conservation.ngos.wwf.verified') }}</span>
            </div>
            <p class="card-description">
              {{ $t('government.conservation.ngos.wwf.description') }}
            </p>
            <div class="ngo-info">
              <p><strong>{{ $t('government.conservation.ngos.wwf.focusAreas') }}</strong> {{ $t('government.conservation.ngos.wwf.focusAreasText') }}</p>
              <p><strong>{{ $t('government.conservation.ngos.wwf.coverage') }}</strong> {{ $t('government.conservation.ngos.wwf.coverageText') }}</p>
            </div>
            <a href="https://www.wwf.org.my/how_you_can_help/donate_now/conserve_our_coral_reefs/" target="_blank" class="donate-btn">
              {{ $t('government.conservation.ngos.wwf.button') }}
            </a>
          </div>

          <!-- TRACC -->
          <div class="donation-card">
            <div class="card-header">
              <h3>{{ $t('government.conservation.ngos.tracc.name') }}</h3>
              <span class="badge verified">{{ $t('government.conservation.ngos.tracc.verified') }}</span>
            </div>
            <p class="card-description">
              {{ $t('government.conservation.ngos.tracc.description') }}
            </p>
            <div class="ngo-info">
              <p><strong>{{ $t('government.conservation.ngos.tracc.focusAreas') }}</strong> {{ $t('government.conservation.ngos.tracc.focusAreasText') }}</p>
              <p><strong>{{ $t('government.conservation.ngos.tracc.coverage') }}</strong> {{ $t('government.conservation.ngos.tracc.coverageText') }}</p>
            </div>
            <a href="https://tracc.org/donate" target="_blank" class="donate-btn">
              {{ $t('government.conservation.ngos.tracc.button') }}
            </a>
          </div>
        </div>
      </section>

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
import ossService from '@/utils/ossService.js'
import LanguageSwitcher from './LanguageSwitcher.vue'

export default {
  name: 'Government',
  components: {
    LanguageSwitcher
  },
  data() {
    return {
      backgroundLoaded: false,
      loadingProgress: 0,
      loadingText: 'Loading government data...',
      appIconUrl: null,
      backgroundImageUrl: null,
      showTravelDropdown: false,
      showEducationDropdown: false,
      currentLanguage: 'en',
      travelIslands: ['Mertang', 'P Singa', 'Sipadan', 'Pulau Lima', 'Seri Buat']
    }
  },
  mounted() {
    this.preloadBackgroundImage();
    this.loadAppIcon();
    this.loadBackgroundImage();
    localStorage.setItem('lastPageRefresh', Date.now().toString());
  },
  methods: {
    async loadAppIcon() {
      try {
        this.appIconUrl = await ossService.getFileUrl('assets/icon.png')
      } catch (error) {
        console.warn('加载应用图标失败，使用默认图标:', error)
        this.appIconUrl = null
      }
    },

    async loadBackgroundImage() {
      try {
        this.backgroundImageUrl = await ossService.getFileUrl('assets/bg_mainpage.webp')
        document.documentElement.style.setProperty('--gov-bg-image', `url('${this.backgroundImageUrl}')`)
      } catch (error) {
        console.warn('加载背景图片失败，使用默认图片:', error)
        this.backgroundImageUrl = null
      }
    },

    preloadBackgroundImage() {
      const img = new Image()
      img.onload = () => {
        this.backgroundLoaded = true
        this.loadingProgress = 100
      }
      img.onerror = () => {
        this.backgroundLoaded = true
        this.loadingProgress = 100
      }
      
      ossService.getFileUrl('assets/bg_mainpage.webp').then(url => {
        img.src = url
      }).catch(() => {
        this.backgroundLoaded = true
        this.loadingProgress = 100
      })

      const progressInterval = setInterval(() => {
        if (this.loadingProgress < 90) {
          this.loadingProgress += Math.random() * 15
        } else {
          clearInterval(progressInterval)
        }
      }, 100)
    },

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
      localStorage.setItem('functionalNavigation', 'true')
      this.$router.push('/education').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToTravelChecklist() {
      localStorage.setItem('functionalNavigation', 'true')
      this.$router.push('/travel-checklist').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToAITools() {
      this.$router.push('/ai-tools').catch(err => {
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

    goToIslandDetail(islandName) {
      localStorage.setItem('functionalNavigation', 'true')
      this.$router.push(`/island/${islandName}`).catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    toggleLanguage() {
      this.currentLanguage = this.currentLanguage === 'en' ? 'zh' : 'en'
    }
  }
}
</script>

<style scoped>
.government-container {
  min-height: 100vh;
  position: relative;
  overflow-x: hidden;
  background: linear-gradient(135deg, rgba(10, 77, 104, 0.7) 0%, rgba(5, 191, 219, 0.7) 100%);
}

.bg-placeholder {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  color: white;
  font-size: 18px;
  overflow: hidden;
  background: linear-gradient(135deg, #0f4c75 0%, #3282b8 25%, #0f4c75 50%, #1e3a8a 75%, #0f4c75 100%);
  background-size: 400% 400%;
  animation: oceanWave 8s ease-in-out infinite;
}

@keyframes oceanWave {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

.progress-container {
  width: 300px;
  text-align: center;
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
}

.loading-text {
  color: white;
  font-size: 16px;
  font-weight: 500;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
  margin: 0;
}

.government-container::before {
  content: "";
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('@/assets/bg_mainpage.webp') no-repeat center center;
  background-size: cover;
  z-index: -1;
  will-change: transform;
  transform: translateZ(0);
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
}

.nav-logo {
  height: 46px;
  cursor: pointer;
}

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
}

.nav-item-dropdown::after {
  content: '';
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  height: 8px;
  background: transparent;
  z-index: 999;
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
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  min-width: 160px;
  z-index: 1000;
  overflow: hidden;
  animation: dropdownSlideIn 0.3s ease;
}

@keyframes dropdownSlideIn {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(-8px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
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

/* Main Content */
.main-content {
  position: relative;
  z-index: 2;
  max-width: 1400px;
  margin: 0 auto;
  padding: 100px 20px 80px;
}

/* Page Header */
.page-header {
  text-align: center;
  margin-bottom: 60px;
  padding: 40px 20px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 25px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.page-header h1 {
  font-size: 3rem;
  font-weight: 700;
  background: linear-gradient(135deg, #01A2EB, #1A1D25);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 15px;
}

.page-header p {
  font-size: 1.2rem;
  color: #555;
  max-width: 800px;
  margin: 0 auto;
}

/* Section Headers */
.section-header {
  text-align: center;
  margin-bottom: 40px;
}

.section-header h2 {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1A1D25;
  margin-bottom: 10px;
}

.section-header p {
  font-size: 1.1rem;
  color: #666;
}

/* Policy Updates Section - Timeline Style */
.policy-updates-section {
  margin-bottom: 80px;
}

.updates-timeline {
  position: relative;
  padding-left: 40px;
}

.updates-timeline::before {
  content: '';
  position: absolute;
  left: 15px;
  top: 0;
  bottom: 0;
  width: 3px;
  background: linear-gradient(180deg, #01A2EB, #1A1D25);
}

.timeline-card {
  position: relative;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 30px;
  margin-bottom: 30px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  border-left: 4px solid #01A2EB;
  transition: all 0.3s ease;
}

.timeline-card::before {
  content: '';
  position: absolute;
  left: -43px;
  top: 30px;
  width: 15px;
  height: 15px;
  background: #01A2EB;
  border-radius: 50%;
  border: 3px solid white;
  box-shadow: 0 0 0 3px #01A2EB;
}

.timeline-card:hover {
  transform: translateX(10px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
}

.timeline-date {
  display: inline-block;
  background: #01A2EB;
  color: white;
  padding: 6px 15px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 600;
  margin-bottom: 15px;
}

.timeline-card h4 {
  font-size: 1.4rem;
  font-weight: 600;
  color: #1A1D25;
  margin-bottom: 12px;
  line-height: 1.4;
}

.timeline-card p {
  color: #555;
  line-height: 1.7;
  margin-bottom: 15px;
}

.source-link {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #01A2EB;
  text-decoration: none;
  font-weight: 600;
  padding: 8px 0;
  transition: all 0.3s ease;
}

.source-link:hover {
  color: #0077BE;
  gap: 12px;
}

.source-icon {
  font-size: 1.2rem;
}

/* Conservation Section */
.conservation-section {
  margin-bottom: 80px;
}

.donation-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 30px;
  margin-top: 40px;
}

.donation-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  border: 2px solid rgba(1, 162, 235, 0.2);
  transition: all 0.3s ease;
}

.donation-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
  border-color: #01A2EB;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
  gap: 10px;
}

.card-header h3 {
  font-size: 1.4rem;
  font-weight: 600;
  color: #1A1D25;
  flex: 1;
}

.badge {
  padding: 5px 12px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
  white-space: nowrap;
}

.badge.verified {
  background: #28a745;
  color: white;
}

.card-description {
  color: #555;
  line-height: 1.7;
  margin-bottom: 15px;
}

.ngo-info {
  background: rgba(1, 162, 235, 0.05);
  padding: 15px;
  border-radius: 10px;
  margin-bottom: 20px;
}

.ngo-info p {
  color: #555;
  line-height: 1.6;
  margin-bottom: 8px;
}

.ngo-info p:last-child {
  margin-bottom: 0;
}

.donate-btn {
  display: inline-block;
  background: linear-gradient(135deg, #01A2EB, #0077BE);
  color: white;
  padding: 12px 25px;
  border-radius: 25px;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(1, 162, 235, 0.3);
}

.donate-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(1, 162, 235, 0.4);
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
  box-shadow: 0 -2px 12px rgba(59, 130, 246, 0.08);
  z-index: 10;
  position: relative;
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
  color: #01A2EB;
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-links a:hover {
  color: #00D4FF;
}

/* Responsive Design */
@media (max-width: 1024px) {
  .donation-grid {
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  }

  .updates-timeline {
    padding-left: 30px;
  }

  .timeline-card::before {
    left: -35px;
  }
}

@media (max-width: 768px) {
  .page-header h1 {
    font-size: 2rem;
  }

  .section-header h2 {
    font-size: 2rem;
  }

  .donation-grid {
    grid-template-columns: 1fr;
  }

  .updates-timeline {
    padding-left: 20px;
  }

  .updates-timeline::before {
    left: 7px;
  }

  .timeline-card {
    margin-left: 10px;
  }

  .timeline-card::before {
    left: -28px;
  }

  .nav-items {
    gap: 15px;
  }

  .top-nav {
    padding: 12px 20px;
  }
}
</style>

