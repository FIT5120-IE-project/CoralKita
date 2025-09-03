<template>
  <div class="education-container">
    <!-- Top Navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <!-- Left side logo -->
        <img src="@/assets/icon.png" alt="logo" class="nav-logo" @click="goToHome" />
        <h1 class="logo" @click="goToHome">CoralKita</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <span class="nav-item" @click="goToMap">Map</span>
          <span class="nav-item" @click="goToTrends">Trends</span>
          <span class="nav-item active">Education</span>
          <span class="nav-item" @click="goToGovernment">Government</span>
        </div>
      </div>
    </div>

    <!-- Header -->
    <div class="education-header">
      <h1>Education & Responsible Tourism Hub</h1>
      <p>Learn about coral reefs and become a responsible traveler</p>
    </div>

    <!-- Main Content Area -->
    <div class="education-content">
      <!-- Learning Interface -->
      <div class="learning-interface">
        <!-- Welcome Info Card -->
        <div class="user-info-card">
          <div class="user-avatar">
            <img src="@/assets/icon.png" alt="CoralKita Avatar" />
          </div>
          <div class="user-details">
            <div>
              <h3>Welcome to CoralKita Knowledge Q&A Interface</h3>
              <p>Explore coral reef knowledge, learn marine conservation, and participate in sustainable tourism</p>
              </div>
          </div>
        </div>
        

        
        


        <!-- Features Section -->
        <div class="features-grid">
                    <!-- Coral Reef Learning Video -->
          <div class="feature-card quiz-card">
            <div class="feature-icon">
              <img src="@/assets/icons/icon_video.png" alt="Video Icon" />
            </div>
            <h3>Coral Reef Learning Video</h3>
            <p>Test your knowledge about coral reefs and learn marine ecology</p>
            <button class="btn-feature" @click="goToQuiz">Start Quiz</button>
          </div>

                      <!-- Travel Checklist -->
            <div class="feature-card progress-card">
              <div class="feature-icon">
                <img src="@/assets/icons/icon_list.png" alt="List Icon" />
              </div>
              <h3>Travel Checklist</h3>
              <p>Help users clearly understand and complete the key steps of responsible travel</p>
              <button class="btn-feature" @click="openTravelChecklist">Fill Checklist</button>
            </div>
        </div>
      </div>
    </div>

    <!-- 验证界面 -->
    <div v-if="showVerification" class="verification-overlay">
      <div class="verification-modal">
        <div class="verification-header">
          <h2>Coral Reef Health Verification</h2>
          <p>Please identify whether the coral reefs in the following 6 images are healthy</p>
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
            </div>
            </div>
            

            </div>
            </div>

    <!-- Error Message -->
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
      </div>
    


    <!-- 测验界面 -->
    <div v-if="showQuiz" class="quiz-overlay">
      <div class="quiz-modal">
        <div class="quiz-header">
          <h2>Coral Reef Knowledge Quiz</h2>
          <button class="btn-close-quiz" @click="closeQuiz">×</button>
        </div>
        
        <div class="quiz-content">
          <!-- 视频学习区域 -->
          <div class="video-section">
            <h3>Video Learning</h3>
            <div class="video-carousel">
              <button class="nav-arrow left" @click="prevVideo" :disabled="currentVideoIndex === 0">
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
                    @click="playVideo(video.id)"
                  >
                    <div class="video-thumbnail">
                      <img :src="video.thumbnail" :alt="video.title" />
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
              
              <button class="nav-arrow right" @click="nextVideo" :disabled="currentVideoIndex >= videoSources.length - 4">
                &#10095;
              </button>
            </div>
            
            <!-- 视频指示器 -->
            <div class="video-indicators">
              <span 
                v-for="(video, index) in videoSources" 
                :key="index"
                class="indicator"
                :class="{ active: index >= currentVideoIndex && index < currentVideoIndex + 4 }"
                @click="goToVideo(index)"
              ></span>
            </div>
          </div>

          <!-- 测验题目区域 -->
          <div class="quiz-section">
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
                <p>No questions available</p>
              </div>
              <div v-else-if="showAnswerResult" class="answer-result">
                <div class="result-icon" :class="isAnswerCorrect ? 'correct' : 'incorrect'">
                  {{ isAnswerCorrect ? '✓' : '✗' }}
                </div>
                <h3 class="result-text">{{ isAnswerCorrect ? 'Correct Answer!' : 'Wrong Answer!' }}</h3>
                <p class="result-explanation" v-if="!isAnswerCorrect">
                  Correct Answer: {{ getCorrectAnswerText() }}
                </p>
                <button class="btn-next-question" @click="nextQuestion">
                  {{ currentQuestionIndex < questions.length - 1 ? 'Next Question' : 'Complete Quiz' }}
                </button>
              </div>
              <div v-else class="question-display">
                <h3 class="question-text">{{ currentQuestion.question }}</h3>
                <div class="options">
                  <label 
                    v-for="(option, optIndex) in currentQuestion.options" 
                    :key="optIndex" 
                    class="option"
                    :class="{ selected: selectedOption === option.id }"
                  >
                    <input 
                      type="radio" 
                      :name="'currentQuestion'" 
                      :value="option.id" 
                      @change="selectOption(option.id)"
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
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import axios from 'axios'

