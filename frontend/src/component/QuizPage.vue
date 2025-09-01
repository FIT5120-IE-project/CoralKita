<template>
  <div class="quiz-page">
    <!-- Page Header -->
    <div class="quiz-header">
      <div class="header-left">
        <button class="btn-back" @click="goBack">
          <i class="back-icon">←</i>
          Back
        </button>
      </div>
      <div class="header-center">
        <h1>Coral Reef Knowledge Quiz</h1>
      </div>
      <div class="header-right">
        <div v-if="isAuthenticated" class="user-info">
          <span class="user-name">{{ currentUser.name }}</span>
          <span class="user-level">Level: {{ currentUser.level || 1 }}</span>
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
        
        <!-- Quiz Type Selection -->
        <div class="quiz-type-selection">
          <div class="quiz-type-card">
            <h3>Single-choice Questions</h3>
            <p>Test your knowledge with multiple choice questions about coral reefs</p>
            <div class="quiz-type-buttons">
              <button class="btn-quiz-type" @click="startSingleChoiceQuiz">
                Start Single-choice Question
              </button>
            </div>
          </div>
          
          <div class="quiz-type-card">
            <h3>Image Classification</h3>
            <p>Identify coral species and reef types from images</p>
            <div class="quiz-type-buttons">
              <button class="btn-quiz-type" @click="startImageClassificationQuiz">
                Start Image Classification
              </button>
            </div>
          </div>
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
              <p class="result-explanation" v-if="!isAnswerCorrect && currentQuestion">
                Correct Answer: {{ getCorrectAnswerText() }}
              </p>
              <div class="result-actions">
                <button class="btn-next-question" @click="nextQuestion">
                  {{ currentQuestionIndex < questions.length - 1 ? 'Next Question' : 'Complete Quiz' }}
                </button>
              </div>
            </div>
            <div v-else-if="quizCompleted" class="quiz-completed">
              <div class="completion-icon">🎉</div>
              <h3>Quiz Completed!</h3>
              
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
                <router-link to="/education" class="login-suggestion">Login Now</router-link>
              </div>

              <div class="completion-actions">
                <button class="btn-restart" @click="restartQuiz">Restart Quiz</button>
                <button class="btn-back-home" @click="goBack">Back to Home</button>
              </div>
            </div>
                      <div v-else class="question-display">
            <!-- 图片分类题目显示 -->
            <div v-if="currentQuestion.type === 'image_classification'" class="image-question">
              <div class="question-image-container">
                <img 
                  :src="currentQuestion.imageUrl" 
                  :alt="'Coral Image ' + (currentQuestionIndex + 1)"
                  class="question-image"
                  @error="handleImageError"
                />
              </div>
              <h3 class="question-text">{{ currentQuestion.question }}</h3>
              <p v-if="currentQuestion.description" class="question-description">{{ currentQuestion.description }}</p>
            </div>
            
            <!-- 普通选择题显示 -->
            <div v-else>
              <h3 class="question-text">{{ currentQuestion.question }}</h3>
            </div>
              <div class="options">
                <label 
                  v-for="(option, optIndex) in currentQuestion.options" 
                  :key="optIndex" 
                  class="option"
                  :class="{ 
                    selected: selectedOption === option.id,
                    disabled: !isAuthenticated
                  }"
                  @click="selectOption(option.id)"
                >
                  <input 
                    type="radio" 
                    :name="'question_' + currentQuestionIndex" 
                    :value="option.id" 
                    :disabled="!isAuthenticated"
                    v-model="selectedOption"
                  />
                  <span class="option-text">{{ option.text }}</span>
                </label>
              </div>
              
              <div class="question-actions">
                <button 
                  class="btn-submit-answer" 
                  @click="submitAnswer"
                  :disabled="selectedOption === null || !isAuthenticated"
                >
                  {{ isAuthenticated ? 'Submit Answer' : 'Login Required to Answer' }}
                </button>
                <div v-if="!isAuthenticated" class="login-hint">
                  <p>You need to <router-link to="/education" class="login-link">login</router-link> to participate in the quiz and earn rewards</p>
                </div>
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
    await this.loadVideoSources()
    await this.loadSourceTitles()
  },
  methods: {
    // Go back to previous page
    goBack() {
      this.$router.push('/education')
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

    // Load random questions
    async loadRandomQuestions() {
      console.log('loadRandomQuestions called, sourceTitles:', this.sourceTitles)
      
      if (this.sourceTitles.length === 0) {
        this.errorMessage = 'No quiz sources available, please refresh the page and try again'
        console.error('No source titles available')
        return
      }

      this.loadingQuestions = true
      this.errorMessage = ''
      try {
        // Randomly select a source title
        const randomTitle = this.sourceTitles[Math.floor(Math.random() * this.sourceTitles.length)]
        console.log('Loading questions for source:', randomTitle)
        
        const response = await axios.get('/quiz/questions', {
          params: { sourceTitle: randomTitle }
        })

        console.log('Questions response:', response.data)

        if (response.data.code === 1 && response.data.data) {
          console.log('Raw questions data:', response.data.data)
          this.questions = response.data.data.map(q => {
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
          console.log('Processed questions:', this.questions)
          this.resetQuiz()
          this.quizStartTime = new Date()
        } else {
          this.errorMessage = '未能获取到题目数据：' + (response.data.msg || '后端返回错误')
          console.error('Backend returned error:', response.data)
        }
      } catch (error) {
        console.error('加载题目失败:', error)
        console.error('Error details:', error.response?.data)
        
        // 使用备用题目数据，确保功能可用
        this.questions = [
          {
            id: 1,
            question: "What is a coral reef primarily made of?",
            optionA: "Rocks",
            optionB: "Living coral polyps",
            optionC: "Seaweed", 
            optionD: "Sand",
            correctOption: "B",
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
            sourceTitle: "Coral Reefs 101 | National Geographic",
            options: [
              { id: 'A', text: 'Zooxanthellae (algae) within their tissues' },
              { id: 'B', text: 'They grow faster in cooler waters' },
              { id: 'C', text: 'They bleach (evict algae)' },
              { id: 'D', text: 'They move to cooler waters' }
            ]
          }
        ]
        
        this.resetQuiz()
        this.quizStartTime = new Date()
        this.errorMessage = ''
      } finally {
        this.loadingQuestions = false
      }
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
    },

    // 选择选项
    selectOption(optionId) {
      if (!this.isAuthenticated) {
        this.errorMessage = '请先登录才能参与答题'
        setTimeout(() => {
          this.errorMessage = ''
        }, 3000)
        return
      }
      this.selectedOption = optionId
    },

    // 提交答案
    submitAnswer() {
      if (!this.isAuthenticated) {
        this.errorMessage = '请先登录才能参与答题'
        return
      }

      if (this.selectedOption === null) return

      const currentQ = this.currentQuestion
      
      let isCorrect = false
      
      if (currentQ.type === 'image_classification') {
        // 图片分类题目：直接比较选项文本与正确答案
        const selectedOption = currentQ.options.find(opt => opt.id === this.selectedOption)
        const selectedText = selectedOption ? selectedOption.text : ''
        isCorrect = selectedText.toLowerCase() === currentQ.correctAnswer
      } else {
        // 普通选择题：比较选项ID与正确答案
        const selectedAnswer = String(this.selectedOption).trim().toUpperCase()
        const correctAnswer = String(currentQ.correctOption || currentQ.correctAnswer).trim().toUpperCase()
        isCorrect = selectedAnswer === correctAnswer
      }
      
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
      this.showAnswerResult = false  // 关键修复：隐藏答案结果界面
      this.quizCompleted = true

      if (this.isAuthenticated) {
        try {
          const response = await axios.post('/quiz/submit-score', {
            userName: this.currentUser.name,
            score: this.quizScore
          })

          if (response.data.code === 1) {
            this.scoreResult = response.data.data
            
            // 更新本地用户信息
            this.$store.commit('UPDATE_USER_STATS', {
              level: this.scoreResult.level,
              experience: this.scoreResult.experience,
              points: this.scoreResult.points
            })
          } else {
            this.errorMessage = '提交得分失败：' + (response.data.msg || '未知错误')
          }
        } catch (error) {
          this.errorMessage = '提交得分失败：' + (error.response?.data?.msg || error.message)
        }
      }
    },

    // 重新开始测验
    restartQuiz() {
      this.loadRandomQuestions()
    },

    // 获取正确答案文本
    getCorrectAnswerText() {
      if (!this.currentQuestion) return ''
      
      if (this.currentQuestion.type === 'image_classification') {
        // 图片分类题目：返回正确答案的英文选项文本
        return this.currentQuestion.correctAnswer === 'health' ? 'Health' : 'Bleach'
      } else {
        // 普通选择题：通过选项ID查找选项文本
        const correctAnswer = this.currentQuestion.correctOption || this.currentQuestion.correctAnswer
        const correctOption = this.currentQuestion.options.find(opt => opt.id === correctAnswer)
        return correctOption ? correctOption.text : ''
      }
    },

    // Start single-choice quiz
    startSingleChoiceQuiz() {
      this.loadRandomQuestions()
    },

    // Start image classification quiz
    startImageClassificationQuiz() {
      if (!this.isAuthenticated) {
        this.errorMessage = '请先登录才能参与图片分类测验'
        setTimeout(() => {
          this.errorMessage = ''
        }, 3000)
        return
      }
      
      this.loadImageClassificationQuestions()
    },

    // Load image classification questions
    async loadImageClassificationQuestions() {
      this.loadingQuestions = true
      this.errorMessage = ''
      
      try {
        const response = await axios.get('/quiz/coral-pictures', {
          params: { userName: this.currentUser.name }
        })

        if (response.data.code === 1 && response.data.data) {
          // 转换图片分类题目格式
          this.questions = response.data.data.map((picture, index) => ({
            id: index + 1,
            type: 'image_classification',
            imageUrl: picture.pictureUrl,
            question: `Please identify the health status of the coral in this image:`,
            description: `Choose "Health" for healthy coral, choose "Bleach" for bleached coral`,
            correctAnswer: picture.answer,
            options: [
              { id: 'A', text: 'Health' },
              { id: 'B', text: 'Bleach' }
            ]
          }))
          
          this.resetQuiz()
          this.quizStartTime = new Date()
        } else {
          this.errorMessage = '获取图片分类题目失败：' + (response.data.msg || '未知错误')
        }
      } catch (error) {
        console.error('加载图片分类题目失败:', error)
        this.errorMessage = '加载图片分类题目失败：' + (error.response?.data?.msg || error.message)
      } finally {
        this.loadingQuestions = false
      }
    }
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
  font-size: 20px;
  color: white;
  margin-bottom: 15px;
  line-height: 1.6;
  text-align: center;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
  font-weight: 600;
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
  font-size: 16px;
  color: white;
  flex: 1;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
  font-weight: 500;
}

.question-actions {
  text-align: center;
}

.btn-submit-answer {
  padding: 15px 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
}

.result-icon.correct {
  background: #28a745;
}

.result-icon.incorrect {
  background: #dc3545;
}

.result-text {
  font-size: 24px;
  margin-bottom: 15px;
  color: #333;
}

.result-explanation {
  font-size: 16px;
  color: #666;
  margin-bottom: 30px;
}

.btn-next-question {
  padding: 15px 30px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-next-question:hover {
  background: #5a6fd8;
  transform: translateY(-2px);
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
  background: rgba(102, 126, 234, 0.1);
  border-radius: 15px;
  padding: 20px;
  margin-top: 20px;
}

.score-summary h4 {
  color: #333;
  margin-bottom: 15px;
  font-size: 18px;
}

.score-text, .accuracy-text {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.score-number, .accuracy-number {
  font-weight: bold;
  color: #667eea;
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
