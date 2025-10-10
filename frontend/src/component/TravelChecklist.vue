<template>
  <div class="travel-checklist-page">
    <!-- 背景图片加载占位符 -->
    <div class="bg-placeholder" v-if="!backgroundLoaded">
      <div class="progress-container">
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: loadingProgress + '%' }"></div>
        </div>
        <p class="loading-text">{{ loadingText }}</p>
      </div>
    </div>
    
    <!-- Header with Navigation -->
    <div class="checklist-header">
      <div class="header-left">
        <button class="btn-back" @click="goBack">
          <span class="back-icon">←</span>
          {{ $t('travelChecklist.back') }}
        </button>
      </div>
      <div class="header-center">
        <h1>{{ $t('travelChecklist.title') }}</h1>
        <p>{{ $t('travelChecklist.subtitle') }}</p>
      </div>
      <div class="header-right">
        <div class="app-stats">
          <span class="stat-item">
            <i class="stat-icon">🌊</i>
            {{ $t('travelChecklist.conservationAction') }}
          </span>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="checklist-container">
      <!-- Questionnaire Section - Show if not completed -->
      <div v-if="!questionnaireCompleted" class="questionnaire-section">
        <div class="questionnaire-card">
          <div class="questionnaire-header">
            <h2>{{ $t('travelChecklist.questionnaire.beforeWeStart') }}</h2>
            <p>{{ $t('travelChecklist.questionnaire.description') }}</p>
            <div class="progress-indicator">
              <span>{{ $t('travelChecklist.questionnaire.progress', { current: currentQuestion + 1, total: questions.length }) }}</span>
            </div>
          </div>

          <div class="question-content">
            <div class="question-card">
              <div class="question-icon">{{ questions[currentQuestion].icon }}</div>
              <h3>{{ $t(questions[currentQuestion].questionKey) }}</h3>
              <div class="answer-options">
        <button 
                  v-for="option in questions[currentQuestion].options" 
                  :key="option.value"
                  class="answer-btn"
                  :class="{ selected: answers[currentQuestion] === option.value }"
                  @click="selectAnswer(currentQuestion, option.value)"
                >
                  {{ $t(option.labelKey) }}
                </button>
            </div>
          </div>
          </div>

          <div class="questionnaire-actions">
            <button 
              v-if="currentQuestion > 0" 
              class="btn-secondary" 
              @click="previousQuestion"
            >
              {{ $t('travelChecklist.questionnaire.previous') }}
            </button>
            <button 
              v-if="currentQuestion < questions.length - 1" 
              class="btn-primary" 
              :disabled="answers[currentQuestion] === null"
              @click="nextQuestion"
            >
              {{ $t('travelChecklist.questionnaire.next') }}
            </button>
            <button 
              v-if="currentQuestion === questions.length - 1" 
              class="btn-primary btn-complete" 
              :disabled="answers[currentQuestion] === null"
              @click="completeQuestionnaire"
            >
              {{ $t('travelChecklist.questionnaire.showChecklist') }}
        </button>
          </div>
        </div>
      </div>

      <!-- Checklist Display Section - Show after questionnaire -->
      <div v-else class="checklist-display">
        <!-- Summary Cards -->
        <div class="summary-cards">
          <div 
            v-for="(category, index) in checklistCategories" 
            :key="index"
            class="summary-card"
            :class="{ active: selectedCategory === index }"
            @click="selectedCategory = index"
          >
            <div class="card-icon">{{ category.icon }}</div>
            <div class="card-content">
              <h3>{{ category.title }}</h3>
              <p>{{ category.subtitle }}</p>
              <div class="card-progress">
                {{ getCompletedCount(index) }}/{{ category.items.length }} completed
              </div>
            </div>
          </div>
      </div>

      <!-- Checklist Items -->
      <div class="checklist-section">
        <div class="section-header">
            <h2>{{ checklistCategories[selectedCategory].title }}</h2>
            <p>{{ checklistCategories[selectedCategory].description }}</p>
        </div>

        <div class="checklist-items">
          <div 
              v-for="(item, index) in checklistCategories[selectedCategory].items" 
            :key="index"
            class="checklist-item"
            :class="{ completed: item.completed }"
          >
                         <div class="item-checkbox">
               <input 
                 type="checkbox" 
                  :id="`item-${selectedCategory}-${index}`"
                 v-model="item.completed"
                 @change="updateProgress"
               />
                <label :for="`item-${selectedCategory}-${index}`" class="checkbox-label">
                 <span class="checkmark">✓</span>
               </label>
             </div>
            <div class="item-content">
                <p>{{ item.text }}</p>
            </div>
          </div>
        </div>
      </div>

        <!-- Reset Button -->
        <div class="reset-section">
          <button class="btn-reset" @click="resetQuestionnaire">
            {{ $t('travelChecklist.resetButton') }}
         </button>
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
import { mapGetters } from 'vuex'
import ossService from '@/utils/ossService.js'

