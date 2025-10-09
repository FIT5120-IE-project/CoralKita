<template>
  <div class="education-container">
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
        <!-- Left side logo -->
        <img :src="imageUrls.appIcon" alt="logo" class="nav-logo" @click="goToHome" />
        <h1 class="logo" @click="goToHome">{{ $t('nav.logo') }}</h1>
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
            <span class="nav-item active">{{ $t('nav.education') }}</span>
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

    <!-- Header -->
    <div class="education-header">
      <h1>{{ $t('education.title') }}</h1>
      <p>{{ $t('education.subtitle') }}</p>
      <div class="header-buttons">
        <button class="verification-game-btn" @click="startVerificationGame">
          {{ $t('education.gameButton') }}
        </button>
        <button class="tips-guides-btn" @click="goToTravelChecklist">
          {{ $t('education.tipsGuidesButton') }}
        </button>
      </div>
    </div>

    <!-- Main Content Area -->
    <div class="education-content">
      <!-- Video Learning Section -->
      <div class="video-learning-section">
        <h2>{{ $t('education.sections.videos') }}</h2>
        <div class="video-grid-2x2">
          <div 
            v-for="(video, index) in localVideos" 
            :key="index" 
            class="video-item"
            @click="playVideo(video)"
          >
            <div class="video-thumbnail">
              <img :src="video.thumbnail" :alt="$t(video.titleKey)" />
              <div class="play-overlay">
                <div class="play-button">
                  <span class="play-icon">▶</span>
          </div>
              </div>
          </div>
            <div class="video-title">{{ $t(video.titleKey) }}</div>
        </div>
            </div>
      </div>

      <!-- Coral Reefs Numbers Section -->
      <div class="coral-numbers-section">
        <h2 class="numbers-title" id="reef-title">{{ $t('education.sections.numbers') }}</h2>

        <div class="numbers-row">
          <article class="numbers-card">
            <div class="numbers-stat"><span class="numbers-count" data-to="3">0</span></div>
            <div class="numbers-headline">{{ $t('education.numbers.mainTypes.title') }}</div>
            <p class="numbers-desc">{{ $t('education.numbers.mainTypes.description') }}</p>
            <a class="numbers-cta" href="https://www.youtube.com/watch?v=mPA9Ze16lGw" target="_blank">{{ $t('education.numbers.mainTypes.learnMore') }}</a>
          </article>
          <article class="numbers-card">
            <div class="numbers-stat"><span class="numbers-count" data-to="3.4" data-decimals="1">0</span><span class="numbers-suffix">billion</span></div>
            <div class="numbers-headline">{{ $t('education.numbers.tourismValue.title') }}</div>
            <p class="numbers-desc">{{ $t('education.numbers.tourismValue.description') }}</p>
            <a class="numbers-cta" href="https://www.youtube.com/watch?v=3JfiN6s85p0" target="_blank">{{ $t('education.numbers.tourismValue.learnMore') }}</a>
          </article>
        </div>

        <div class="numbers-row">
          <article class="numbers-card">
            <div class="numbers-stat"><span class="numbers-fraction"><sup>1</sup>/<sub>4</sub></span></div>
            <div class="numbers-headline">{{ $t('education.numbers.marineSpecies.title') }}</div>
            <p class="numbers-desc">{{ $t('education.numbers.marineSpecies.description') }}</p>
            <a class="numbers-cta" href="https://www.youtube.com/watch?v=bHO-z-1xJDY" target="_blank">{{ $t('education.numbers.marineSpecies.learnMore') }}</a>
          </article>
          <article class="numbers-card">
            <div class="numbers-stat"><span class="numbers-count" data-to="4600">0</span></div>
            <div class="numbers-headline">{{ $t('education.numbers.bombFishing.title') }}</div>
            <p class="numbers-desc">{{ $t('education.numbers.bombFishing.description') }}</p>
            <a class="numbers-cta" href="https://www.youtube.com/watch?v=GnB8OoT2UAE" target="_blank">{{ $t('education.numbers.bombFishing.learnMore') }}</a>
          </article>
        </div>


        <div class="numbers-row">
          <article class="numbers-card">
            <div class="numbers-stat"><span class="numbers-count" data-to="4000">0</span></div>
            <div class="numbers-headline">{{ $t('education.numbers.fishSpecies.title') }}</div>
            <p class="numbers-desc">{{ $t('education.numbers.fishSpecies.description') }}</p>
            <a class="numbers-cta" href="https://www.youtube.com/watch?v=14ot4DrXdds&t=383s" target="_blank">{{ $t('education.numbers.fishSpecies.learnMore') }}</a>
          </article>
          <article class="numbers-card">
            <div class="numbers-stat"><span class="numbers-count" data-to="30000">0</span><span class="numbers-suffix">+</span></div>
            <div class="numbers-headline">{{ $t('education.numbers.coralsOutplanted.title') }}</div>
            <p class="numbers-desc">{{ $t('education.numbers.coralsOutplanted.description') }}</p>
            <a class="numbers-cta" href="https://www.youtube.com/watch?v=pgwcPlX5Kzo&t=104s" target="_blank">{{ $t('education.numbers.coralsOutplanted.learnMore') }}</a>
          </article>
        </div>
      </div>
    </div>

    <!-- Verification Interface -->
    <div v-if="showVerification" class="verification-overlay" 
         :class="{ 'clickable-overlay': verificationCompleted && verificationResult }"
         @click="handleOverlayClick">
        <div class="verification-modal" @click.stop>
          <div class="verification-header">
            <button class="btn-close-verification" @click="closeVerification">
              ×
            </button>
            <h2>{{ $t('education.verification.title') }}</h2>
            <p>{{ $t('education.verification.description') }}</p>
          </div>
    
        <div class="verification-content">
          <div v-if="loadingVerification" class="loading-verification">
            <div class="loading-spinner"></div>
            <p>Loading verification images...</p>
        </div>
          
          <div v-else-if="!verificationCompleted" class="images-grid">
            <!-- Selection Progress -->
            <div class="selection-progress">
              <h4>Please select 4 healthy coral reef images</h4>
              <p>Selected: {{ selectedImages.length }} / {{ maxSelections }}</p>
              <p>Remaining selections: {{ maxSelections - selectedImages.length }}</p>
            </div>
            
            <div 
              v-for="image in verificationImages" 
              :key="image.id"
              class="image-item"
              :class="{ 
                'selected-correct': image.isSelected && image.isCorrect === true,
                'selected-incorrect': image.isSelected && image.isCorrect === false,
                'disabled': verificationCompleted
              }"
              @click="selectHealthyImage(image.id)"
            >
              <div class="image-container">
                <img 
                  :src="image.imageUrl" 
                  :alt="'Coral Reef Image ' + image.id"
                  class="verification-image"
                  @error="handleImageError"
                />
                <div class="image-number">{{ image.id }}</div>
                
                <!-- Selection Result -->
                <div v-if="image.isSelected" class="selection-indicator">
                  <span v-if="image.isCorrect === true" class="correct-indicator">✓ Correct</span>
                  <span v-else-if="image.isCorrect === false" class="incorrect-indicator">✗ Incorrect</span>
                </div>
            </div>
            
              <!-- Click Hint -->
              <div class="click-hint">
                <span v-if="!image.isSelected">Click to select</span>
                <span v-else-if="image.isCorrect === true">Correct selection</span>
                <span v-else-if="image.isCorrect === false">Incorrect selection</span>
              </div>
            </div>
          </div>
          
                    <!-- Verification Result -->
          <div v-if="verificationCompleted && verificationResult" class="verification-result">
            <div class="result-icon" :class="verificationResult.success ? 'success' : 'failure'">
              {{ verificationResult.success ? '🎉' : '😔' }}
            </div>
            <h3>{{ verificationResult.success ? 'Verification Successful!' : 'Verification Failed' }}</h3>
            <p>{{ verificationResult.message }}</p>
            <div class="click-to-close-hint">Click anywhere to continue</div>
            </div>
            </div>
            

            </div>
            </div>

    <!-- Error Message -->
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
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
import { mapGetters } from 'vuex'
import axios from 'axios'
import ossService from '@/utils/ossService.js'

