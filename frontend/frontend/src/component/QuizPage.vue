<template>
  <div class="quiz-page">
    <!-- Page Header -->
    <div class="quiz-header">
      <div class="header-left">
        <button class="btn-back" @click="goBack">
          <i class="back-icon"><</i>
          Back
        </button>
      </div>
      <div class="header-center">
        <h1>Coral Reef Knowledge Quiz</h1>
      </div>
      <div class="header-right">
        <div class="app-info">
        </div>
      </div>
    </div>

    <!-- Main Content Area -->
    <div class="quiz-content">
      <!-- Video Learning Section -->
      <div class="video-section">
        <h2>Video Learning</h2>
        <div class="video-carousel">
          <button 
            class="nav-arrow left" 
            @click="prevVideo" 
            :disabled="currentVideoIndex === 0"
          >
            &#10094;
          </button>
          
          <div class="video-container">
            <div v-if="loadingVideo" class="loading-placeholder">
              <div class="loading-spinner"></div>
              <p>Loading videos...</p>
            </div>
            <div v-else-if="videoSources.length === 0" class="no-videos">
              <p>No video resources available</p>
            </div>
            <div v-else class="video-grid">
              <div 
                v-for="(video, index) in visibleVideos" 
                :key="video.id" 
                class="video-item"
                @click="playVideo(video)"
              >
                <div class="video-thumbnail">
                  <img 
                    :src="video.thumbnail || '/api/placeholder/300/200'" 
                    :alt="video.title" 
                    @error="handleThumbnailError"
                    loading="lazy"
                  />
                  <div class="play-overlay">
                    <div class="play-button">
                      <span class="play-icon">▶</span>
                    </div>
                  </div>
                </div>
                <div class="video-title">{{ video.title }}</div>
              </div>
            </div>
          </div>
          
          <button 
            class="nav-arrow right" 
            @click="nextVideo" 
            :disabled="currentVideoIndex >= videoSources.length - 4"
          >
            &#10095;
          </button>
        </div>
        
        <!-- Video Indicators -->
        <div class="video-indicators" v-if="videoSources.length > 4">
          <span 
            v-for="(video, index) in videoSources" 
            :key="index"
            class="indicator"
            :class="{ active: index >= currentVideoIndex && index < currentVideoIndex + 4 }"
            @click="goToVideo(index)"
          ></span>
        </div>
      </div>

      <!-- Quiz Questions Section -->
      <div class="quiz-section">
        <div class="quiz-header-info">
          <h2>Knowledge Quiz</h2>
        </div>
        

        
        <!-- Quiz Progress and Questions (shown when quiz is active) -->
        <div v-if="questions.length > 0" class="quiz-active-section">
          <div class="quiz-progress">
            <span class="progress-text">Question {{ currentQuestionIndex + 1 }} / {{ questions.length }}</span>
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: progressPercentage + '%' }"></div>
            </div>
          </div>
          
          <div class="question-container">
            <div v-if="loadingQuestions" class="loading-placeholder">
              <div class="loading-spinner"></div>
              <p>Loading questions...</p>
            </div>
            <div v-else-if="questions.length === 0" class="no-questions">
              <div class="empty-state">
                <h3>Ready to Start Quiz</h3>
                <p>Please select a video source to begin your quiz</p>
                <button v-if="sourceTitles.length > 0" class="btn-start-quiz" @click="loadRandomQuestions">
                  Start Random Quiz
                </button>
              </div>
            </div>
            <div v-else-if="showAnswerResult" class="answer-result">
              <div class="result-icon" :class="isAnswerCorrect ? 'correct' : 'incorrect'">
                {{ isAnswerCorrect ? '✓' : '✗' }}
              </div>
              <h3 class="result-text">{{ isAnswerCorrect ? 'Correct Answer!' : 'Wrong Answer!' }}</h3>
              <div v-if="!isAnswerCorrect && currentQuestion" class="result-explanation">
                <p class="correct-answer">Correct Answer: {{ getCorrectAnswerText() }}</p>
                <div v-if="currentQuestion.explanation" class="explanation-text">
                  <p>{{ currentQuestion.explanation }}</p>
                </div>
              </div>
              <div class="result-actions">
                <button class="btn-next-question" @click="nextQuestion">
                  {{ currentQuestionIndex < questions.length - 1 ? 'Next Question' : 'Complete Quiz' }}
                </button>
              </div>
            </div>
            <div v-else-if="quizCompleted" class="quiz-completed">
              <!-- Medal Display -->
              <div v-if="medalInfo" class="medal-display">
                <div class="medal-icon">
                  <img v-if="medalInfo.image" :src="medalInfo.image" :alt="medalInfo.title" class="medal-image" />
                  <span v-else :style="{ color: medalInfo.color }">{{ medalInfo.icon }}</span>
                </div>
                <h2 class="medal-title">{{ medalInfo.title }}</h2>
                <p class="medal-message">{{ medalInfo.message }}</p>
              </div>
              
              <!-- Detailed Statistics -->
              <div class="quiz-statistics">
                <div class="stats-grid">
                  <div class="stat-card correct">
                    <div class="stat-icon">✓</div>
                    <div class="stat-info">
                      <span class="stat-number">{{ correctAnswers }}</span>
                      <span class="stat-label">Correct</span>
                    </div>
                  </div>
                  <div class="stat-card wrong">
                    <div class="stat-icon">✗</div>
                    <div class="stat-info">
                      <span class="stat-number">{{ wrongAnswers }}</span>
                      <span class="stat-label">Wrong</span>
                    </div>
                  </div>
                  <div class="stat-card total">
                    <div class="stat-icon">📊</div>
                    <div class="stat-info">
                      <span class="stat-number">{{ totalQuestions }}</span>
                      <span class="stat-label">Total</span>
                    </div>
                  </div>
                </div>
                
                <div class="score-summary">
                  <h4>Quiz Results</h4>
                  <p class="score-text">Total Score: <span class="score-number">{{ quizScore }}</span> / {{ totalQuestions }}</p>
                  <p class="accuracy-text">Accuracy: <span class="accuracy-number">{{ Math.round((correctAnswers / totalQuestions) * 100) }}%</span></p>
                </div>
              </div>

              <!-- Reward Information -->
              <div v-if="isAuthenticated && scoreResult" class="reward-info">
                <h4>🎁 Congratulations! You've earned rewards!</h4>
                <div class="rewards-grid">
                  <div class="reward-card experience">
                    <div class="reward-icon">⭐</div>
                    <div class="reward-details">
                      <span class="reward-amount">+{{ scoreResult.earnedExperience }}</span>
                      <span class="reward-type">Experience</span>
                    </div>
                  </div>
                  <div class="reward-card points">
                    <div class="reward-icon">💎</div>
                    <div class="reward-details">
                      <span class="reward-amount">+{{ scoreResult.earnedPoints }}</span>
                      <span class="reward-type">Points</span>
                    </div>
                  </div>
                </div>
                <div class="level-progress">
                  <p class="level-info">
                    <span class="level-badge">Level {{ scoreResult.level }}</span>
                    <span class="experience-info">Total Experience: {{ scoreResult.experience }}</span>
                  </p>
                </div>
              </div>
              
              <!-- Guest User Prompt -->
              <div v-else-if="!isAuthenticated" class="login-prompt">
                <p class="prompt-text">💡 Login to earn experience points and rewards!</p>
                <router-link to="/education" class="login-suggestion" @click.native="setNavigationFlag">Login Now</router-link>
              </div>

              <!-- Share Section - Show when user gets 5 or more correct answers -->
              <div v-if="correctAnswers >= 5" class="share-section">
                <div class="share-header">
                  <h3>🎉 Share Your Achievement!</h3>
                  <p>You did great! Share your {{ medalInfo.title }} with friends</p>
                </div>
                
                <div class="share-options">
                  <button class="btn-share twitter" @click="shareToTwitter">
                    <img src="@/assets/icons/icon_twiter.png" alt="Twitter" class="share-icon" />
                    <span>Twitter</span>
                  </button>
                  
                  <button class="btn-share facebook" @click="shareToFacebook">
                    <img src="@/assets/icons/icon_facebook.png" alt="Facebook" class="share-icon" />
                    <span>Facebook</span>
                  </button>
                  
                  <button class="btn-share copy" @click="copyShareLink">
                    <img src="@/assets/icons/icon_link.png" alt="Copy Link" class="share-icon" />
                    <span>Copy Link</span>
                  </button>
                  
                  <button class="btn-share download" @click="downloadBadge">
                    <img src="@/assets/icons/icon_download.png" alt="Download Badge" class="share-icon" />
                    <span>Download Badge</span>
                  </button>
                </div>
                
                <!-- Share Link Copied Notification -->
                <div v-if="showCopyNotification" class="copy-notification">
                  ✅ Share link copied to clipboard!
                </div>
              </div>

              <div class="completion-actions">
                <button class="btn-restart" @click="restartQuiz">Restart Quiz</button>
                <button class="btn-back-home" @click="goBack">Back to Home</button>
              </div>
            </div>
                      <div v-else class="question-display">
            <!-- 题目显示 -->
            <div>
              <h3 class="question-text">{{ currentQuestion.question }}</h3>
            </div>
              <div class="options">
                <label 
                  v-for="(option, optIndex) in currentQuestion.options" 
                  :key="optIndex" 
                  class="option"
                  :class="{ 
                    selected: selectedOption === option.id
                  }"
                  @click="selectOption(option.id)"
                >
                  <input 
                    type="radio" 
                    :name="'question_' + currentQuestionIndex" 
                    :value="option.id" 

                    v-model="selectedOption"
                  />
                  <span class="option-text">{{ option.text }}</span>
                </label>
              </div>
              
              <div class="question-actions">
                <button 
                  class="btn-submit-answer" 
                  @click="submitAnswer"
                  :disabled="selectedOption === null"
                >