export default {
  name: 'TravelChecklist',
  data() {
    return {
      backgroundLoaded: false,
      loadingProgress: 0,
      loadingText: 'Loading checklist...',
      backgroundImageUrl: null,
      
      // Questionnaire state
      questionnaireCompleted: false,
      currentQuestion: 0,
      answers: [null, null, null, null], // Stores answers for 4 questions
      
      // Questions configuration
      questions: [
        {
          icon: '🤿',
          questionKey: 'travelChecklist.questions.firstTime',
          options: [
            { labelKey: 'travelChecklist.options.yes', value: 'yes' },
            { labelKey: 'travelChecklist.options.no', value: 'no' }
          ]
        },
        {
          icon: '👨‍👩‍👧‍👦',
          questionKey: 'travelChecklist.questions.travellingWithKids',
          options: [
            { labelKey: 'travelChecklist.options.yes', value: 'yes' },
            { labelKey: 'travelChecklist.options.no', value: 'no' }
          ]
        },
        {
          icon: '🪸',
          questionKey: 'travelChecklist.questions.reefCondition',
          options: [
            { labelKey: 'travelChecklist.options.good', value: 'good' },
            { labelKey: 'travelChecklist.options.fair', value: 'fair' },
            { labelKey: 'travelChecklist.options.poor', value: 'poor' },
            { labelKey: 'travelChecklist.options.unknown', value: 'unknown' }
          ]
        },
        {
          icon: '🏊',
          questionKey: 'travelChecklist.questions.swimmingLevel',
          options: [
            { labelKey: 'travelChecklist.options.beginner', value: 'beginner' },
            { labelKey: 'travelChecklist.options.intermediate', value: 'intermediate' },
            { labelKey: 'travelChecklist.options.advanced', value: 'advanced' }
          ]
        }
      ],
      
      selectedCategory: 0,
      
      // Checklist data structure
      checklistData: {
        question1: {
          yes: [
            'travelChecklist.checklist.question1.yes.item1',
            'travelChecklist.checklist.question1.yes.item2',
            'travelChecklist.checklist.question1.yes.item3',
            'travelChecklist.checklist.question1.yes.item4',
            'travelChecklist.checklist.question1.yes.item5',
            'travelChecklist.checklist.question1.yes.item6',
            'travelChecklist.checklist.question1.yes.item7'
          ],
          no: [
            'travelChecklist.checklist.question1.no.item1',
            'travelChecklist.checklist.question1.no.item2',
            'travelChecklist.checklist.question1.no.item3',
            'travelChecklist.checklist.question1.no.item4'
          ]
        },
        question2: {
          yes: [
            'travelChecklist.checklist.question2.yes.item1',
            'travelChecklist.checklist.question2.yes.item2',
            'travelChecklist.checklist.question2.yes.item3',
            'travelChecklist.checklist.question2.yes.item4',
            'travelChecklist.checklist.question2.yes.item5',
            'travelChecklist.checklist.question2.yes.item6',
            'travelChecklist.checklist.question2.yes.item7',
            'travelChecklist.checklist.question2.yes.item8',
            'travelChecklist.checklist.question2.yes.item9'
          ],
          no: [
            'travelChecklist.checklist.question2.no.item1',
            'travelChecklist.checklist.question2.no.item2',
            'travelChecklist.checklist.question2.no.item3'
          ]
        },
        question3: {
          good: [
            'travelChecklist.checklist.question3.good.item1',
            'travelChecklist.checklist.question3.good.item2',
            'travelChecklist.checklist.question3.good.item3',
            'travelChecklist.checklist.question3.good.item4'
          ],
          fair: [
            'travelChecklist.checklist.question3.fair.item1',
            'travelChecklist.checklist.question3.fair.item2',
            'travelChecklist.checklist.question3.fair.item3',
            'travelChecklist.checklist.question3.fair.item4'
          ],
          poor: [
            'travelChecklist.checklist.question3.poor.item1',
            'travelChecklist.checklist.question3.poor.item2',
            'travelChecklist.checklist.question3.poor.item3',
            'travelChecklist.checklist.question3.poor.item4'
          ],
          unknown: [
            'travelChecklist.checklist.question3.unknown.item1',
            'travelChecklist.checklist.question3.unknown.item2',
            'travelChecklist.checklist.question3.unknown.item3',
            'travelChecklist.checklist.question3.unknown.item4'
          ]
        },
        question4: {
          beginner: [
            'travelChecklist.checklist.question4.beginner.item1',
            'travelChecklist.checklist.question4.beginner.item2',
            'travelChecklist.checklist.question4.beginner.item3',
            'travelChecklist.checklist.question4.beginner.item4',
            'travelChecklist.checklist.question4.beginner.item5',
            'travelChecklist.checklist.question4.beginner.item6'
          ],
          intermediate: [
            'travelChecklist.checklist.question4.intermediate.item1',
            'travelChecklist.checklist.question4.intermediate.item2',
            'travelChecklist.checklist.question4.intermediate.item3',
            'travelChecklist.checklist.question4.intermediate.item4',
            'travelChecklist.checklist.question4.intermediate.item5'
          ],
          advanced: [
            'travelChecklist.checklist.question4.advanced.item1',
            'travelChecklist.checklist.question4.advanced.item2',
            'travelChecklist.checklist.question4.advanced.item3',
            'travelChecklist.checklist.question4.advanced.item4',
            'travelChecklist.checklist.question4.advanced.item5'
          ]
        }
      }
    }
  },
  
  computed: {
    checklistCategories() {
      if (!this.questionnaireCompleted) return []
      
      const categories = []
      
      // Category 1: Based on experience
      categories.push({
        icon: '🤿',
        title: this.$t('travelChecklist.categories.experience.title'),
        subtitle: this.answers[0] === 'yes' ? this.$t('travelChecklist.categories.experience.firstTime') : this.$t('travelChecklist.categories.experience.experienced'),
        description: this.$t('travelChecklist.categories.experience.description'),
        items: this.checklistData.question1[this.answers[0]].map(textKey => ({
          text: this.$t(textKey),
          completed: false
        }))
      })
      
      // Category 2: Travelling with kids
      const withKids = this.answers[1] === 'yes'
      categories.push({
        icon: '👨‍👩‍👧‍👦',
        title: withKids ? this.$t('travelChecklist.categories.traveling.titleWithKids') : this.$t('travelChecklist.categories.traveling.titleSolo'),
        subtitle: withKids ? this.$t('travelChecklist.categories.traveling.familyTips') : this.$t('travelChecklist.categories.traveling.independent'),
        description: withKids ? this.$t('travelChecklist.categories.traveling.descriptionWithKids') : this.$t('travelChecklist.categories.traveling.descriptionSolo'),
        items: this.checklistData.question2[this.answers[1]].map(textKey => ({
          text: this.$t(textKey),
          completed: false
        }))
      })
      
      // Category 3: Based on reef condition
      categories.push({
        icon: '🪸',
        title: this.$t('travelChecklist.categories.reefCondition.title'),
        subtitle: this.$t(`travelChecklist.categories.reefCondition.${this.answers[2]}Condition`),
        description: this.$t('travelChecklist.categories.reefCondition.description'),
        items: this.checklistData.question3[this.answers[2]].map(textKey => ({
          text: this.$t(textKey),
          completed: false
        }))
      })
      
      // Category 4: Based on swimming skills
      categories.push({
        icon: '🏊',
        title: this.$t('travelChecklist.categories.swimmingSkills.title'),
        subtitle: this.$t(`travelChecklist.categories.swimmingSkills.${this.answers[3]}Level`),
        description: this.$t('travelChecklist.categories.swimmingSkills.description'),
        items: this.checklistData.question4[this.answers[3]].map(textKey => ({
          text: this.$t(textKey),
          completed: false
        }))
      })
      
      return categories
    }
  },
  
  methods: {
    selectAnswer(questionIndex, value) {
      this.$set(this.answers, questionIndex, value)
    },
    
    nextQuestion() {
      if (this.currentQuestion < this.questions.length - 1) {
        this.currentQuestion++
      }
    },
    
    previousQuestion() {
      if (this.currentQuestion > 0) {
        this.currentQuestion--
      }
    },
    
    completeQuestionnaire() {
      this.questionnaireCompleted = true
    },
    
    resetQuestionnaire() {
      this.questionnaireCompleted = false
      this.currentQuestion = 0
      this.answers = [null, null, null, null]
      this.selectedCategory = 0
    },
    
    getCompletedCount(categoryIndex) {
      if (!this.checklistCategories[categoryIndex]) return 0
      return this.checklistCategories[categoryIndex].items.filter(item => item.completed).length
    },
    
    updateProgress() {
      // Optional: Add any progress tracking logic here
    },
    
    goBack() {
      // Set functional navigation flag
      localStorage.setItem('functionalNavigation', 'true')
      this.$router.push('/education').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err)
        }
      })
    },
    
    async loadBackgroundImage() {
      try {
        this.backgroundImageUrl = await ossService.getFileUrl('bg_trend.webp')
        document.documentElement.style.setProperty('--bg-image', `url(${this.backgroundImageUrl})`)
      } catch (error) {
        console.warn('Failed to load background image:', error)
        this.backgroundImageUrl = null
      }
    },
    
    preloadBackgroundImage() {
      const img = new Image()
      img.src = 'http://static.coralkita.site/assets-webp/bg_trend.webp'
      img.onload = () => {
        console.log('Background image preloaded')
        this.backgroundLoaded = true
      }
      img.onerror = () => {
        console.warn('Failed to preload background image')
        this.backgroundLoaded = true
      }
    }
  },
  
  mounted() {
    console.log('TravelChecklist page loaded')
    this.preloadBackgroundImage()
    this.loadBackgroundImage()
    
    localStorage.setItem('lastPageRefresh', Date.now().toString())
  }
}
</script>