import LanguageSwitcher from './LanguageSwitcher.vue'

export default {
  name: 'Education',
  components: {
    LanguageSwitcher
  },
  data() {
    return {
      backgroundLoaded: false, // 背景图片加载状态
      loadingProgress: 0, // 加载进度
      loadingText: 'Loading map data...', // 加载文本
      // Travel dropdown related
      showTravelDropdown: false,
      showEducationDropdown: false,
      currentLanguage: 'en',
      travelIslands: ['Mertang', 'P Singa', 'Sipadan', 'Pulau Lima', 'Seri Buat'],
      
      // Verification system related
      showVerification: false, // Control verification interface display - only show on page refresh
      verificationImages: [], // Verification image list
      loadingVerification: false, // Loading verification images status
      selectedImages: [], // User selected image ID list (maximum 4)
      maxSelections: 4, // Maximum selection times
      verificationCompleted: false, // Whether verification is completed
      verificationResult: null, // Verification result
      
      // OSS图片URL
      backgroundImageUrl: null, // 背景图片URL
      imageUrls: {
        appIcon: '',
        video1Thumbnail: '',
        video1Video: '',
        video2Thumbnail: '',
        video2Video: '',
        video3Thumbnail: '',
        video3Video: '',
        video4Thumbnail: '',
        video4Video: ''
      },
      // Local video data - now pointing to YouTube links
      localVideos: [
        {
          titleKey: "education.videos.important",
          thumbnail: 'http://static.coralkita.site/assets/Why are coral reefs so important.jpg',
          video: 'https://youtu.be/eNqbSi_6KdA?si=pmOStLJfsyXi__Jl'
        },
        {
          titleKey: "education.videos.dieOff",
          thumbnail: 'http://static.coralkita.site/assets/What Would Happen If All The Coral Reefs Died Off.jpg',
          video: 'https://youtu.be/_5DooxgwEiw?si=p9U4ANsPrIRu2wDn'
        },
        {
          titleKey: "education.videos.dying",
          thumbnail: 'http://static.coralkita.site/assets/Coral Reefs Are Dying. Here\'s How We Can Save Them.jpg',
          video: 'https://youtu.be/MUAsFZuFQvQ?si=86Ns9Qj6SMD65cfP'
        },
        {
          titleKey: "education.videos.bleaching",
          thumbnail: 'http://static.coralkita.site/assets/Coral Bleaching Explained The Story of Frank the Coral.jpg',
          video: 'https://youtu.be/UyEw_Rl8mqM?si=w4m9p53ynq5XX2HY'
        }
      ]
    }
  },
  computed: {
    ...mapGetters(['isAuthenticated', 'currentUser']),
    
    
  },
  methods: {
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
        console.log('Education background image preloaded to cache');
        this.loadingProgress = 100;
        this.loadingText = 'Map data loaded successfully!';
        setTimeout(() => {
          this.backgroundLoaded = true;
        }, 500);
      };
      img.onerror = () => {
        console.warn('Failed to preload Education background image');
        this.loadingProgress = 100;
        this.loadingText = 'Using backup data...';
        setTimeout(() => {
          this.backgroundLoaded = true; // 即使失败也隐藏占位符
        }, 500);
      };
      
      console.log('Education background image preload started');
    },

    /**
     * 模拟加载进度
     */
    simulateLoadingProgress() {
      const progressSteps = [
        { progress: 20, text: 'Connecting to map server...' },
        { progress: 40, text: 'Loading map data...' },
        { progress: 60, text: 'Locating island information...' },
        { progress: 80, text: 'Generating interactive map...' },
        { progress: 95, text: 'Almost ready...' }
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

    /**
     * 加载所有OSS图片URL
     */
    async loadOssImages() {
      try {
        console.log('开始加载OSS图片...')
        
        // 并行加载所有图片URL（只加载缩略图，视频使用YouTube链接）
        const [
          appIcon,
          video1Thumbnail,
          video2Thumbnail,
          video3Thumbnail,
          video4Thumbnail
        ] = await Promise.all([
          ossService.getSignedUrl('assets/icon.png'),
          ossService.getSignedUrl('assets/Why are coral reefs so important.jpg'),
          ossService.getSignedUrl('assets/What Would Happen If All The Coral Reefs Died Off.jpg'),
          ossService.getSignedUrl('assets/Coral Reefs Are Dying. Here\'s How We Can Save Them.jpg'),
          ossService.getSignedUrl('assets/Coral Bleaching Explained The Story of Frank the Coral.jpg')
        ])

        // 更新图片URL（只更新缩略图）
        this.imageUrls = {
          appIcon,
          video1Thumbnail,
          video2Thumbnail,
          video3Thumbnail,
          video4Thumbnail
        }

        // 更新视频数据
        this.updateVideoData()

        console.log('OSS图片加载完成:', this.imageUrls)
      } catch (error) {
        console.error('加载OSS图片失败:', error)
        // 如果OSS加载失败，使用本地图片作为备用
        this.loadFallbackImages()
      }
    },

    /**
     * 更新视频数据 - 只更新缩略图，保留YouTube链接
     */
    updateVideoData() {
      this.localVideos = [
        {
          titleKey: "education.videos.important",
          thumbnail: this.imageUrls.video1Thumbnail,
          video: 'https://youtu.be/eNqbSi_6KdA?si=pmOStLJfsyXi__Jl'
        },
        {
          titleKey: "education.videos.dieOff",
          thumbnail: this.imageUrls.video2Thumbnail,
          video: 'https://youtu.be/_5DooxgwEiw?si=p9U4ANsPrIRu2wDn'
        },
        {
          titleKey: "education.videos.dying",
          thumbnail: this.imageUrls.video3Thumbnail,
          video: 'https://youtu.be/MUAsFZuFQvQ?si=86Ns9Qj6SMD65cfP'
        },
        {
          titleKey: "education.videos.bleaching",
          thumbnail: this.imageUrls.video4Thumbnail,
          video: 'https://youtu.be/UyEw_Rl8mqM?si=w4m9p53ynq5XX2HY'
        }
      ]
    },

    /**
     * 加载备用本地图片
     */
    loadFallbackImages() {
      console.log('使用本地图片作为备用...')
      this.imageUrls = {
        appIcon: 'http://static.coralkita.site/assets/icon.png',
        video1Thumbnail: 'http://static.coralkita.site/assets/Why are coral reefs so important.jpg',
        video2Thumbnail: 'http://static.coralkita.site/assets/What Would Happen If All The Coral Reefs Died Off.jpg',
        video3Thumbnail: 'http://static.coralkita.site/assets/Coral Reefs Are Dying. Here\'s How We Can Save Them.jpg',
        video4Thumbnail: 'http://static.coralkita.site/assets/Coral Bleaching Explained The Story of Frank the Coral.jpg'
      }
      
      // 更新视频数据
      this.updateVideoData()
    },

    // Travel navigation methods
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
    
    


    // Navigation methods
    goToHome() {
      // Navigate to home page - page refresh, don't set localStorage
      window.location.href = '/';
    },

    goToMap() {
      console.log('Navigate to Map page');
      this.$router.push('/map').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },


    goToGovernment() {
      console.log('Navigate to Government page');
      this.$router.push('/government').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToEducation() {
      console.log('Navigate to Tourism Hub page');
      // We're already on the Education page, so just refresh the current view
      this.$router.push('/education').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToTravelChecklist() {
      console.log('Navigate to Travel Checklist page');
      // Set functional navigation flag
      localStorage.setItem('functionalNavigation', 'true');
      this.$router.push('/travel-checklist').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToAITools() {
      console.log('Navigate to AI Tools page');
      this.$router.push('/ai-tools').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToActionHub() {
      console.log('Navigate to Action Hub page');
      this.$router.push('/action-hub').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToPolicies() {
      console.log('Navigate to Policies page');
      this.$router.push('/policies').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToFAQ() {
      console.log('Navigate to FAQ page');
      this.$router.push('/faq').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    toggleLanguage() {
      this.currentLanguage = this.currentLanguage === 'en' ? 'zh' : 'en'
    },


    async loadQuestions() {
      this.loadingQuestions = true;
      try {
        // Use backend API to get quiz questions
        const response = await fetch('/quiz/questions');
        if (response.ok) {
          const data = await response.json();
          if (data.code === 1) {
            // Randomly select 5 questions
            const allQuestions = data.data;
            this.questions = this.getRandomQuestions(allQuestions, 5);
            this.selectedOptions = new Array(this.questions.length).fill(null);
          } else {
            throw new Error(data.msg || 'Failed to get questions');
          }
        } else {
          throw new Error('Failed to get questions');
        }
      } catch (error) {
        console.error('Error loading quiz questions:', error);
        // If API fails, use default example questions
        this.questions = [
          {
            id: 1,
            question: 'What are coral reefs mainly composed of?',
            options: [
              { id: 1, text: 'Coral polyp skeletons', isCorrect: true },
              { id: 2, text: 'Seaweed', isCorrect: false },
              { id: 3, text: 'Shells', isCorrect: false },
              { id: 4, text: 'Starfish', isCorrect: false }
            ]
          },
          {
            id: 2,
            question: 'Which of the following behaviors is most harmful to coral reefs?',
            options: [
              { id: 1, text: 'Touching corals', isCorrect: false },
              { id: 2, text: 'Using sunscreen', isCorrect: true },
              { id: 3, text: '观察鱼类', isCorrect: false },
              { id: 4, text: '拍照留念', isCorrect: false }
            ]
          },
          {
            id: 3,
            question: '珊瑚礁被称为海洋中的什么？',
            options: [
              { id: 1, text: '沙漠', isCorrect: false },
              { id: 2, text: '雨林', isCorrect: true },
              { id: 3, text: '草原', isCorrect: false },
              { id: 4, text: '山脉', isCorrect: false }
            ]
          },
          {
            id: 4,
            question: 'Which of the following is NOT a major threat to coral reefs?',
            options: [
              { id: 1, text: 'Ocean acidification', isCorrect: false },
              { id: 2, text: 'Overfishing', isCorrect: false },
              { id: 3, text: 'Increase in dolphin population', isCorrect: true },
              { id: 4, text: 'Plastic pollution', isCorrect: false }
            ]
          },
          {
            id: 5,
            question: 'What is the most effective way to protect coral reefs?',
            options: [
              { id: 1, text: '减少碳排放', isCorrect: true },
              { id: 2, text: '增加旅游', isCorrect: false },
              { id: 3, text: '捕捞更多鱼类', isCorrect: false },
              { id: 4, text: '建设更多港口', isCorrect: false }
            ]
          }
        ];
        this.selectedOptions = new Array(this.questions.length).fill(null);
      } finally {
        this.loadingQuestions = false;
      }
    },

    getRandomQuestions(questions, count) {
      const shuffled = [...questions].sort(() => 0.5 - Math.random());
      return shuffled.slice(0, count);
    },



    playVideo(video) {
      try {
        // Open YouTube link in a new tab
        window.open(video.video, '_blank', 'noopener,noreferrer');
      } catch (error) {
        console.error('Error opening video:', error);
        alert('Unable to open video link. Please check your browser settings.');
      }
    },


    // Verification related methods
    async checkFirstTimeVisit() {
      const hasCompletedVerification = localStorage.getItem('verificationCompleted');
      const isFunctionalNavigation = localStorage.getItem('functionalNavigation');
      
      console.log('=== VERIFICATION DEBUG INFO ===');
      console.log('hasCompletedVerification:', hasCompletedVerification);
      console.log('isFunctionalNavigation:', isFunctionalNavigation);
      console.log('================================');
      
      // Show verification if:
      // 1. Verification not completed yet AND
      // 2. This is not a functional navigation from quiz/checklist
      const shouldShowVerification = !hasCompletedVerification && !isFunctionalNavigation;
      
      if (shouldShowVerification) {
        console.log('Showing verification interface - first time visit, page refresh, or main navigation');
        this.showVerification = true;
        await this.loadVerificationImages();
      } else {
        console.log('Not showing verification - already completed or returning from functional navigation');
        this.showVerification = false;
      }
    },



    async loadVerificationImages() {
      this.loadingVerification = true;
      try {
        const response = await axios.get('/quiz/coral-pictures-balanced');
        console.log('API Response:', response.data);
        
        // 记录从后端获取的图片信息
        if (response.data.code === 1 && response.data.data) {
          console.log('从后端获取的图片顺序:');
          response.data.data.forEach((item, index) => {
            console.log(`位置${index + 1}: ${item.answer} - ${item.pictureUrl.substring(item.pictureUrl.lastIndexOf('/') + 1, item.pictureUrl.indexOf('?'))}`);
          });
        }
        
        if (response.data.code === 1 && response.data.data) {
          // 先创建图片数组
          let imageArray = response.data.data.map((item, index) => ({
            id: index + 1,
            imageUrl: item.pictureUrl,
            correctAnswer: item.answer, // 'health' 或 'bleach'
            isSelected: false, // 是否被用户选择
            isCorrect: null // null=未判断, true=选择正确, false=选择错误
          }));
          
          // 打乱图片顺序
          imageArray = this.shuffleArray(imageArray);
          
          // 重新分配ID（保持1-6的顺序显示）
          this.verificationImages = imageArray.map((item, index) => ({
            ...item,
            id: index + 1
          }));
          
          this.selectedImages = [];
          
          console.log('图片顺序已打乱，健康图片位置:', 
            this.verificationImages.map((img, idx) => 
              img.correctAnswer === 'health' ? (idx + 1) : null
            ).filter(pos => pos !== null)
          );
        } else {
          console.error('Failed to load verification images:', response.data.msg);
          // 添加测试数据，以防API失败
          let fallbackImageArray = Array.from({length: 6}, (_, index) => ({
            id: index + 1,
            imageUrl: `https://via.placeholder.com/300x200/4facfe/ffffff?text=Coral${index + 1}`,
            correctAnswer: index < 4 ? 'health' : 'bleach', // 前4个健康，后2个不健康
            isSelected: false,
            isCorrect: null
          }));
          
          // 打乱fallback数据顺序
          fallbackImageArray = this.shuffleArray(fallbackImageArray);
          
          // 重新分配ID
          this.verificationImages = fallbackImageArray.map((item, index) => ({
            ...item,
            id: index + 1
          }));
        }
      } catch (error) {
        console.error('Error loading verification images:', error);
        // 添加测试数据，以防API失败
        let testImageArray = Array.from({length: 6}, (_, index) => ({
          id: index + 1,
          imageUrl: `https://via.placeholder.com/300x200/4facfe/ffffff?text=Coral${index + 1}`,
          correctAnswer: index < 4 ? 'health' : 'bleach', // 前4个健康，后2个不健康
          isSelected: false,
          isCorrect: null
        }));
        
        // 打乱测试数据顺序
        testImageArray = this.shuffleArray(testImageArray);
        
        // 重新分配ID
        this.verificationImages = testImageArray.map((item, index) => ({
          ...item,
          id: index + 1
        }));
      } finally {
        this.loadingVerification = false;
      }
    },

    selectHealthyImage(imageId) {
      const image = this.verificationImages.find(img => img.id === imageId);
      if (!image) return;

      // 如果已经验证完成，不允许继续选择
      if (this.verificationCompleted) return;

      // 如果图片已经被选择，取消选择
      if (image.isSelected) {
        image.isSelected = false;
        image.isCorrect = null;
        this.selectedImages = this.selectedImages.filter(id => id !== imageId);
        return;
      }

      // 如果已经选择了4张图片，不允许继续选择
      if (this.selectedImages.length >= this.maxSelections) {
        alert(`You can only select ${this.maxSelections} images maximum!`);
        return;
      }

      // 选择图片并立即判断正确性
      image.isSelected = true;
      image.isCorrect = (image.correctAnswer === 'health');
      this.selectedImages.push(imageId);

      console.log(`选择图片${imageId}, 正确答案: ${image.correctAnswer}, 选择结果: ${image.isCorrect ? '正确' : '错误'}`);

      // 播放选择动画
      this.playSelectionAnimation(imageId, image.isCorrect);

      // 如果选择了4张图片，自动提交验证
      if (this.selectedImages.length === this.maxSelections) {
        setTimeout(() => {
          this.submitVerification();
        }, 1000); // 延迟1秒让用户看到最后的选择结果
      }
    },

    playSelectionAnimation(imageId, isCorrect) {
      // 简化版本：只打印日志，不进行DOM操作
      console.log(`播放${isCorrect ? '正确' : '错误'}选择动画 - 图片${imageId}`);
    },

    // 数组打乱方法（Fisher-Yates洗牌算法）
    shuffleArray(array) {
      const shuffled = [...array]; // 创建副本，不修改原数组
      for (let i = shuffled.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
      }
      return shuffled;
    },

    submitVerification() {
      console.log('开始验证结果...');
      
      // 检查是否已选择4张图片
      if (this.selectedImages.length < this.maxSelections) {
        alert(`Please select ${this.maxSelections} healthy coral reef images!`);
        return;
      }

      // 计算选择的正确数量（选中的图片中有多少是真正健康的）
      let correctSelections = 0;
      const selectedImages = this.verificationImages.filter(img => img.isSelected);
      
      selectedImages.forEach(image => {
        if (image.correctAnswer === 'health') {
          correctSelections++;
        }
      });

      // 还需要检查是否漏选了健康的图片
      const totalHealthyImages = this.verificationImages.filter(img => img.correctAnswer === 'health').length;
      
      console.log(`选择了${this.selectedImages.length}张图片，其中${correctSelections}张是健康的`);
      console.log(`总共有${totalHealthyImages}张健康图片`);

      // 只有选择的4张图片全部是健康的，并且没有漏选健康图片才算成功
      const allCorrect = (correctSelections === this.maxSelections) && (correctSelections === totalHealthyImages);

      if (allCorrect) {
        this.verificationResult = {
          success: true,
          message: 'Congratulations! You got them all correct. Welcome to the CoralKita knowledge quiz interface!'
        };
      } else {
        this.verificationResult = {
          success: false,
          message: 'Unfortunately, you didn\'t get them all correct. Please enter our interface to learn more about coral reef knowledge and quizzes.'
        };
      }

      this.verificationCompleted = true;
      
      // Mark verification as completed globally
      localStorage.setItem('verificationCompleted', 'true');
    },

    closeVerification() {
      this.showVerification = false;
      // Reset verification state for next verification
      this.verificationCompleted = false;
      this.verificationResult = null;
      this.verificationImages = [];
      this.selectedImages = [];
      // Keep localStorage flag for navigation consistency
    },

    handleOverlayClick() {
      // Only close when verification is completed and showing result
      if (this.verificationCompleted && this.verificationResult) {
        this.closeVerification();
      }
    },

    handleImageError(event) {
      console.warn('图片加载失败:', event.target.src);
      event.target.src = '/api/placeholder/300/200?text=Image+Load+Failed';
    },

    // 手动启动验证游戏
    async startVerificationGame() {
      console.log('手动启动验证游戏');
      
      // 重置验证状态
      this.verificationCompleted = false;
      this.verificationResult = null;
      this.verificationImages = [];
      this.selectedImages = [];
      
      // 显示验证界面并加载图片
      this.showVerification = true;
      await this.loadVerificationImages();
    },

    // 初始化数字动画功能
    initNumbersAnimation() {
      // IntersectionObserver to reveal rows/title and trigger count ups
      const io = new IntersectionObserver((entries) => {
        entries.forEach(e => {
          if (e.isIntersecting) {
            e.target.classList.add('is-in');
            // for each number inside this card, start counting once
            e.target.querySelectorAll('.numbers-count').forEach(this.startNumbersCount);
            io.unobserve(e.target);
          }
        });
      }, { threshold: 0.2 });

      document.querySelectorAll('.numbers-card, .numbers-title').forEach(el => io.observe(el));
    },

    // 数字计数动画
    startNumbersCount(el) {
      if (el.dataset.done) return; // prevent double
      const to = parseFloat(el.dataset.to || '0');
      const decimals = parseInt(el.dataset.decimals || '0');
      const duration = 1200; // ms
      const start = performance.now();
      const from = 0;
      
      function tick(now) {
        const p = Math.min(1, (now - start) / duration);
        const eased = 1 - Math.pow(1 - p, 3); // easeOutCubic
        const val = from + (to - from) * eased;
        el.textContent = val.toLocaleString(undefined, { 
          minimumFractionDigits: decimals, 
          maximumFractionDigits: decimals 
        });
        if (p < 1) {
          requestAnimationFrame(tick);
        } else { 
          el.dataset.done = '1'; 
          el.classList.add('numbers-pop'); 
        }
      }
      requestAnimationFrame(tick);
    }
  },

  async mounted() {
    // 立即开始预加载背景图片
    this.preloadBackgroundImage();
    
    // 加载背景图片
    this.loadBackgroundImage();
    
    // Set global refresh detection timestamp for verification system
    localStorage.setItem('lastPageRefresh', Date.now().toString());
    
    // Load OSS images first
    await this.loadOssImages();
    
    // Check if this is a functional navigation (from quiz/checklist)
    const isFunctionalNavigation = localStorage.getItem('functionalNavigation');
    
    console.log('=== EDUCATION MOUNTED DEBUG ===');
    console.log('isFunctionalNavigation from localStorage:', isFunctionalNavigation);
    console.log('Will clear verification status:', !isFunctionalNavigation);
    console.log('================================');
    
    if (!isFunctionalNavigation) {
      // This is either a page refresh or main navigation, clear verification status
      console.log('Clearing verification status - page refresh or main navigation detected');
      localStorage.removeItem('verificationCompleted');
    } else {
      console.log('Keeping verification status - functional navigation detected');
      // Clear the functional navigation flag after using it
      localStorage.removeItem('functionalNavigation');
    }
    
    // 禁用自动验证 - 只有点击按钮时才触发验证游戏
    console.log('Education组件已挂载，验证游戏已禁用自动触发');
    console.log('用户需要点击按钮手动启动验证游戏');
    
    // 确保验证界面不会自动显示
    this.showVerification = false;
    
    // 初始化数字动画功能
    this.$nextTick(() => {
      this.initNumbersAnimation();
    });
  }
}
</script>

<style scoped>


.education-container {
  min-height: 100vh;
  position: relative;
  display: flex;
  flex-direction: column;
  padding: 0;
  /* 删除 background 相关 */
  background: none;
  /* 删除 transform、will-change */
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
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1;
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

.education-container::before {
  content: "";
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('@/assets/bg_login5.webp') no-repeat center center;
  background-size: cover;
  z-index: -1;
  /* 优化背景图片加载 */
  will-change: transform;      /* 提示浏览器优化 */
  transform: translateZ(0);     /* 启用硬件加速 */
}

.education-header {
  position: relative;
  z-index: 2;
  text-align: center;
  color: white;
  padding: 40px 20px 18px;
  margin-bottom: 24px;
  margin-top: 80px;
}

.education-header h1 {
  font-size: 3rem;
  margin-bottom: 15px;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.8);
  font-weight: 700;
}

.education-header p {
  font-size: 1.3rem;
  opacity: 0.95;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.6);
  margin-bottom: 25px;
}

/* Header buttons container */
.header-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

/* 验证游戏按钮样式 */
.verification-game-btn {
  background: linear-gradient(135deg, #72c4f0ff, #6c98d6ff);
  color: white;
  border: none;
  padding: 15px 30px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 50px;
  cursor: pointer;
  
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.22);
  text-shadow: none;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.verification-game-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.29);
  background: linear-gradient(135deg, #67b0d7ff, #5a8ad3ff);
}

.verification-game-btn:active {
  transform: translateY(0);
  box-shadow: 0 3px 10px rgba(102, 126, 234, 0.5);
}

/* Tips and Guides按钮样式 */
.tips-guides-btn {
  background: linear-gradient(135deg, #28a745, #20c997);
  color: white;
  border: none;
  padding: 15px 30px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 50px;
  cursor: pointer;
  
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.22);
  text-shadow: none;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.tips-guides-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.29);
  background: linear-gradient(135deg, #218838, #1ea085);
}

.tips-guides-btn:active {
  transform: translateY(0);
  box-shadow: 0 3px 10px rgba(40, 167, 69, 0.5);
}

.education-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 40px;
}

/* Video Learning Section */
.video-learning-section {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 30px;
  margin-bottom: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.video-learning-section h2 {
  color: white;
  font-size: 2rem;
  margin-bottom: 25px;
  text-align: center;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

/* Coral Numbers Section */
.coral-numbers-section {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 30px;
  margin-bottom: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.numbers-title {
  color: white;
  font-size: clamp(28px, 3.2vw, 44px);
  text-align: center;
  margin: 0.35rem 0 1.2rem;
  font-weight: 700;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
  opacity: 0;
  transform: translateY(10px);
  transition: opacity 0.6s ease, transform 0.6s ease;
}

.numbers-title.is-in {
  opacity: 1;
  transform: none;
}

.numbers-row {
  display: flex;
  flex-direction: column;
  gap: 32px;
  margin-bottom: 32px;
}

@media (min-width: 768px) {
  .numbers-row {
    flex-direction: row;
  }
}

.numbers-card {
  flex: 1;
  padding: 0 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  opacity: 0;
  transform: translateY(14px);
  transition: opacity 0.6s ease, transform 0.6s ease;
  background: transparent;
  border-radius: 0;
  padding: 20px 0;
  border: none;
}

.numbers-card.is-in {
  opacity: 1;
  transform: none;
}

.numbers-stat {
  font-weight: 800;
  font-size: clamp(32px, 4vw, 52px);
  margin-bottom: 6px;
  line-height: 1;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.numbers-count {
  display: inline-block;
  transform: translateY(0.1em);
}

.numbers-suffix {
  font-weight: 700;
  opacity: 0.9;
  margin-left: 0.15em;
}

.numbers-headline {
  font-weight: 600;
  font-size: 15px;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.numbers-desc {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  line-height: 1.6;
}

.numbers-cta {
  margin-top: 12px;
  align-self: flex-start;
  color: rgba(255, 255, 255, 0.9);
  border-radius: 999px;
  padding: 10px 16px;
  text-decoration: none;
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.3);
  font-size: 14px;
  font-weight: 600;
  transition: 0.2s;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.numbers-cta:hover {
  box-shadow: 0 4px 12px rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
  background: rgba(255, 255, 255, 0.15);
  color: white;
}

.numbers-fraction {
  display: inline-flex;
  align-items: flex-end;
  line-height: 0.9;
}

.numbers-fraction sup {
  font-size: 50%;
  transform: translateY(-2px);
}

.numbers-fraction sub {
  font-size: 50%;
  transform: translateY(2px);
}

/* 进入视口时数字弹跳轻微缩放 */
.numbers-pop {
  animation: numbersPop 0.5s ease-out both;
}

@keyframes numbersPop {
  0% { transform: scale(0.92); }
  100% { transform: scale(1); }
}

/* 无障碍：尊重"减少动效"设置 */
@media (prefers-reduced-motion: reduce) {
  .numbers-title, .numbers-card {
    opacity: 1 !important;
    transform: none !important;
    transition: none !important;
  }
  .numbers-pop {
    animation: none !important;
  }
}

/* 未登录状态样式 */
.login-section {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

/* 登录表单容器 */
.login-form-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.login-form-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  text-align: center;
  color: white;
  max-width: 500px;
  width: 100%;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.login-form-card h2 {
  font-size: 2rem;
  margin-bottom: 15px;
  color: white;
}

.login-form-card p {
  font-size: 1.1rem;
  margin-bottom: 30px;
  opacity: 0.9;
  line-height: 1.6;
}

/* 注册表单容器 */
.register-form-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.register-form-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  text-align: center;
  color: white;
  max-width: 500px;
  width: 100%;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.register-form-card h2 {
  font-size: 2rem;
  margin-bottom: 15px;
  color: white;
}

.register-form-card p {
  font-size: 1.1rem;
  margin-bottom: 30px;
  opacity: 0.9;
  line-height: 1.6;
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.form-header h3 {
  color: #333;
  font-size: 18px;
}

.close-btn {
  font-size: 24px;
  color: #999;
  cursor: pointer;
  padding: 5px;
}

.close-btn:hover {
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: white;
  font-weight: 500;
  text-align: left;
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  font-size: 16px;
  background: rgba(255, 255, 255, 0.1);
  color: white;
  transition: all 0.3s ease;
}

.form-group input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.form-group input:focus {
  outline: none;
  border-color: rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.2);
}

.form-actions {
  margin-top: 30px;
}

.btn-submit {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.btn-submit:hover {
  transform: translateY(-2px);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.form-links {
  text-align: center;
  margin-top: 20px;
}

.link {
  color: rgba(255, 255, 255, 0.9);
  cursor: pointer;
  text-decoration: underline;
  font-size: 14px;
  transition: color 0.3s ease;
}

.link:hover {
  color: white;
}

/* 学习界面样式 */
.learning-interface {
  color: white;
}

/* 移除了登录认证相关样式 */

.user-info-card {
  background: rgba(255, 255, 255, 0.15);
  border-radius: 20px;
  padding: 30px;
  margin-bottom: 40px;
  display: flex;
  align-items: center;
  gap: 25px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(20px);
  color: white;
}

.user-avatar img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid rgba(255, 255, 255, 0.3);
}

.user-details h3 {
  margin-bottom: 15px;
  font-size: 1.5rem;
  color: white;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.user-details p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 1rem;
}

.user-stats {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.2);
  padding: 10px 16px;
  border-radius: 25px;
  font-size: 0.9rem;
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px);
}

.stat-icon {
  font-size: 1.2rem;
}

.user-actions {
  margin-left: auto;
  display: flex;
  gap: 15px;
  align-items: center;
}

.btn-login-action {
  padding: 10px 20px;
  background: rgba(102, 126, 234, 0.8);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.btn-login-action:hover {
  background: rgba(102, 126, 234, 1);
  transform: translateY(-2px);
}

/* 移除了更多登录相关样式 */



/* 功能网格 */
.features-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  margin-top: 40px;
}

.feature-card {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 40px 30px;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 3px solid rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  color: white;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
}

.feature-icon {
  margin-bottom: 25px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80px;
}

.feature-icon img {
  width: 60px;
  height: 60px;
  object-fit: contain;
  filter: brightness(0) invert(1); /* 将图标转换为白色 */
  transition: transform 0.3s ease;
}

.feature-card:hover .feature-icon img {
  transform: scale(1.1);
}

.feature-card h3 {
  margin-bottom: 15px;
  font-size: 1.4rem;
  color: white;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.feature-card p {
  margin-bottom: 30px;
  color: rgba(255, 255, 255, 0.9);
  line-height: 1.6;
  font-size: 1rem;
}

.btn-feature {
  padding: 14px 28px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 600;
  font-size: 1rem;
  backdrop-filter: blur(10px);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.btn-feature:hover {
  background: rgba(47, 111, 151, 0.3);
  border-color: rgba(255, 255, 255, 0.7);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
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

/* 移除小箭头，使用图二样式 */

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

/* Error Message */
.error-message {
  position: fixed;
  top: 20px;
  right: 20px;
  background: #ff4757;
  color: white;
  padding: 12px 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .education-header h1 {
    font-size: 2rem;
  }
  
  .login-form-card h2,
  .register-form-card h2 {
    font-size: 1.5rem;
  }
  
  .user-info-card {
    flex-direction: column;
    text-align: center;
  }
  
  .user-stats {
    justify-content: center;
    flex-wrap: wrap;
  }
  
  .user-actions {
    margin-left: 0;
    margin-top: 20px;
    justify-content: center;
  }
  
  .auth-modal {
    width: 95%;
    margin: 20px;
  }
  
  .auth-header {
    padding: 20px 25px 15px;
  }
  
  .auth-header h2 {
    font-size: 1.5rem;
  }
  
  .auth-form {
    padding: 25px;
  }
  
  .auth-form h3 {
    font-size: 1.4rem;
  }
  
  .auth-form .form-group input {
    padding: 12px 15px;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .feature-card {
    padding: 30px 25px;
  }
  
  .education-header {
    padding: 40px 20px;
  }
  
  .education-header h1 {
    font-size: 2.2rem;
  }
  
  .education-header p {
    font-size: 1.1rem;
  }
  
  .quiz-modal {
    width: 95%;
    margin: 20px;
  }
  
  .quiz-header {
    padding: 20px 25px 15px;
  }
  
  .quiz-header h2 {
    font-size: 1.5rem;
  }
  
  .quiz-content {
    padding: 25px;
  }
  
  .quiz-section h3 {
    font-size: 1.4rem;
  }
  
  .source-selector {
    text-align: center;
  }
  
  .source-selector label {
    text-align: center;
  }
  
  .source-selector select {
    width: 100%;
  }
  
  .video-grid-2x2 {
    grid-template-columns: 1fr;
    grid-template-rows: auto;
    max-width: 100%;
    padding: 15px 0;
    gap: 15px;
  }
  
  .video-grid {
    grid-template-columns: 1fr;
  }
  
  .video-item {
    width: 100%;
  }
  
  .video-thumbnail {
    height: 100px;
  }
  
  .video-display {
    overflow-x: hidden;
  }
  
  .nav-arrow {
    display: none;
  }
  
  .video-indicators {
    display: none;
  }
  
  .quiz-progress {
    flex-direction: column;
    gap: 10px;
  }
  
  .question-container {
    padding: 20px;
  }
  
  .question-text {
    font-size: 1.1rem;
  }
  
  .answer-result {
    padding: 15px;
  }
  
  .result-icon {
    font-size: 2.5rem;
  }
  
  .result-text {
    font-size: 1.3rem;
  }
}

.video-indicators {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}

.indicator {
  width: 10px;
  height: 10px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  cursor: pointer;
  transition: background 0.3s ease;
}

.indicator.active {
  background: white;
  width: 15px;
  height: 15px;
}

/* 测验界面样式 */
.quiz-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(5px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.quiz-modal {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 25px;
  padding: 0;
  max-width: 900px;
  width: 95%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s ease;
  display: flex;
  flex-direction: column;
}

.quiz-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25px 30px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.quiz-header h2 {
  color: white;
  font-size: 1.8rem;
  margin: 0;
  font-weight: 600;
}

.btn-close-quiz {
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.8);
  font-size: 28px;
  cursor: pointer;
  padding: 5px;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.btn-close-quiz:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
  transform: rotate(90deg);
}

.quiz-content {
  flex-grow: 1;
  padding: 30px;
  color: white;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.video-section, .quiz-section {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 30px;
  text-align: center;
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.video-section h3, .quiz-section h3 {
  margin-bottom: 20px;
  font-size: 1.5rem;
  color: white;
}

.quiz-progress {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
  color: white;
  font-size: 1.1rem;
}

.progress-text {
  font-weight: 500;
}

.progress-bar {
  flex-grow: 1;
  height: 8px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  border-radius: 4px;
  transition: width 0.3s ease-in-out;
}

.question-container {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 30px;
  text-align: center;
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.question-display {
  text-align: left;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.question-text {
  margin-bottom: 25px;
  font-size: 1.3rem;
  color: white;
}

.answer-result {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 15px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.result-icon {
  font-size: 3rem;
  color: white;
}

.result-icon.correct {
  color: #4CAF50; /* Green for correct */
}

.result-icon.incorrect {
  color: #F44336; /* Red for incorrect */
}

.result-text {
  font-size: 1.5rem;
  font-weight: 600;
  color: white;
}

.result-explanation {
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.6;
  text-align: left;
  padding: 0 10px;
}

.btn-next-question {
  padding: 14px 28px;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(79, 172, 254, 0.3);
}

.btn-next-question:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(79, 172, 254, 0.4);
}

.btn-next-question:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.video-carousel {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
  position: relative;
}

.video-container {
  flex-grow: 1;
  overflow-x: auto;
  display: flex;
  gap: 20px;
  padding: 10px 0;
}

.nav-arrow {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.3s ease;
  color: white;
  font-size: 2rem;
  z-index: 10;
}

.nav-arrow:hover {
  background: rgba(255, 255, 255, 0.2);
}

.video-display {
  flex-grow: 1;
  overflow-x: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.video-display::-webkit-scrollbar {
  display: none;
}

/* 2x2 Video Grid Layout */
.video-grid-2x2 {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 20px;
  padding: 20px 0;
  max-width: 1000px;
  margin: 0 auto;
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
  padding-bottom: 20px;
}

.video-item {
  cursor: pointer;
  transition: transform 0.2s ease;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);

}

.video-item:hover {
  transform: translateY(-5px);
}

.video-thumbnail {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.video-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.play-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 15px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.video-item:hover .play-overlay {
  opacity: 1;
}

.play-icon {
  font-size: 2rem;
  color: white;
}

.play-button {
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid white;
  border-radius: 50%;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.3s ease;
}

.play-button:hover {
  background: rgba(255, 255, 255, 0.3);
}

.video-title {
  padding: 10px 15px;
  font-size: 0.9rem;
  color: white;
  text-align: left;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 0 0 15px 15px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.loading-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 0;
  color: white;
  font-size: 1.1rem;
}

.loading-spinner {
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top: 4px solid white;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.no-videos, .no-questions {
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.1rem;
  padding: 20px 0;
}

.questions-container {
  text-align: left;
}

.question-item {
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.question-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.question-item h4 {
  margin-bottom: 15px;
  font-size: 1.2rem;
  color: white;
}

.options {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.option {
  display: flex;
  align-items: center;
  padding: 12px 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
  color: white;
}

.option:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.8);
}

.option.selected {
  background: rgba(255, 255, 255, 0.2);
  border-color: #4facfe;
  box-shadow: 0 0 10px rgba(79, 172, 254, 0.3);
}

.option input[type="radio"] {
  margin-right: 15px;
  width: 20px;
  height: 20px;
  accent-color: white;
}

.option-text {
  flex-grow: 1;
}

.quiz-actions {
  margin-top: 30px;
  text-align: right;
}

.btn-submit-quiz {
  padding: 14px 28px;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(79, 172, 254, 0.3);
}

.btn-submit-quiz:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(79, 172, 254, 0.4);
}

.btn-submit-quiz:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.btn-submit-answer {
  padding: 14px 28px;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(79, 172, 254, 0.3);
}

.btn-submit-answer:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(79, 172, 254, 0.4);
}

.btn-submit-answer:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.question-actions {
  margin-top: 30px;
  text-align: center;
}

/* 验证界面样式 */
.verification-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(5px);
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 5vh;
  z-index: 2000;
  animation: fadeIn 0.3s ease forwards;
}

.verification-overlay.clickable-overlay {
  cursor: pointer;
}

.verification-overlay.clickable-overlay .verification-modal {
  cursor: default;
}

.verification-modal {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 25px;
  padding: 0;
  max-width: 900px;
  width: 95%;
  max-height: 85vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s ease forwards;
}

.verification-header {
  position: relative;
  text-align: center;
  padding: 30px 30px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.verification-header h2 {
  color: white;
  font-size: 2rem;
  margin-bottom: 10px;
  font-weight: 700;
}

.verification-header p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 1.1rem;
  margin: 0;
}

.verification-header::after {
  content: "Click to select 4 healthy coral reef images";
  display: block;
  margin-top: 10px;
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.7);
  font-style: italic;
}

.btn-close-verification {
  position: absolute;
  top: 15px;
  right: 15px;
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.8);
  font-size: 28px;
  cursor: pointer;
  padding: 8px;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  z-index: 10;
}

.btn-close-verification:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
  transform: rotate(90deg);
}

.verification-content {
  padding: 30px;
}

.loading-verification {
  text-align: center;
  color: white;
  padding: 40px 20px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top: 4px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

.images-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.selection-progress {
  grid-column: 1 / -1;
  text-align: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  margin-bottom: 20px;
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.selection-progress h4 {
  margin: 0 0 10px 0;
  font-size: 1.2rem;
  color: white;
}

.selection-progress p {
  margin: 5px 0;
  font-size: 1rem;
  opacity: 0.9;
}

.image-item {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 15px;
  text-align: center;
  border: 3px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
  cursor: pointer;
  position: relative;
}

.image-item:hover {
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
  transform: translateY(-8px);
  border-color: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
}

.image-item.selected-correct {
  border-color: #4CAF50;
  box-shadow: 0 0 20px rgba(76, 175, 80, 0.6);
  background: rgba(76, 175, 80, 0.15);
}

.image-item.selected-incorrect {
  border-color: #F44336;
  box-shadow: 0 0 20px rgba(244, 67, 54, 0.6);
  background: rgba(244, 67, 54, 0.15);
}

/* 只在刚选择时播放动画 */
.image-item.just-selected-correct {
  animation: correctSelection 0.5s ease forwards;
}

.image-item.just-selected-incorrect {
  animation: incorrectSelection 0.5s ease forwards;
}

.image-item.disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

@keyframes correctSelection {
  0% { transform: scale(1); }
  50% { transform: scale(1.05); }
  100% { transform: scale(1); }
}

@keyframes incorrectSelection {
  0% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
  100% { transform: translateX(0); }
}

.image-container {
  position: relative;
  margin-bottom: 15px;
}

.verification-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 10px;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.image-number {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 14px;
}



/* 选择状态指示器 */
.selection-indicator {
  position: absolute;
  top: 10px;
  left: 10px;
  padding: 5px 10px;
  border-radius: 15px;
  font-size: 12px;
  font-weight: bold;
  backdrop-filter: blur(10px);
  z-index: 10;
}

.correct-indicator {
  background: rgba(76, 175, 80, 0.9);
  color: white;
}

.incorrect-indicator {
  background: rgba(244, 67, 54, 0.9);
  color: white;
}

/* 点击提示 */
.click-hint {
  margin-top: 10px;
  font-size: 14px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
}

.image-item.selected-correct .click-hint {
  color: #4CAF50;
  font-weight: bold;
}

.image-item.selected-incorrect .click-hint {
  color: #F44336;
  font-weight: bold;
}

.verification-result {
  text-align: center;
  color: white;
  padding: 40px 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.verification-result:hover {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 15px;
}

.verification-result .result-icon {
  font-size: 80px;
  margin-bottom: 20px;
  animation: bounceIn 0.6s ease-out;
}

.verification-result .result-icon.success {
  color: #4CAF50;
}

.verification-result .result-icon.failure {
  color: #FF9800;
}

.verification-result h3 {
  font-size: 28px;
  margin-bottom: 15px;
  font-weight: 700;
}

.verification-result p {
  font-size: 16px;
  line-height: 1.6;
  opacity: 0.9;
}

.click-to-close-hint {
  margin-top: 20px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.7);
  font-style: italic;
  animation: fadeInOut 2s infinite;
}

@keyframes fadeInOut {
  0%, 100% { opacity: 0.5; }
  50% { opacity: 1; }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.9);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes bounceIn {
  0% {
    transform: scale(0.3);
    opacity: 0;
  }
  50% {
    transform: scale(1.05);
  }
  70% {
    transform: scale(0.9);
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .verification-modal {
    width: 95%;
    margin: 20px;
  }
  
  .images-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
  }
  
  .verification-image {
    height: 120px;
  }
  
  .verification-header h2 {
    font-size: 1.5rem;
  }
  
  .verification-content {
    padding: 20px;
  }
}

@media (max-width: 480px) {
  .images-grid {
    grid-template-columns: 1fr;
  }
  
  .verification-image {
    height: 180px;
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
</style>