Submit Answer
                </button>

              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Video Playback Modal -->
    <div v-if="showVideoModal" class="video-modal-overlay" @click="closeVideoModal">
      <div class="video-modal" @click.stop>
        <div class="video-modal-header">
          <h3>{{ currentPlayingVideo.title }}</h3>
          <button class="btn-close-video" @click="closeVideoModal">×</button>
        </div>
        <div class="video-modal-content">
          <div class="video-player">
            <video 
              v-if="currentPlayingVideo.video_src"
              :src="currentPlayingVideo.video_src"
              controls
              width="100%"
              height="400"
              @error="handleVideoError"
              class="local-video-player"
            >
              Your browser does not support video playback
            </video>
            <div v-else class="video-placeholder">
              <div class="placeholder-content">
                <div class="placeholder-icon">📹</div>
                <h4>Video Temporarily Unavailable</h4>
                <p>Video file failed to load</p>
              </div>
            </div>
          </div>
          <div v-if="isAuthenticated" class="video-completion">
            <button class="btn-mark-watched" @click="markVideoWatched">
              Mark as Watched (+2 Experience)
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Error Message -->
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import axios from 'axios'

export default {
  name: 'QuizPage',
  data() {
    return {
      // Video related
      videoSources: [],
      loadingVideo: false,
      currentVideoIndex: 0,
      showVideoModal: false,
      currentPlayingVideo: null,
      watchedVideos: new Set(),

      // Quiz related
      questions: [],
      loadingQuestions: false,
      currentQuestionIndex: 0,
      selectedOption: null,
      showAnswerResult: false,
      isAnswerCorrect: false,
      quizScore: 0,
      quizCompleted: false,
      scoreResult: null,
      sourceTitles: [],
      correctAnswers: 0,
      wrongAnswers: 0,
      totalQuestions: 0,
      
      // Medal system
      medalInfo: null,
      showMedal: false,

      // Share system
      showCopyNotification: false,

      // Other
      errorMessage: '',
      quizStartTime: null,
      quizEndTime: null
    }
  },
  computed: {
    ...mapGetters(['isAuthenticated', 'currentUser']),
    
    // Get currently visible videos (show 4)
    visibleVideos() {
      const startIndex = this.currentVideoIndex
      const endIndex = Math.min(this.videoSources.length, startIndex + 4)
      return this.videoSources.slice(startIndex, endIndex)
    },
    
    // Currently displayed question
    currentQuestion() {
      return this.questions[this.currentQuestionIndex] || null
    },
    
    // Progress percentage
    progressPercentage() {
      if (this.questions.length === 0) return 0
      return ((this.currentQuestionIndex + 1) / this.questions.length) * 100
    }
  },
  async mounted() {
    // Set global refresh detection timestamp for verification system
    localStorage.setItem('lastPageRefresh', Date.now().toString());
    
    await this.loadVideoSources()
    await this.loadSourceTitles()
    
    // Auto-start quiz when page loads
    this.loadRandomQuestions()
  },
  methods: {
    // Go back to previous page
    goBack() {
      // 设置功能导航标记，表示这是从功能页面返回
      localStorage.setItem('functionalNavigation', 'true');
      this.$router.push('/education').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    // Set navigation flag for router-link clicks
    setNavigationFlag() {
      localStorage.setItem('functionalNavigation', 'true');
    },

    // Load video sources
    async loadVideoSources() {
      this.loadingVideo = true
      this.errorMessage = ''
      try {
        // Use local video files and thumbnails
        const localVideoData = [
          {
            id: 1,
            title: 'Why are coral reefs so important?',
            video_src: require('@/assets/Why are coral reefs so important.mp4'),
            thumbnail: require('@/assets/Why are coral reefs so important.jpg')
          },
          {
            id: 2,
            title: 'What Would Happen If All The Coral Reefs Died Off?',
            video_src: require('@/assets/What Would Happen If All The Coral Reefs Died Off.mp4'),
            thumbnail: require('@/assets/What Would Happen If All The Coral Reefs Died Off.jpg')
          },
          {
            id: 3,
            title: 'Coral Reefs Are Dying. Here\'s How We Can Save Them',
            video_src: require('@/assets/Coral Reefs Are Dying. Here\'s How We Can Save Them.mp4'),
            thumbnail: require('@/assets/Coral Reefs Are Dying. Here\'s How We Can Save Them.jpg')
          },
          {
            id: 4,
            title: 'Coral Bleaching Explained: The Story of Frank the Coral',
            video_src: require('@/assets/Coral Bleaching Explained The Story of Frank the Coral.mp4'),
            thumbnail: require('@/assets/Coral Bleaching Explained The Story of Frank the Coral.jpg')
          },
          {
            id: 5,
            title: 'Coral Reefs 101 | National Geographic',
            video_src: require('@/assets/Coral Reefs 101 National Geographic.mp4'),
            thumbnail: require('@/assets/Coral Reefs 101 National Geographic.jpg')
          }
        ]
        
        this.videoSources = localVideoData
        
      } catch (error) {
        this.errorMessage = 'Failed to load local videos: ' + error.message
        console.error('Failed to load local videos:', error)
      } finally {
        this.loadingVideo = false
      }
    },

    // Load source titles
    async loadSourceTitles() {
      try {
        console.log('Loading source titles...')
        const response = await axios.get('/quiz/sources')
        console.log('Source titles response:', response.data)
        if (response.data.code === 1) {
          this.sourceTitles = response.data.data || []
          console.log('Loaded source titles:', this.sourceTitles)
        } else {
          console.error('Failed to load source titles:', response.data)
          this.errorMessage = 'Failed to get quiz sources: ' + (response.data.msg || 'Unknown error')
        }
      } catch (error) {
        console.error('Failed to load source titles:', error)
        // Use fallback data to ensure functionality
        this.sourceTitles = ['Coral Reefs 101 | National Geographic']
        this.errorMessage = ''
      }
    },

    // Video navigation
    prevVideo() {
      if (this.currentVideoIndex > 0) {
        this.currentVideoIndex--
      }
    },

    nextVideo() {
      if (this.currentVideoIndex < this.videoSources.length - 4) {
        this.currentVideoIndex++
      }
    },

    goToVideo(index) {
      this.currentVideoIndex = Math.max(0, Math.min(index, this.videoSources.length - 4))
    },

    // Play video
    playVideo(video) {
      this.currentPlayingVideo = video
      this.showVideoModal = true
    },

    closeVideoModal() {
      this.showVideoModal = false
      this.currentPlayingVideo = null
    },



    // Handle video loading error
    handleVideoError() {
      console.error('Video loading failed')
      this.errorMessage = 'Video loading failed, please try refreshing the page or check your network connection'
      setTimeout(() => {
        this.errorMessage = ''
      }, 5000)
    },

    // Handle thumbnail loading error
    handleThumbnailError(event) {
      console.warn('Thumbnail loading failed:', event.target.src)
      event.target.src = '/api/placeholder/300/200?text=Video+Thumbnail'
    },

    // Handle image loading error for image classification questions
    handleImageError(event) {
      console.warn('Image loading failed:', event.target.src)
      event.target.src = '/api/placeholder/300/200?text=Image+Placeholder'
    },

    // Mark video as watched
    async markVideoWatched() {
      if (!this.isAuthenticated || !this.currentPlayingVideo) return

      const videoId = this.currentPlayingVideo.id
      if (this.watchedVideos.has(videoId)) {
        this.errorMessage = 'You have already watched this video'
        return
      }

      try {
        // Here should call backend API to increase experience points
        // Temporarily simulate local update
        this.watchedVideos.add(videoId)
        
        // Show reward information
        this.$set(this, 'errorMessage', 'Congratulations! You earned 2 experience points')
        setTimeout(() => {
          this.errorMessage = ''
        }, 3000)

        this.closeVideoModal()
      } catch (error) {
        this.errorMessage = 'Failed to mark as watched: ' + (error.response?.data?.msg || error.message)
      }
    },

    // Load random questions (10 questions from all quiz banks)
    async loadRandomQuestions() {
      console.log('Loading 10 random questions from all quiz banks')

      this.loadingQuestions = true
      this.errorMessage = ''
      try {
        const response = await axios.get('/quiz/random')
        console.log('Random questions response:', response.data)

        if (response.data.code === 1 && response.data.data) {
          console.log('Raw questions data:', response.data.data)
          // 随机选择10道题
          const allQuestions = response.data.data.map(q => {
            return {
              ...q,
              options: [
                { id: 'A', text: q.optionA },
                { id: 'B', text: q.optionB },
                { id: 'C', text: q.optionC },
                { id: 'D', text: q.optionD }
              ].filter(opt => opt.text && opt.text.trim() !== '')
            }
          })
          
          // 随机选择10道题
          this.questions = this.getRandomQuestions(allQuestions, 10)
          
          console.log('Final questions array (10 random):', this.questions)
          this.resetQuiz()
          this.quizStartTime = new Date()
        } else {
          this.errorMessage = '未能获取到题目数据：' + (response.data.msg || '后端返回错误')
          console.error('Backend returned error:', response.data)
        }
      } catch (error) {
        console.error('加载题目失败:', error)
        console.error('Error details:', error.response?.data)
        
        // 使用备用题目数据，确保功能可用（10道题）
        const fallbackQuestions = [
          {
            id: 1,
            question: "What is a coral reef primarily made of?",
            optionA: "Rocks",
            optionB: "Living coral polyps",
            optionC: "Seaweed", 
            optionD: "Sand",
            correctOption: "B",
            explanation: "Coral reefs are built by tiny coral polyps that secrete calcium carbonate to form their hard skeletons. Over time, these skeletons accumulate to create the reef structure we see today.",
            sourceTitle: "Coral Reefs 101 | National Geographic",
            options: [
              { id: 'A', text: 'Rocks' },
              { id: 'B', text: 'Living coral polyps' },
              { id: 'C', text: 'Seaweed' },
              { id: 'D', text: 'Sand' }
            ]
          },
          {
            id: 2,
            question: "Coral are actually what kind of organisms?",
            optionA: "Plants",
            optionB: "Fungi",
            optionC: "Animals",
            optionD: "Rocks",
            correctOption: "C",
            explanation: "Despite their plant-like appearance, corals are actually animals. They belong to the phylum Cnidaria, the same group as jellyfish and sea anemones.",
            sourceTitle: "Coral Reefs 101 | National Geographic",
            options: [
              { id: 'A', text: 'Plants' },
              { id: 'B', text: 'Fungi' },
              { id: 'C', text: 'Animals' },
              { id: 'D', text: 'Rocks' }
            ]
          },
          {
            id: 3,
            question: "How do polyps contribute structurally to a reef?",
            optionA: "By photosynthesizing",
            optionB: "By secreting limestone skeletons",
            optionC: "By forming sand",
            optionD: "By growing as separate individuals",
            correctOption: "B",
            explanation: "Coral polyps extract calcium carbonate from seawater and secrete it to form hard limestone skeletons. These skeletons provide the structural foundation of coral reefs.",
            sourceTitle: "Coral Reefs 101 | National Geographic",
            options: [
              { id: 'A', text: 'By photosynthesizing' },
              { id: 'B', text: 'By secreting limestone skeletons' },
              { id: 'C', text: 'By forming sand' },
              { id: 'D', text: 'By growing as separate individuals' }
            ]
          },
          {
            id: 4,
            question: "What percentage of marine species live in coral reefs?",
            optionA: "5%",
            optionB: "10%",
            optionC: "25%",
            optionD: "50%",
            correctOption: "C",
            explanation: "About 25% of all marine species depend on coral reefs at some point in their lives. This makes coral reefs some of the most biodiverse ecosystems on Earth, despite covering less than 1% of the ocean floor.",
            sourceTitle: "Coral Reefs 101 | National Geographic",
            options: [
              { id: 'A', text: '5%' },
              { id: 'B', text: '10%' },
              { id: 'C', text: '25%' },
              { id: 'D', text: '50%' }
            ]
          },
          {
            id: 5,
            question: "What helps coral reefs grow faster?",
            optionA: "Zooxanthellae (algae) within their tissues",
            optionB: "They grow faster in cooler waters",
            optionC: "They bleach (evict algae)",
            optionD: "They move to cooler waters",
            correctOption: "A",
            explanation: "Zooxanthellae are symbiotic algae that live inside coral tissues. They perform photosynthesis and provide up to 90% of the coral's energy needs, significantly boosting their growth rate.",
            sourceTitle: "Coral Reefs 101 | National Geographic",
            options: [
              { id: 'A', text: 'Zooxanthellae (algae) within their tissues' },
              { id: 'B', text: 'They grow faster in cooler waters' },
              { id: 'C', text: 'They bleach (evict algae)' },
              { id: 'D', text: 'They move to cooler waters' }
            ]
          },
          {
            id: 6,
            question: "What is coral bleaching?",
            optionA: "Corals turning white when healthy",
            optionB: "Corals expelling algae due to stress",
            optionC: "Natural coral reproduction",
            optionD: "Corals growing faster",
            correctOption: "B",
            explanation: "Coral bleaching occurs when stressed corals expel their symbiotic algae, turning white.",
            sourceTitle: "Coral Bleaching",
            options: [
              { id: 'A', text: 'Corals turning white when healthy' },
              { id: 'B', text: 'Corals expelling algae due to stress' },
              { id: 'C', text: 'Natural coral reproduction' },
              { id: 'D', text: 'Corals growing faster' }
            ]
          },
          {
            id: 7,
            question: "What is the main cause of coral bleaching?",
            optionA: "Ocean pollution",
            optionB: "Rising water temperatures",
            optionC: "Overfishing",
            optionD: "Ocean acidification",
            correctOption: "B",
            explanation: "Rising water temperatures due to climate change is the primary cause of mass coral bleaching events.",
            sourceTitle: "Climate Change and Corals",
            options: [
              { id: 'A', text: 'Ocean pollution' },
              { id: 'B', text: 'Rising water temperatures' },
              { id: 'C', text: 'Overfishing' },
              { id: 'D', text: 'Ocean acidification' }
            ]
          },
          {
            id: 8,
            question: "How long can some corals live?",
            optionA: "100 years",
            optionB: "500 years",
            optionC: "1,000 years",
            optionD: "5,000 years",
            correctOption: "C",
            explanation: "Some coral colonies can live for thousands of years, making them among the longest-living organisms on Earth.",
            sourceTitle: "Coral Longevity",
            options: [
              { id: 'A', text: '100 years' },
              { id: 'B', text: '500 years' },
              { id: 'C', text: '1,000 years' },
              { id: 'D', text: '5,000 years' }
            ]
          },
          {
            id: 9,
            question: "What do coral reefs protect coastlines from?",
            optionA: "Marine pollution",
            optionB: "Storm waves and erosion",
            optionC: "Overfishing",
            optionD: "Ocean acidification",
            correctOption: "B",
            explanation: "Coral reefs act as natural barriers, protecting coastlines from storm waves and erosion.",
            sourceTitle: "Reef Protection Services",
            options: [
              { id: 'A', text: 'Marine pollution' },
              { id: 'B', text: 'Storm waves and erosion' },
              { id: 'C', text: 'Overfishing' },
              { id: 'D', text: 'Ocean acidification' }
            ]
          },
          {
            id: 10,
            question: "What percentage of the world's coral reefs are at risk?",
            optionA: "25%",
            optionB: "50%",
            optionC: "75%",
            optionD: "Over 90%",
            correctOption: "D",
            explanation: "Over 90% of the world's coral reefs are threatened by human activities and climate change.",
            sourceTitle: "Coral Conservation",
            options: [
              { id: 'A', text: '25%' },
              { id: 'B', text: '50%' },
              { id: 'C', text: '75%' },
              { id: 'D', text: 'Over 90%' }
            ]
          }
        ]
        
        this.questions = fallbackQuestions
        this.resetQuiz()
        this.quizStartTime = new Date()
        this.errorMessage = ''
      } finally {
        this.loadingQuestions = false
      }
    },
    
    // 随机选择指定数量的题目
    getRandomQuestions(questions, count) {
      const shuffled = [...questions].sort(() => 0.5 - Math.random())
      return shuffled.slice(0, count)
    },

    // 重置测验
    resetQuiz() {
      this.currentQuestionIndex = 0
      this.selectedOption = null
      this.showAnswerResult = false
      this.quizScore = 0
      this.quizCompleted = false
      this.scoreResult = null
      this.correctAnswers = 0
      this.wrongAnswers = 0
      this.totalQuestions = this.questions.length
      this.medalInfo = null
      this.showMedal = false
      this.showCopyNotification = false
    },

    // 选择选项
    selectOption(optionId) {
      this.selectedOption = optionId
    },

    // 提交答案
    submitAnswer() {
      if (this.selectedOption === null) return

      const currentQ = this.currentQuestion
      
      let isCorrect = false
      
        // 普通选择题：比较选项ID与正确答案
        const selectedAnswer = String(this.selectedOption).trim().toUpperCase()
        const correctAnswer = String(currentQ.correctOption || currentQ.correctAnswer).trim().toUpperCase()
        isCorrect = selectedAnswer === correctAnswer
      
      this.isAnswerCorrect = isCorrect

      if (this.isAnswerCorrect) {
        this.quizScore++
        this.correctAnswers++
      } else {
        this.wrongAnswers++
      }

      this.showAnswerResult = true
    },

    // 下一题
    nextQuestion() {
      console.log('nextQuestion: currentIndex=', this.currentQuestionIndex, 'totalQuestions=', this.questions.length)
      if (this.currentQuestionIndex < this.questions.length - 1) {
        this.currentQuestionIndex++
        this.selectedOption = null
        this.showAnswerResult = false
        console.log('Moving to question', this.currentQuestionIndex + 1)
      } else {
        console.log('Calling completeQuiz()')
        this.completeQuiz()
      }
    },

    // 完成测验
    async completeQuiz() {
      this.quizEndTime = new Date()
      this.showAnswerResult = false
      this.quizCompleted = true
      this.totalQuestions = this.questions.length

      // 计算奖章
      this.medalInfo = this.calculateMedal(this.correctAnswers, this.totalQuestions)
      this.showMedal = true
    },

    // 计算奖章
    calculateMedal(correctCount, totalCount) {
      if (correctCount < 3) {
        return {
          type: 'participation',
          title: 'Thank You for Participating',
          message: 'Thank you for participating in the CoralKita knowledge quiz! Keep learning and you\'ll get even better!',
          icon: '🌊',
          color: '#6c757d'
        }
      } else if (correctCount >= 3 && correctCount <= 5) {
        return {
          type: 'bronze',
          title: 'Bronze Medal',
          message: 'Congratulations on earning the Bronze Medal! You have gained basic knowledge about coral reefs!',
          icon: '🥉',
          image: require('@/assets/bronze.png'),
          color: '#CD7F32'
        }
      } else if (correctCount >= 6 && correctCount <= 9) {
        return {
          type: 'silver',
          title: 'Silver Medal',
          message: 'Congratulations on earning the Silver Medal! You have performed excellently in coral reef conservation!',
          icon: '🥈',
          image: require('@/assets/sliver.png'),
          color: '#C0C0C0'
        }
      } else if (correctCount === 10) {
        return {
          type: 'gold',
          title: 'Gold Medal',
          message: 'Congratulations on earning the Gold Medal! You are a true coral reef conservation expert!',
          icon: '🥇',
          image: require('@/assets/gold.png'),
          color: '#FFD700'
        }
      }
    },

    // 重新开始测验
    restartQuiz() {
      this.loadRandomQuestions()
    },

    // 分享功能方法
    shareToTwitter() {
      const shareText = `🎉 I just earned a ${this.medalInfo.title} on CoralKita Quiz! 🐠\n\nScore: ${this.correctAnswers}/${this.totalQuestions} (${Math.round((this.correctAnswers / this.totalQuestions) * 100)}%)\n\nTest your coral reef knowledge too! 🌊`;
      const shareUrl = this.generateShareUrl();
      const twitterUrl = `https://twitter.com/intent/tweet?text=${encodeURIComponent(shareText)}&url=${encodeURIComponent(shareUrl)}`;
      window.open(twitterUrl, '_blank', 'width=550,height=420');
    },

    shareToFacebook() {
      const shareUrl = this.generateShareUrl();
      const facebookUrl = `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(shareUrl)}`;
      window.open(facebookUrl, '_blank', 'width=580,height=400');
    },

    async copyShareLink() {
      const shareUrl = this.generateShareUrl();
      const shareText = `🎉 I just earned a ${this.medalInfo.title} on CoralKita Quiz! 🐠\n\nScore: ${this.correctAnswers}/${this.totalQuestions} (${Math.round((this.correctAnswers / this.totalQuestions) * 100)}%)\n\nTest your coral reef knowledge too! 🌊\n\n${shareUrl}`;
      
      try {
        await navigator.clipboard.writeText(shareText);
        this.showCopyNotification = true;
        setTimeout(() => {
          this.showCopyNotification = false;
        }, 3000);
      } catch (err) {
        // Fallback for older browsers
        const textArea = document.createElement('textarea');
        textArea.value = shareText;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand('copy');
        document.body.removeChild(textArea);
        
        this.showCopyNotification = true;
        setTimeout(() => {
          this.showCopyNotification = false;
        }, 3000);
      }
    },

    downloadBadge() {
      // Create a canvas to generate badge image
      const canvas = document.createElement('canvas');
      const ctx = canvas.getContext('2d');
      
      // Set canvas size
      canvas.width = 400;
      canvas.height = 300;
      
      // Background gradient
      const gradient = ctx.createLinearGradient(0, 0, 400, 300);
      gradient.addColorStop(0, 'rgba(26, 29, 37, 0.9)');
      gradient.addColorStop(1, 'rgba(1, 162, 235, 0.8)');
      ctx.fillStyle = gradient;
      ctx.fillRect(0, 0, 400, 300);
      
      // Border
      ctx.strokeStyle = 'rgba(255, 255, 255, 0.3)';
      ctx.lineWidth = 2;
      ctx.strokeRect(10, 10, 380, 280);
      
      // Title
      ctx.fillStyle = 'white';
      ctx.font = 'bold 24px Arial';
      ctx.textAlign = 'center';
      ctx.fillText('CoralKita Quiz Achievement', 200, 50);
      
      // Medal icon (using emoji)
      ctx.font = '60px Arial';
      ctx.fillText(this.medalInfo.icon, 200, 120);
      
      // Medal title
      ctx.font = 'bold 20px Arial';
      ctx.fillText(this.medalInfo.title, 200, 160);
      
      // Score
      ctx.font = '16px Arial';
      ctx.fillText(`Score: ${this.correctAnswers}/${this.totalQuestions} (${Math.round((this.correctAnswers / this.totalQuestions) * 100)}%)`, 200, 190);
      
      // Date
      const date = new Date().toLocaleDateString();
      ctx.font = '14px Arial';
      ctx.fillText(`Achieved on ${date}`, 200, 220);
      
      // CoralKita branding
      ctx.font = '12px Arial';
      ctx.fillStyle = 'rgba(255, 255, 255, 0.7)';
      ctx.fillText('CoralKita - Learn about coral reefs', 200, 260);
      
      // Download the image
      const link = document.createElement('a');
      link.download = `CoralKita-${this.medalInfo.type}-badge-${Date.now()}.png`;
      link.href = canvas.toDataURL();
      link.click();
    },

    generateShareUrl() {
      // Generate a share URL with achievement info
      const baseUrl = window.location.origin;
      const params = new URLSearchParams({
        achievement: this.medalInfo.type,
        score: `${this.correctAnswers}/${this.totalQuestions}`,
        accuracy: Math.round((this.correctAnswers / this.totalQuestions) * 100)
      });
      return `${baseUrl}/quiz?${params.toString()}`;
    },

    // 获取正确答案文本
    getCorrectAnswerText() {
      if (!this.currentQuestion) return ''
      
        // 普通选择题：通过选项ID查找选项文本
        const correctAnswer = this.currentQuestion.correctOption || this.currentQuestion.correctAnswer
        const correctOption = this.currentQuestion.options.find(opt => opt.id === correctAnswer)
        return correctOption ? correctOption.text : ''
    },

    // Start single-choice quiz
    startSingleChoiceQuiz() {
      this.loadRandomQuestions()
    },


  }
}
</script>

<style scoped>
.quiz-page {
  min-height: 100vh;
  background-image: url('@/assets/ed_interface.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  font-family: 'Arial', sans-serif;
}

.quiz-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 40px;
  background: linear-gradient(90deg, rgba(26, 29, 37, 0.9) 0%, rgba(1, 162, 235, 0.9) 100%);
  backdrop-filter: blur(15px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.btn-back {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s ease;
}

.btn-back:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateX(-2px);
}

.back-icon {
  font-size: 18px;
}

.header-center h1 {
  color: white;
  margin: 0;
  font-size: 28px;
  font-weight: 600;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  color: white;
}

.user-name {
  font-size: 16px;
  font-weight: 600;
}

.user-level {
  font-size: 14px;
  opacity: 0.8;
}

.quiz-content {
  padding: 40px;
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 40px;
}

/* 视频区域样式 */
.video-section {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.video-section h2 {
  color: white;
  margin: 0 0 20px 0;
  font-size: 24px;
  text-align: center;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.video-carousel {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}

.nav-arrow {
  width: 50px;
  height: 50px;
  border: none;
  background: #667eea;
  color: white;
  border-radius: 50%;
  cursor: pointer;
  font-size: 20px;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.nav-arrow:hover:not(:disabled) {
  background: #5a6fd8;
  transform: scale(1.1);
}

.nav-arrow:disabled {
  background: #ccc;
  cursor: not-allowed;
  opacity: 0.5;
}

.video-container {
  flex: 1;
  min-height: 250px;
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.video-item {
  cursor: pointer;
  transition: transform 0.3s ease;
  border-radius: 10px;
  overflow: hidden;
  background: white;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.video-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.video-thumbnail {
  position: relative;
  width: 100%;
  height: 150px;
  overflow: hidden;
}

.video-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.video-item:hover .video-thumbnail img {
  transform: scale(1.05);
}

.play-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.video-item:hover .play-overlay {
  opacity: 1;
}

.play-button {
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.play-icon {
  font-size: 24px;
  color: #667eea;
  margin-left: 3px;
}

.video-title {
  padding: 15px;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  text-align: center;
}

.video-indicators {
  display: flex;
  justify-content: center;
  gap: 8px;
}

.indicator {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #ddd;
  cursor: pointer;
  transition: background 0.3s ease;
}

.indicator.active {
  background: #667eea;
}

/* 测验区域样式 */
.quiz-section {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.quiz-header-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.quiz-header-info h2 {
  color: white;
  margin: 0;
  font-size: 24px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.quiz-type-selection {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
  justify-content: center;
}

.single-quiz-card {
  max-width: 500px;
  margin: 0 auto;
}

.quiz-type-card {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 15px;
  padding: 25px;
  text-align: center;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.quiz-type-card:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.8);
  transform: translateY(-3px);
}

.quiz-type-card h3 {
  color: white;
  margin-bottom: 10px;
  font-size: 20px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.quiz-type-card p {
  color: white;
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 25px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

.quiz-type-buttons {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.btn-quiz-type {
  padding: 12px 25px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.btn-quiz-type:hover {
  background: #5a6fd8;
  transform: translateY(-2px);
}

.quiz-active-section {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.quiz-progress {
  text-align: right;
  margin-bottom: 30px;
}

.progress-text {
  font-size: 14px;
  color: white;
  margin-bottom: 8px;
  display: block;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
  font-weight: 500;
}

.progress-bar {
  width: 200px;
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: #667eea;
  transition: width 0.3s ease;
}

.question-container {
  min-height: 300px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-state h3 {
  color: white;
  margin-bottom: 10px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
  font-size: 24px;
  font-weight: 600;
}

.empty-state p {
  color: white;
  margin-bottom: 30px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
  font-size: 16px;
  opacity: 0.9;
}

.btn-start-quiz {
  padding: 15px 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.btn-start-quiz:hover {
  background: #5a6fd8;
  transform: translateY(-2px);
}

.question-display {
  max-width: 800px;
  margin: 0 auto;
}

/* 图片分类题目样式 */
.image-question {
  text-align: center;
  margin-bottom: 30px;
}

.question-image-container {
  width: 100%;
  max-width: 400px;
  margin: 0 auto 20px;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.question-image {
  width: 100%;
  height: auto;
  display: block;
}

.question-text {
  font-size: 22px;
  color: white;
  margin-bottom: 20px;
  line-height: 1.7;
  text-align: center;
  text-shadow: 0 2px 6px rgba(0, 0, 0, 0.9);
  font-weight: 700;
  background: rgba(0, 0, 0, 0.3);
  padding: 15px 20px;
  border-radius: 10px;
  backdrop-filter: blur(5px);
}

.question-description {
  font-size: 16px;
  color: white;
  margin-bottom: 30px;
  line-height: 1.5;
  text-align: center;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
  opacity: 0.9;
}

.options {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 30px;
}

/* 图片分类题目只有两个选项时的特殊样式 */
.image-question .options {
  flex-direction: row;
  justify-content: center;
  gap: 30px;
}

.image-question .option {
  min-width: 150px;
  justify-content: center;
}

.option {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  background: rgba(255, 255, 255, 0.1);
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.option:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.8);
  transform: translateY(-2px);
}

.option.selected {
  background: rgba(102, 126, 234, 0.3);
  border-color: #667eea;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.option.disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.option input[type="radio"] {
  margin-right: 15px;
  accent-color: #667eea;
}

.option-text {
  font-size: 17px;
  color: white;
  flex: 1;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.9);
  font-weight: 600;
  line-height: 1.5;
}

.question-actions {
  text-align: center;
}

.btn-submit-answer {
  padding: 15px 40px;
  background: linear-gradient(135deg, #4882a1ff, #32507bff);
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 15px;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.btn-submit-answer:hover:not(:disabled) {
  background: #5a6fd8;
  transform: translateY(-2px);
}

.btn-submit-answer:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.login-hint {
  color: white;
  font-size: 14px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
  opacity: 0.9;
}

.login-link {
  color: #667eea;
  text-decoration: none;
}

.login-link:hover {
  text-decoration: underline;
}

/* 答案结果样式 */
.answer-result {
  text-align: center;
  padding: 40px 20px;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.result-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  font-weight: bold;
  margin: 0 auto 20px;
  color: white;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.result-icon.correct {
  background: #28a745;
}

.result-icon.incorrect {
  background: #dc3545;
}

.result-text {
  font-size: 28px;
  margin-bottom: 20px;
  color: white;
  font-weight: 700;
  text-shadow: 0 2px 6px rgba(0, 0, 0, 0.5);
}

.result-explanation {
  text-align: left;
  margin: 20px auto 30px;
  max-width: 600px;
  background: rgba(255, 255, 255, 0.15);
  padding: 25px;
  border-radius: 15px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(15px);
}

.correct-answer {
  font-size: 18px;
  color: white;
  margin-bottom: 15px;
  font-weight: 700;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.7);
  line-height: 1.5;
}

.explanation-text h4 {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 10px;
  font-weight: 600;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

.explanation-text p {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.95);
  line-height: 1.7;
  margin: 0;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  font-weight: 500;
}

.btn-next-question {
  padding: 15px 30px;
  background: linear-gradient(135deg, #4882a1ff, #32507bff);
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.btn-next-question:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a5acd 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

/* 测验完成样式 */
.quiz-completed {
  text-align: center;
  padding: 40px 20px;
}

.completion-icon {
  font-size: 60px;
  margin-bottom: 20px;
}

.quiz-completed h3 {
  font-size: 28px;
  color: #333;
  margin-bottom: 30px;
}

/* 奖章显示样式 */
.medal-display {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 40px;
  margin-bottom: 30px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.2);
  text-align: center;
}

.medal-icon {
  font-size: 80px;
  margin-bottom: 20px;
  animation: bounceIn 0.6s ease-out;
  display: flex;
  justify-content: center;
  align-items: center;
}

.medal-image {
  width: 80px;
  height: 80px;
  object-fit: contain;
  animation: bounceIn 0.6s ease-out;
}

.medal-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 15px;
  color: white;
  text-shadow: 0 2px 6px rgba(0, 0, 0, 0.5);
}

.medal-message {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.95);
  line-height: 1.6;
  margin: 0;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.5);
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

/* 统计信息样式 */
.quiz-statistics {
  margin-bottom: 30px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 25px;
}

.stat-card {
  background: white;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 15px;
  transition: transform 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-card.correct {
  border-left: 4px solid #28a745;
}

.stat-card.wrong {
  border-left: 4px solid #dc3545;
}

.stat-card.total {
  border-left: 4px solid #667eea;
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
}

.stat-card.correct .stat-icon {
  background: rgba(40, 167, 69, 0.1);
  color: #28a745;
}

.stat-card.wrong .stat-icon {
  background: rgba(220, 53, 69, 0.1);
  color: #dc3545;
}

.stat-card.total .stat-icon {
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
}

.stat-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-top: 2px;
}

.score-summary {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border-radius: 15px;
  padding: 20px;
  margin-top: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.score-summary h4 {
  color: white;
  margin-bottom: 15px;
  font-size: 18px;
  font-weight: 600;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.5);
}

.score-text, .accuracy-text {
  font-size: 16px;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  font-weight: 500;
  margin-bottom: 8px;
}

.score-number, .accuracy-number {
  font-weight: bold;
  color: #FFD700;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.7);
  font-size: 20px;
}

/* 奖励信息样式 */
.reward-info {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  border-radius: 20px;
  padding: 25px;
  margin-bottom: 30px;
  border: 1px solid rgba(102, 126, 234, 0.2);
}

.reward-info h4 {
  color: #333;
  margin-bottom: 20px;
  font-size: 20px;
}

.rewards-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 20px;
}

.reward-card {
  background: white;
  border-radius: 12px;
  padding: 15px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.reward-card.experience {
  border-left: 4px solid #ffc107;
}

.reward-card.points {
  border-left: 4px solid #17a2b8;
}

.reward-icon {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}

.reward-card.experience .reward-icon {
  background: rgba(255, 193, 7, 0.2);
}

.reward-card.points .reward-icon {
  background: rgba(23, 162, 184, 0.2);
}

.reward-details {
  display: flex;
  flex-direction: column;
}

.reward-amount {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.reward-type {
  font-size: 12px;
  color: #666;
  margin-top: 2px;
}

.level-progress {
  text-align: center;
  padding: 15px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 12px;
}

.level-info {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin: 0;
}

.level-badge {
  background: #667eea;
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
}

.experience-info {
  color: #333;
  font-size: 14px;
}

/* 登录提示样式 */
.login-prompt {
  background: rgba(255, 193, 7, 0.1);
  border-radius: 15px;
  padding: 20px;
  margin-bottom: 30px;
  border: 1px solid rgba(255, 193, 7, 0.3);
}

.prompt-text {
  color: #333;
  margin-bottom: 10px;
  font-size: 16px;
}

.login-suggestion {
  display: inline-block;
  background: #ffc107;
  color: #333;
  padding: 8px 20px;
  border-radius: 20px;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s ease;
}

.login-suggestion:hover {
  background: #e0a800;
  transform: translateY(-1px);
  text-decoration: none;
}

.completion-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.btn-restart,
.btn-back-home {
  padding: 15px 30px;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-restart {
  background: #667eea;
  color: white;
}

.btn-restart:hover {
  background: #5a6fd8;
  transform: translateY(-2px);
}

.btn-back-home {
  background: #6c757d;
  color: white;
}

.btn-back-home:hover {
  background: #5a6268;
  transform: translateY(-2px);
}

/* 分享功能样式 */
.share-section {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0.05) 100%);
  border-radius: 20px;
  padding: 30px;
  margin: 30px 0;
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  text-align: center;
}

.share-header h3 {
  color: white;
  font-size: 24px;
  margin-bottom: 10px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.share-header p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 16px;
  margin-bottom: 25px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.share-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
}

.btn-share {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 20px 15px;
  border: none;
  border-radius: 15px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.btn-share:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.btn-share.twitter {
  background: linear-gradient(135deg, #1da1f2 0%, #0d8bd9 100%);
}

.btn-share.twitter:hover {
  background: linear-gradient(135deg, #0d8bd9 0%, #0a7bc4 100%);
}

.btn-share.facebook {
  background: linear-gradient(135deg, #4267b2 0%, #365899 100%);
}

.btn-share.facebook:hover {
  background: linear-gradient(135deg, #365899 0%, #2d4373 100%);
}

.btn-share.copy {
  background: linear-gradient(135deg, #28a745 0%, #20953b 100%);
}

.btn-share.copy:hover {
  background: linear-gradient(135deg, #20953b 0%, #1e7e34 100%);
}

.btn-share.download {
  background: linear-gradient(135deg, #6f42c1 0%, #5a32a3 100%);
}

.btn-share.download:hover {
  background: linear-gradient(135deg, #5a32a3 0%, #4c2a85 100%);
}

.share-icon {
  width: 24px;
  height: 24px;
  margin-bottom: 5px;
  object-fit: contain;
}

.copy-notification {
  background: linear-gradient(135deg, #28a745 0%, #20953b 100%);
  color: white;
  padding: 12px 20px;
  border-radius: 25px;
  font-weight: 600;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
  animation: slideInUp 0.3s ease, fadeOut 0.3s ease 2.7s forwards;
  margin-top: 15px;
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeOut {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .share-options {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .btn-share {
    padding: 15px 10px;
    font-size: 12px;
  }
  
  .share-icon {
    width: 20px;
    height: 20px;
  }
}

@media (max-width: 480px) {
  .share-options {
    grid-template-columns: 1fr;
  }
  
  .share-section {
    padding: 20px;
    margin: 20px 0;
  }
}

/* 视频模态框样式 */
.video-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.video-modal {
  background: white;
  border-radius: 15px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow: hidden;
}

.video-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.video-modal-header h3 {
  margin: 0;
  color: #333;
}

.btn-close-video {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-close-video:hover {
  color: #333;
}

.video-modal-content {
  padding: 20px;
}

.video-player iframe {
  border-radius: 10px;
}

.local-video-player {
  border-radius: 10px;
  background: #000;
  outline: none;
}

.local-video-player::-webkit-media-controls-panel {
  background-color: rgba(0, 0, 0, 0.8);
}

.local-video-player::-webkit-media-controls-play-button,
.local-video-player::-webkit-media-controls-pause-button,
.local-video-player::-webkit-media-controls-mute-button,
.local-video-player::-webkit-media-controls-fullscreen-button {
  filter: brightness(1.2);
}

.video-placeholder {
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
  border-radius: 10px;
  color: #666;
}

.placeholder-content {
  text-align: center;
  padding: 40px 20px;
}

.placeholder-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.placeholder-content h4 {
  color: #333;
  margin: 0 0 10px 0;
  font-size: 18px;
}

.placeholder-content p {
  color: #666;
  margin: 0 0 20px 0;
  font-size: 14px;
}

.external-link {
  display: inline-block;
  background: #667eea;
  color: white;
  padding: 10px 20px;
  border-radius: 20px;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
}

.external-link:hover {
  background: #5a6fd8;
  text-decoration: none;
  transform: translateY(-1px);
}

.video-completion {
  margin-top: 20px;
  text-align: center;
}

.btn-mark-watched {
  padding: 12px 25px;
  background: #28a745;
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.btn-mark-watched:hover {
  background: #218838;
  transform: translateY(-2px);
}

/* 加载和错误状态 */
.loading-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.no-videos,
.no-questions {
  text-align: center;
  padding: 60px 20px;
  color: white;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.error-message {
  position: fixed;
  top: 20px;
  right: 20px;
  background: #dc3545;
  color: white;
  padding: 15px 20px;
  border-radius: 10px;
  z-index: 1001;
  max-width: 400px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .quiz-header {
    padding: 15px 20px;
    flex-direction: column;
    gap: 15px;
  }

  .quiz-content {
    padding: 20px;
    gap: 30px;
  }

  .video-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .video-carousel {
    flex-direction: column;
    gap: 15px;
  }

  .nav-arrow {
    align-self: center;
  }

  .quiz-header-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .quiz-type-selection {
    grid-template-columns: 1fr;
    gap: 15px;
  }

  .question-image-container {
    max-width: 300px;
  }

  .image-question .options {
    flex-direction: column;
    gap: 15px;
  }

  .image-question .option {
    min-width: auto;
  }

  .completion-actions {
    flex-direction: column;
    align-items: center;
  }

  .stats-grid {
    grid-template-columns: 1fr;
    gap: 15px;
  }

  .rewards-grid {
    grid-template-columns: 1fr;
  }

  .level-info {
    flex-direction: column;
    gap: 10px;
  }
}

@media (max-width: 480px) {
  .video-grid {
    grid-template-columns: 1fr;
  }

  .header-center h1 {
    font-size: 20px;
  }

  .question-text {
    font-size: 18px;
  }
}
</style>