<style scoped>
.travel-checklist-page {
  min-height: 100vh;
  background-image: var(--bg-image, url('http://static.coralkita.site/assets-webp/bg_trend.webp'));
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  background-repeat: no-repeat;
  position: relative;
}

/* Background Loading Placeholder */
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
  justify-content: center;
  align-items: center;
  z-index: 1000;
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
}

@keyframes oceanWave {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

/* Header */
.checklist-header {
  background: linear-gradient(90deg, rgba(26, 29, 37, 0.95) 0%, rgba(1, 162, 235, 0.95) 100%);
  backdrop-filter: blur(15px);
  padding: 20px 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.header-left {
  flex: 1;
}

.btn-back {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 10px 20px;
  border-radius: 25px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.btn-back:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateX(-3px);
}

.back-icon {
  font-size: 1.2rem;
}

.header-center {
  flex: 2;
  text-align: center;
}

.header-center h1 {
  color: white;
  font-size: 2rem;
  margin: 0 0 5px 0;
  font-weight: 700;
}

.header-center p {
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
  font-size: 1rem;
}

.header-right {
  flex: 1;
  display: flex;
  justify-content: flex-end;
}

.app-stats {
  display: flex;
  align-items: center;
  gap: 10px;
}

.stat-item {
  background: rgba(255, 255, 255, 0.1);
  padding: 8px 15px;
  border-radius: 20px;
  color: white;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 8px;
}

.stat-icon {
  font-size: 1.2rem;
}

/* Main Container */
.checklist-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 120px 20px 40px;
}

/* Questionnaire Section */
.questionnaire-section {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 200px);
}