export default {
  name: 'Education',
  data() {
    return {
      // 验证系统相关
      showVerification: false, // 控制验证界面的显示 - 只在页面刷新时显示
      verificationImages: [], // 验证图片列表
      loadingVerification: false, // 加载验证图片状态
      selectedImages: [], // 用户选择的图片ID列表（最多4个）
      maxSelections: 4, // 最大选择次数
      verificationCompleted: false, // 验证是否完成
      verificationResult: null, // 验证结果
      
      // 原有的quiz相关数据（现在主要用于测验模态框）
      showQuiz: false, // 控制测验模态框的显示
      videoSources: [], // 视频源列表
      loadingVideo: false, // 加载视频源时的状态
      questions: [], // 测验题目
      loadingQuestions: false, // 加载题目时的状态
      selectedOptions: [], // 用户选择的选项
      quizScore: 0, // 测验得分
      quizCompleted: false, // 测验是否完成
      currentVideoIndex: 0, // 当前播放的视频索引
      currentQuestionIndex: 0, // 当前显示的题目索引
      selectedOption: null, // 当前选中的选项
      showAnswerResult: false, // 是否显示答案结果
      isAnswerCorrect: false, // 答案是否正确
      answerExplanation: '', // 答案解释
      quizStartTime: null, // 测验开始时间
      quizEndTime: null, // 测验结束时间
      quizDuration: 0 // 测验持续时间 (秒)
    }
  },
  computed: {
    ...mapGetters(['isAuthenticated', 'currentUser']),
    
    // 获取当前可见的视频（显示4个）
    visibleVideos() {
      const startIndex = this.currentVideoIndex;
      const endIndex = Math.min(this.videoSources.length, startIndex + 4);
      return this.videoSources.slice(startIndex, endIndex);
    },
    
    // 当前显示的题目
    currentQuestion() {
      return this.questions[this.currentQuestionIndex] || null;
    },
    
    // 进度百分比
    progressPercentage() {
      if (this.questions.length === 0) return 0;
      return ((this.currentQuestionIndex + 1) / this.questions.length) * 100;
    }
  },
  methods: {
    
    goToQuiz() {
      // Navigate to independent quiz page
      localStorage.setItem('hasNavigatedToEducation', 'true');
      this.$router.push('/quiz');
    },
    
    

    openTravelChecklist() {
      localStorage.setItem('hasNavigatedToEducation', 'true');
      this.$router.push('/travel-checklist');
    },

    // Navigation methods
    goToHome() {
      // Navigate to home page - 页面刷新，不设置localStorage
      window.location.href = '/';
    },

    goToMap() {
      console.log('Navigate to Map page');
      localStorage.setItem('hasNavigatedToEducation', 'true');
      this.$router.push('/map');
    },

    goToTrends() {
      console.log('Navigate to Trends page');
      localStorage.setItem('hasNavigatedToEducation', 'true');
      this.$router.push('/trends');
    },

    goToGovernment() {
      console.log('Navigate to Government page');
      localStorage.setItem('hasNavigatedToEducation', 'true');
      this.$router.push('/government');
    },

    async loadVideoSources() {
      this.loadingVideo = true;
      try {
        // 使用后端API获取珊瑚礁图片作为视频源
        const response = await fetch(`/quiz/coral-pictures?userName=${this.currentUser?.name || 'guest'}`);
        if (response.ok) {
          const data = await response.json();
          if (data.code === 1) {
            // 将珊瑚礁图片转换为视频源格式
            this.videoSources = data.data.map((pic, index) => ({
              id: index + 1,
              title: `珊瑚礁知识视频 ${index + 1}`,
              thumbnail: pic.signedUrl,
              description: '了解珊瑚礁生态系统和保护知识'
            }));
          } else {
            throw new Error(data.msg || '获取视频源失败');
          }
        } else {
          throw new Error('获取视频源失败');
        }
      } catch (error) {
        console.error('Error loading video sources:', error);
        // 如果API失败，使用默认的示例视频源
        this.videoSources = [
          {
            id: 1,
            title: '珊瑚礁生态系统介绍',
            thumbnail: 'https://via.placeholder.com/300x200/4facfe/ffffff?text=珊瑚礁视频1',
            description: '了解珊瑚礁的基本构成和生态功能'
          },
          {
            id: 2,
            title: '珊瑚礁保护方法',
            thumbnail: 'https://via.placeholder.com/300x200/00f2fe/ffffff?text=珊瑚礁视频2',
            description: '学习如何保护珊瑚礁生态系统'
          },
          {
            id: 3,
            title: '海洋生物多样性',
            thumbnail: 'https://via.placeholder.com/300x200/667eea/ffffff?text=珊瑚礁视频3',
            description: '探索珊瑚礁中的海洋生物'
          },
          {
            id: 4,
            title: '气候变化对珊瑚礁的影响',
            thumbnail: 'https://via.placeholder.com/300x200/764ba2/ffffff?text=珊瑚礁视频4',
            description: '了解气候变化如何影响珊瑚礁'
          },
          {
            id: 5,
            title: '可持续旅游实践',
            thumbnail: 'https://via.placeholder.com/300x200/4facfe/ffffff?text=珊瑚礁视频5',
            description: '学习如何在旅游中保护珊瑚礁'
          }
        ];
      } finally {
        this.loadingVideo = false;
      }
    },

    async loadQuestions() {
      this.loadingQuestions = true;
      try {
        // 使用后端API获取测验题目
        const response = await fetch('/quiz/questions');
        if (response.ok) {
          const data = await response.json();
          if (data.code === 1) {
            // 随机选择5个题目
            const allQuestions = data.data;
            this.questions = this.getRandomQuestions(allQuestions, 5);
            this.selectedOptions = new Array(this.questions.length).fill(null);
          } else {
            throw new Error(data.msg || '获取题目失败');
          }
        } else {
          throw new Error('获取题目失败');
        }
      } catch (error) {
        console.error('Error loading quiz questions:', error);
        // 如果API失败，使用默认的示例题目
        this.questions = [
          {
            id: 1,
            question: '珊瑚礁主要由什么构成？',
            options: [
              { id: 1, text: '珊瑚虫的骨骼', isCorrect: true },
              { id: 2, text: '海藻', isCorrect: false },
              { id: 3, text: '贝壳', isCorrect: false },
              { id: 4, text: '海星', isCorrect: false }
            ]
          },
          {
            id: 2,
            question: '以下哪种行为对珊瑚礁最有害？',
            options: [
              { id: 1, text: '触摸珊瑚', isCorrect: false },
              { id: 2, text: '使用防晒霜', isCorrect: true },
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
            question: '珊瑚礁的主要威胁不包括？',
            options: [
              { id: 1, text: '海洋酸化', isCorrect: false },
              { id: 2, text: '过度捕捞', isCorrect: false },
              { id: 3, text: '海豚数量增加', isCorrect: true },
              { id: 4, text: '塑料污染', isCorrect: false }
            ]
          },
          {
            id: 5,
            question: '保护珊瑚礁最有效的方法是？',
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

    selectOption(optionId) {
      this.selectedOption = optionId;
    },

    async submitAnswer() {
      if (this.selectedOption === null) {
        alert('请选择一个答案！');
        return;
      }

      const currentQuestion = this.questions[this.currentQuestionIndex];
      const isCorrect = currentQuestion.options.find(opt => opt.id === this.selectedOption).isCorrect;
      
      this.isAnswerCorrect = isCorrect;
      this.showAnswerResult = true;

      if (isCorrect) {
        this.quizScore++;
      }

      this.answerExplanation = isCorrect ? '恭喜你，回答正确！' : `很遗憾，回答错误。正确答案是：${this.getCorrectAnswerText()}`;

      this.quizEndTime = new Date();
      this.quizDuration = (this.quizEndTime - this.quizStartTime) / 1000;

      // 延迟一段时间后自动进入下一题
      setTimeout(() => {
        this.nextQuestion();
      }, 2000); // 2秒后显示下一题
    },

    nextQuestion() {
      this.showAnswerResult = false;
      this.selectedOption = null;
      this.currentQuestionIndex++;

      if (this.currentQuestionIndex < this.questions.length) {
        // 如果还有题目，继续显示当前题目
        this.currentQuestion = this.questions[this.currentQuestionIndex];
      } else {
        // 如果没有题目了，显示完成测验
        this.quizEndTime = new Date();
        this.quizDuration = (this.quizEndTime - this.quizStartTime) / 1000;
        this.showQuiz = false; // 关闭测验模态框
        alert(`测验完成！\n您的得分：${this.quizScore}/${this.questions.length}\n用时：${this.quizDuration}秒`);

        // 更新用户信息 (模拟)
        this.$store.dispatch('updateUserStats', {
          points: this.quizScore * 10, // 假设每题10分
          experience: this.quizScore * 5, // 假设每题5经验
          level: Math.floor(this.quizScore / 5) + 1 // 假设每5题升一级
        });
      }
    },

    getCorrectAnswerText() {
      const currentQuestion = this.questions[this.currentQuestionIndex];
      const correctOption = currentQuestion.options.find(opt => opt.isCorrect);
      return correctOption ? correctOption.text : 'N/A';
    },

    goToVideo(index) {
      this.currentVideoIndex = index;
    },

    prevVideo() {
      if (this.currentVideoIndex > 0) {
        this.currentVideoIndex--;
      }
    },

    nextVideo() {
      if (this.currentVideoIndex < this.videoSources.length - 4) {
        this.currentVideoIndex++;
      }
    },

    playVideo(videoId) {
      const video = this.videoSources.find(v => v.id === videoId);
      if (video) {
        // 创建全屏视频播放器
        const videoPlayer = document.createElement('div');
        videoPlayer.className = 'fullscreen-video-player';
        videoPlayer.innerHTML = `
          <div class="video-overlay">
            <button class="close-video-btn" onclick="this.parentElement.parentElement.remove()">×</button>
            <video controls autoplay class="fullscreen-video">
              <source src="${video.videoUrl || video.thumbnail}" type="video/mp4">
              您的浏览器不支持视频播放
            </video>
            <div class="video-info">
              <h3>${video.title}</h3>
              <p>${video.description}</p>
            </div>
          </div>
        `;
        
        // 添加样式
        const style = document.createElement('style');
        style.textContent = `
          .fullscreen-video-player {
            position: fixed;
            top: 0;
            left: 0;
            width: 100vw;
            height: 100vh;
            background: rgba(0, 0, 0, 0.95);
            z-index: 9999;
            display: flex;
            justify-content: center;
            align-items: center;
          }
          .video-overlay {
            position: relative;
            width: 100%;
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
          }
          .close-video-btn {
            position: absolute;
            top: 20px;
            right: 20px;
            background: rgba(255, 255, 255, 0.2);
            border: none;
            color: white;
            font-size: 24px;
            width: 40px;
            height: 40px;
            border-radius: 50%;
            cursor: pointer;
            z-index: 10000;
          }
          .fullscreen-video {
            max-width: 90%;
            max-height: 80%;
            border-radius: 10px;
          }
          .video-info {
            position: absolute;
            bottom: 20px;
            left: 20px;
            color: white;
            background: rgba(0, 0, 0, 0.7);
            padding: 15px;
            border-radius: 10px;
            max-width: 300px;
          }
          .video-info h3 {
            margin: 0 0 10px 0;
            font-size: 18px;
          }
          .video-info p {
            margin: 0;
            font-size: 14px;
            opacity: 0.9;
          }
        `;
        
        document.head.appendChild(style);
        document.body.appendChild(videoPlayer);
        
        // 点击背景关闭视频
        videoPlayer.addEventListener('click', (e) => {
          if (e.target === videoPlayer) {
            videoPlayer.remove();
          }
        });
      }
    },

    closeQuiz() {
      this.showQuiz = false;
      this.videoSources = [];
      this.questions = [];
      this.selectedOptions = [];
      this.quizScore = 0;
      this.quizCompleted = false;
      this.currentVideoIndex = 0;
      this.currentQuestionIndex = 0;
      this.selectedOption = null;
      this.showAnswerResult = false;
      this.isAnswerCorrect = false;
      this.answerExplanation = '';
      this.quizStartTime = null;
      this.quizEndTime = null;
      this.quizDuration = 0;
    },

    // 验证相关方法
    async checkFirstTimeVisit() {
      // 使用localStorage而不是sessionStorage来持久化标记
      // 但每次页面刷新时都清除这个标记
      const wasRefresh = sessionStorage.getItem('pageWasRefreshed');
      const hasNavigated = localStorage.getItem('hasNavigatedToEducation');
      
      console.log('页面加载检查:', { 
        wasRefresh, 
        hasNavigated,
        showVerification: this.showVerification
      });
      
      // 如果没有导航标记，说明是页面刷新或首次访问
      if (!hasNavigated) {
        console.log('检测到页面刷新或首次访问，显示验证界面');
        this.showVerification = true;
        await this.loadVerificationImages();
      } else {
        console.log('检测到路由导航，不显示验证界面');
        this.showVerification = false;
        // 清除导航标记，为下次刷新做准备
        localStorage.removeItem('hasNavigatedToEducation');
      }
      
      // 设置页面刷新标记
      sessionStorage.setItem('pageWasRefreshed', 'true');
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
      
      // 3秒后关闭验证界面
      setTimeout(() => {
        this.closeVerification();
      }, 3000);
    },

    closeVerification() {
      this.showVerification = false;
      // 重置验证状态，为下次验证做准备
      this.verificationCompleted = false;
      this.verificationResult = null;
      this.verificationImages = [];
      this.selectedImages = [];
      // 不清除localStorage标记，让它在导航时保持
    },

    handleImageError(event) {
      console.warn('图片加载失败:', event.target.src);
      event.target.src = '/api/placeholder/300/200?text=Image+Load+Failed';
    }
  },

  async mounted() {
    // 页面加载时检查是否需要显示验证
    console.log('Education组件已挂载，开始检查验证');
    try {
      await this.checkFirstTimeVisit();
      console.log('验证检查完成');
    } catch (error) {
      console.error('验证检查出错:', error);
    }
  }
}
</script>

<style scoped>
.education-container {
  min-height: 100vh;
  background-image: url('@/assets/bg_login5.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  padding: 0;
  position: relative;
}

.education-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  z-index: 1;
}

.education-header {
  position: relative;
  z-index: 2;
  text-align: center;
  color: white;
  padding: 40px 20px 30px;
  margin-bottom: 40px;
  margin-top: 20px;
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
}

.education-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 40px;
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
  background: rgba(255, 255, 255, 0.15);
  border-radius: 20px;
  padding: 40px 30px;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(20px);
  color: white;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
  background: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.3);
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
  background: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}

/* Top Navigation */
.top-nav {
  background: linear-gradient(90deg, #1A1D25 0%, #01A2EB 100%);
  backdrop-filter: blur(10px);
  color: white;
  padding: 12px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 10;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.video-item:hover {
  transform: translateY(-5px);
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
  align-items: center;
  z-index: 2000;
  animation: fadeIn 0.3s ease forwards;
}

.verification-modal {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 25px;
  padding: 0;
  max-width: 900px;
  width: 95%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s ease forwards;
}

.verification-header {
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
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-2px);
  border-color: rgba(255, 255, 255, 0.5);
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
</style>