.questionnaire-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 30px;
  padding: 50px;
  max-width: 700px;
  width: 100%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.questionnaire-header {
  text-align: center;
  margin-bottom: 40px;
}

.questionnaire-header h2 {
  color: #0f4c75;
  font-size: 2.5rem;
  margin-bottom: 10px;
  font-weight: 700;
}

.questionnaire-header p {
  color: #666;
  font-size: 1.1rem;
  margin-bottom: 20px;
}

.progress-indicator {
  display: inline-block;
  background: linear-gradient(135deg, #3282b8, #0f4c75);
  color: white;
  padding: 8px 20px;
  border-radius: 20px;
  font-weight: 600;
  font-size: 0.9rem;
}

.question-content {
  margin-bottom: 40px;
}

.question-card {
  text-align: center;
}

.question-icon {
  font-size: 5rem;
  margin-bottom: 20px;
  animation: bounce 2s ease-in-out infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.question-card h3 {
  color: #0f4c75;
  font-size: 1.8rem;
  margin-bottom: 30px;
  font-weight: 600;
}

.answer-options {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  justify-content: center;
}

.answer-btn {
  background: rgba(50, 130, 184, 0.1);
  border: 2px solid #3282b8;
  color: #0f4c75;
  padding: 15px 30px;
  border-radius: 25px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 120px;
}

.answer-btn:hover {
  background: rgba(50, 130, 184, 0.2);
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(50, 130, 184, 0.3);
}

.answer-btn.selected {
  background: linear-gradient(135deg, #3282b8, #0f4c75);
   color: white;
  border-color: #0f4c75;
  transform: scale(1.05);
}

.questionnaire-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.btn-primary,
.btn-secondary {
  padding: 15px 40px;
  border-radius: 25px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
}

.btn-primary {
  background: linear-gradient(135deg, #3282b8, #0f4c75);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(50, 130, 184, 0.4);
}

.btn-primary:disabled {
   opacity: 0.5;
   cursor: not-allowed;
}

.btn-primary.btn-complete {
  background: linear-gradient(135deg, #28a745, #20c997);
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.8);
  border: 2px solid #3282b8;
  color: #0f4c75;
}

.btn-secondary:hover {
  background: white;
  transform: translateY(-3px);
}

/* Checklist Display */
.checklist-display {
  animation: fadeIn 0.5s ease-in;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.summary-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.summary-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 3px solid transparent;
  display: flex;
  align-items: center;
  gap: 20px;
}

.summary-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.summary-card.active {
  border-color: #3282b8;
  background: rgba(50, 130, 184, 0.1);
}

.card-icon {
  font-size: 3rem;
  flex-shrink: 0;
}

.card-content h3 {
  color: #0f4c75;
  font-size: 1.2rem;
  margin-bottom: 5px;
  font-weight: 600;
}

.card-content p {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 10px;
}

.card-progress {
  background: linear-gradient(135deg, #3282b8, #0f4c75);
  color: white;
  padding: 5px 12px;
  border-radius: 15px;
  font-size: 0.85rem;
  font-weight: 600;
  display: inline-block;
}

/* Checklist Section */
.checklist-section {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

.section-header {
  text-align: center;
  margin-bottom: 30px;
}

.section-header h2 {
  color: #0f4c75;
  font-size: 2rem;
  margin-bottom: 10px;
  font-weight: 700;
}

.section-header p {
  color: #666;
  font-size: 1.1rem;
}

.checklist-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.checklist-item {
  background: white;
  border: 2px solid #e0e0e0;
  border-radius: 15px;
  padding: 20px;
  display: flex;
  align-items: flex-start;
  gap: 20px;
  transition: all 0.3s ease;
}

.checklist-item:hover {
  border-color: #3282b8;
  box-shadow: 0 5px 15px rgba(50, 130, 184, 0.1);
  transform: translateX(5px);
}

.checklist-item.completed {
  background: rgba(40, 167, 69, 0.05);
  border-color: #28a745;
}

.item-checkbox {
  flex-shrink: 0;
}

.item-checkbox input[type="checkbox"] {
  display: none;
}

.checkbox-label {
  display: block;
  width: 30px;
  height: 30px;
  border: 2px solid #3282b8;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  transition: all 0.3s ease;
}

.checkbox-label:hover {
  background: rgba(50, 130, 184, 0.1);
  transform: scale(1.1);
}

.checkmark {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) scale(0);
  color: white;
  font-size: 1.2rem;
  font-weight: bold;
  transition: transform 0.3s ease;
}

.item-checkbox input[type="checkbox"]:checked + .checkbox-label {
  background: linear-gradient(135deg, #28a745, #20c997);
  border-color: #28a745;
}

.item-checkbox input[type="checkbox"]:checked + .checkbox-label .checkmark {
  transform: translate(-50%, -50%) scale(1);
}

.item-content {
  flex: 1;
}

.item-content p {
  color: #333;
  font-size: 1rem;
  line-height: 1.6;
  margin: 0;
}

.checklist-item.completed .item-content p {
  color: #666;
  text-decoration: line-through;
}

/* Reset Section */
.reset-section {
  text-align: center;
  margin-top: 30px;
}

.btn-reset {
  background: rgba(220, 53, 69, 0.1);
  border: 2px solid #dc3545;
  color: #dc3545;
  padding: 12px 30px;
  border-radius: 25px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-reset:hover {
  background: #dc3545;
  color: white;
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(220, 53, 69, 0.3);
}

/* Footer */
.main-footer {
  width: 100%;
  background: rgba(26, 43, 66, 0.95);
  color: #e0e7ef;
  text-align: center;
  padding: 20px 0;
  font-size: 15px;
  margin-top: 40px;
}

.footer-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.footer-links a {
  color: #a5d8ff;
  text-decoration: none;
  margin: 0 10px;
  transition: color 0.3s ease;
}

.footer-links a:hover {
  color: #fff;
  text-decoration: underline;
}

/* Responsive Design */
@media (max-width: 768px) {
  .checklist-header {
    flex-direction: column;
    gap: 15px;
    padding: 15px 20px;
  }

  .header-left,
  .header-right {
    width: 100%;
    justify-content: center;
  }
  
  .header-center h1 {
    font-size: 1.5rem;
  }
  
  .questionnaire-card {
    padding: 30px 20px;
  }
  
  .questionnaire-header h2 {
    font-size: 2rem;
  }
  
  .question-icon {
    font-size: 3.5rem;
  }
  
  .question-card h3 {
    font-size: 1.4rem;
  }
  
  .answer-options {
    flex-direction: column;
  }
  
  .answer-btn {
  width: 100%;
  }
  
  .summary-cards {
    grid-template-columns: 1fr;
  }
  
  .checklist-section {
    padding: 25px 20px;
  }
  
  .section-header h2 {
    font-size: 1.5rem;
  }
}
</style>
