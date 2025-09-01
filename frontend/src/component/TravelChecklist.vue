<template>
  <div class="travel-checklist-page">
    <!-- Header with Navigation -->
    <div class="checklist-header">
      <div class="header-left">
        <button class="btn-back" @click="goBack">
          <span class="back-icon">←</span>
          Back
        </button>
      </div>
      <div class="header-center">
        <h1>Responsible Travel Checklist</h1>
        <p>Complete key steps for sustainable tourism</p>
      </div>
      <div class="header-right">
        <div class="user-stats" v-if="isAuthenticated">
          <span class="stat-item">
            <i class="stat-icon">⭐</i>
            Points: {{ currentUser.points || 0 }}
          </span>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="checklist-container">
      <!-- Phase Selection -->
      <div class="phase-selector">
        <button 
          v-for="(phase, index) in phases" 
          :key="index"
          class="phase-btn"
          :class="{ active: currentPhase === index }"
          @click="selectPhase(index)"
        >
          <div class="phase-icon">{{ phase.icon }}</div>
          <div class="phase-info">
            <h3>{{ phase.title }}</h3>
            <p>{{ phase.subtitle }}</p>
            <div class="phase-progress">
              {{ getCompletedCount(index) }}/{{ phase.items.length }} completed
            </div>
          </div>
        </button>
      </div>

      <!-- Checklist Items -->
      <div class="checklist-section">
        <div class="section-header">
          <h2>{{ phases[currentPhase].title }}</h2>
          <p>{{ phases[currentPhase].description }}</p>
        </div>

        <div class="checklist-items">
          <div 
            v-for="(item, index) in phases[currentPhase].items" 
            :key="index"
            class="checklist-item"
            :class="{ completed: item.completed }"
          >
            <div class="item-checkbox">
              <input 
                type="checkbox" 
                :id="`item-${currentPhase}-${index}`"
                v-model="item.completed"
                @change="updateProgress"
              />
              <label :for="`item-${currentPhase}-${index}`" class="checkbox-label">
                <span class="checkmark">✓</span>
              </label>
            </div>
            <div class="item-content">
              <h4>{{ item.title }}</h4>
              <div class="item-points">+2 points</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Submit Section -->
      <div class="submit-section">
        <div class="submit-info">
          <div class="total-points">
            Total Points Earned: {{ getTotalPoints() }}
          </div>
          <div class="completion-status">
            Total Completed: {{ getTotalCompleted() }}/{{ getTotalItems() }} items
          </div>
        </div>
        <button class="btn-submit" @click="submitChecklist" :disabled="!isAuthenticated">
          {{ isAuthenticated ? 'Submit Checklist' : 'Login to Submit' }}
        </button>
      </div>

      <!-- Login Reminder -->
      <div v-if="!isAuthenticated" class="login-reminder">
        <p>Please log in to save your progress and earn points</p>
        <button class="btn-login" @click="goToEducation">Go to Login</button>
      </div>
    </div>

    <!-- Feedback Modal -->
    <div v-if="showFeedback" class="feedback-overlay">
      <div class="feedback-modal">
        <div class="feedback-header">
          <h2>Travel Checklist Results</h2>
          <button class="close-btn" @click="closeFeedback">×</button>
        </div>
        <div class="feedback-content">
          <div class="feedback-summary">
            <div class="points-earned">
              <h3>Points Earned: {{ getTotalPoints() }}</h3>
              <p>Great job on completing {{ getTotalCompleted() }} items!</p>
            </div>
          </div>
          
          <!-- Suggestions for uncompleted items -->
          <div v-if="getUncompletedItems().length > 0" class="suggestions">
            <h3>Suggestions for Better Responsible Travel:</h3>
            <div class="suggestion-list">
              <div 
                v-for="suggestion in getUncompletedItems()" 
                :key="suggestion.id"
                class="suggestion-item"
              >
                <div class="suggestion-icon">💡</div>
                <div class="suggestion-content">
                  <h4>{{ suggestion.title }}</h4>
                  <p>{{ suggestion.suggestion }}</p>
                </div>
              </div>
            </div>
          </div>

          <div class="feedback-actions">
            <button class="btn-continue" @click="continueWithSubmission">
              Continue with Submission
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'TravelChecklist',
  data() {
    return {
      currentPhase: 0,
      showFeedback: false,
      phases: [
        {
          title: 'Plan & Prepare',
          subtitle: 'Before Travel',
          icon: '📋',
          description: 'Essential preparations for responsible tourism',
          items: [
            {
              title: 'Research and book eco-friendly operators',
              description: 'Research and book eco-friendly operators.',
              completed: false,
              suggestion: 'Choosing eco-friendly operators ensures your tourism dollars support businesses committed to reef conservation. These operators follow strict guidelines to minimize environmental impact and often contribute to local conservation efforts.'
            },
            {
              title: 'Use reef-safe sunscreen (oxybenzone & octinoxate free)',
              description: 'Use reef-safe sunscreen (oxybenzone & octinoxate free).',
              completed: false,
              suggestion: 'Chemical sunscreens containing oxybenzone and octinoxate have been proven to cause coral bleaching even in small concentrations. Reef-safe alternatives protect both your skin and marine life.'
            },
            {
              title: 'Bring reusable water bottles, shopping bags, and utensils',
              description: 'Bring reusable water bottles, shopping bags, and utensils.',
              completed: false,
              suggestion: 'Single-use plastics are a major threat to marine ecosystems. Plastic pollution can harm marine life through ingestion and entanglement, and contribute to the degradation of coral reef habitats.'
            },
            {
              title: 'Check current reef health alerts for your destination',
              description: 'Check current reef health alerts for your destination.',
              completed: false,
              suggestion: 'Staying informed about reef conditions helps you make responsible choices about where to visit. Avoiding stressed reefs during bleaching events gives corals the best chance to recover without additional human pressure.'
            }
          ]
        },
        {
          title: 'On-Site Behaviors',
          subtitle: 'During Travel',
          icon: '🤿',
          description: 'Responsible practices while exploring coral reefs',
          items: [
            {
              title: 'Never touch, step on, or collect corals or marine animals',
              description: 'Never touch, step on, or collect corals or marine animals.',
              completed: false,
              suggestion: 'Even light touches can damage coral polyps and stress marine animals. Corals are living organisms that can take decades to grow, and human contact can kill them or make them vulnerable to disease.'
            },
            {
              title: 'Maintain a safe distance when observing wildlife to avoid stress or harm',
              description: 'Maintain a safe distance when observing wildlife to avoid stress or harm.',
              completed: false,
              suggestion: 'Marine animals can experience stress when approached too closely, affecting their feeding, mating, and natural behaviors. Respecting their space ensures both their wellbeing and your safety.'
            },
            {
              title: 'Practice neutral buoyancy before entering sensitive reef zones',
              description: 'Practice neutral buoyancy before entering sensitive reef zones.',
              completed: false,
              suggestion: 'Proper buoyancy control is the most important skill for reef protection. Practice in open water or sandy areas first to avoid accidental damage to fragile coral ecosystems.'
            },
            {
              title: 'Avoid kicking up sand or sediment near corals',
              description: 'Avoid kicking up sand or sediment near corals.',
              completed: false,
              suggestion: 'Sediment can block sunlight essential for coral photosynthesis and physically damage delicate coral structures. Suspended particles can also reduce water quality and visibility for other divers.'
            },
            {
              title: 'Bring back all trash, including wrappers and bottles',
              description: 'Bring back all trash, including wrappers and bottles.',
              completed: false,
              suggestion: 'Marine pollution is one of the biggest threats to coral reefs. Small trash items can be ingested by marine life or become entangled around coral formations, causing long-term damage.'
            },
            {
              title: 'Join community-led eco-tours',
              description: 'Join community-led eco-tours.',
              completed: false,
              suggestion: 'Community-led tours provide economic benefits directly to local people, creating incentives for conservation. Local guides often have the best knowledge of marine life behavior and sensitive areas.'
            },
            {
              title: 'Follow the guide instructions, especially in marine protected areas',
              description: 'Follow the guide instructions, especially in marine protected areas.',
              completed: false,
              suggestion: 'Marine protected areas have specific rules designed to protect vulnerable ecosystems. Guide instructions are based on scientific research and local regulations to minimize environmental impact.'
            }
          ]
        },
        {
          title: 'Post-Trip Action',
          subtitle: 'After Travel',
          icon: '🌍',
          description: 'Continue your impact beyond the trip',
          items: [
            {
              title: 'Post photos that show good practices, not harmful ones',
              description: 'Post photos that show good practices, not harmful ones.',
              completed: false,
              suggestion: 'Your social media posts influence others\' behavior. Showing responsible practices encourages followers to adopt similar behaviors, while harmful content can normalize destructive actions that damage reefs.'
            },
            {
              title: 'Tag and promote eco-friendly operators in your posts',
              description: 'Tag and promote eco-friendly operators in your posts.',
              completed: false,
              suggestion: 'Promoting responsible operators helps them gain more customers and demonstrates market demand for sustainable tourism. This economic incentive encourages more businesses to adopt eco-friendly practices.'
            },
            {
              title: 'Upload reef photos to monitoring platforms to support citizen science',
              description: 'Upload reef photos to monitoring platforms to support citizen science.',
              completed: false,
              suggestion: 'Citizen science data is invaluable for reef conservation research. Your photos can help scientists monitor coral health changes, track species populations, and identify conservation priorities.'
            },
            {
              title: 'Report any bleaching or damage to local conservation groups',
              description: 'Report any bleaching or damage to local conservation groups.',
              completed: false,
              suggestion: 'Early reporting of reef damage helps conservationists respond quickly to threats. Your observations can trigger interventions that prevent further damage and support reef recovery efforts.'
            },
            {
              title: 'Leave positive feedback for eco-friendly operators',
              description: 'Leave positive feedback for eco-friendly operators.',
              completed: false,
              suggestion: 'Positive reviews help eco-friendly operators attract more customers and stand out from competitors. Your feedback also helps other travelers identify truly responsible tourism options.'
            },
            {
              title: 'Pay attention to newsletters to keep updated on reef health and tips',
              description: 'Pay attention to newsletters to keep updated on reef health and tips.',
              completed: false,
              suggestion: 'Staying informed helps you make better decisions for future trips and keeps you engaged in reef conservation. Continued learning ensures your travel practices evolve with new scientific understanding.'
            }
          ]
        }
      ]
    }
  },
  computed: {
    ...mapGetters(['isAuthenticated', 'currentUser'])
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    
    goToEducation() {
      this.$router.push('/education');
    },
    
    selectPhase(phaseIndex) {
      this.currentPhase = phaseIndex;
    },
    
    updateProgress() {
      // Progress is automatically updated through v-model
    },
    
    getCompletedCount(phaseIndex) {
      return this.phases[phaseIndex].items.filter(item => item.completed).length;
    },
    
    getTotalCompleted() {
      return this.phases.reduce((total, phase) => {
        return total + phase.items.filter(item => item.completed).length;
      }, 0);
    },
    
    getTotalItems() {
      return this.phases.reduce((total, phase) => total + phase.items.length, 0);
    },
    
    getTotalPoints() {
      return this.getTotalCompleted() * 2;
    },
    
    getUncompletedItems() {
      const uncompleted = [];
      this.phases.forEach((phase, phaseIndex) => {
        phase.items.forEach((item, itemIndex) => {
          if (!item.completed) {
            uncompleted.push({
              id: `${phaseIndex}-${itemIndex}`,
              title: item.title,
              suggestion: item.suggestion
            });
          }
        });
      });
      return uncompleted;
    },
    
    submitChecklist() {
      if (!this.isAuthenticated) {
        alert('Please log in to submit your checklist');
        return;
      }
      
      const uncompletedItems = this.getUncompletedItems();
      if (uncompletedItems.length > 0) {
        this.showFeedback = true;
      } else {
        this.continueWithSubmission();
      }
    },
    
    continueWithSubmission() {
      const pointsEarned = this.getTotalPoints();
      
      // Update user points in Vuex
      if (pointsEarned > 0) {
        this.$store.dispatch('updateUserStats', {
          points: (this.currentUser.points || 0) + pointsEarned
        });
      }
      
      // Show success message
      alert(`Checklist submitted successfully! You earned ${pointsEarned} points.`);
      
      this.closeFeedback();
      
      // Optionally navigate back
      // this.$router.push('/education');
    },
    
    closeFeedback() {
      this.showFeedback = false;
    }
  }
}
</script>

<style scoped>
.travel-checklist-page {
  min-height: 100vh;
  background-image: url('@/assets/ed_interface.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  font-family: 'Arial', sans-serif;
}

/* Header */
.checklist-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 40px;
  background: linear-gradient(90deg, rgba(26, 29, 37, 0.9) 0%, rgba(1, 162, 235, 0.9) 100%);
  backdrop-filter: blur(15px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.header-left .btn-back {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 20px;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-back:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.header-center {
  text-align: center;
  color: white;
}

.header-center h1 {
  margin: 0 0 5px 0;
  font-size: 24px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.header-center p {
  margin: 0;
  opacity: 0.9;
  font-size: 14px;
}

.header-right .user-stats {
  display: flex;
  gap: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
  color: white;
  font-size: 14px;
}

/* Main Container */
.checklist-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}


/* Phase Selector */
.phase-selector {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 40px;
}

.phase-btn {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: white;
  text-align: left;
}

.phase-btn:hover {
  background: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.4);
  transform: translateY(-5px);
}

.phase-btn.active {
  background: rgba(102, 126, 234, 0.3);
  border-color: #667eea;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.phase-icon {
  font-size: 32px;
  margin-bottom: 15px;
}

.phase-info h3 {
  margin: 0 0 8px 0;
  font-size: 18px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.phase-info p {
  margin: 0 0 10px 0;
  opacity: 0.8;
  font-size: 14px;
}

.phase-progress {
  font-size: 12px;
  opacity: 0.7;
}

/* Checklist Section */
.checklist-section {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  margin-bottom: 30px;
}

.section-header {
  margin-bottom: 25px;
  text-align: center;
}

.section-header h2 {
  color: white;
  margin: 0 0 10px 0;
  font-size: 24px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.section-header p {
  color: white;
  opacity: 0.9;
  margin: 0;
  font-size: 16px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

/* Checklist Items */
.checklist-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.checklist-item {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.1);
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 15px;
  transition: all 0.3s ease;
}

.checklist-item:hover {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.4);
  transform: translateY(-2px);
}

.checklist-item.completed {
  background: rgba(76, 175, 80, 0.2);
  border-color: rgba(76, 175, 80, 0.6);
}

/* Custom Checkbox */
.item-checkbox {
  position: relative;
}

.item-checkbox input[type="checkbox"] {
  opacity: 0;
  position: absolute;
  width: 24px;
  height: 24px;
  cursor: pointer;
}

.checkbox-label {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border: 2px solid rgba(255, 255, 255, 0.5);
  border-radius: 6px;
  background: rgba(255, 255, 255, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
}

.checkmark {
  color: transparent;
  font-size: 16px;
  font-weight: bold;
  transition: all 0.3s ease;
}

.item-checkbox input[type="checkbox"]:checked + .checkbox-label {
  background: #4CAF50;
  border-color: #4CAF50;
}

.item-checkbox input[type="checkbox"]:checked + .checkbox-label .checkmark {
  color: white;
}

/* Item Content */
.item-content {
  flex: 1;
}

.item-content h4 {
  color: white;
  margin: 0 0 8px 0;
  font-size: 16px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

.item-content p {
  color: white;
  opacity: 0.9;
  margin: 0 0 10px 0;
  font-size: 14px;
  line-height: 1.5;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

.item-points {
  color: #FFD700;
  font-size: 12px;
  font-weight: bold;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.8);
}

/* Submit Section */
.submit-section {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  text-align: center;
  margin-bottom: 20px;
}

.submit-info {
  margin-bottom: 20px;
}

.total-points {
  color: #FFD700;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.completion-status {
  color: white;
  font-size: 16px;
  opacity: 0.9;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

.btn-submit {
  padding: 15px 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 18px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.6);
}

.btn-submit:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

/* Login Reminder */
.login-reminder {
  background: rgba(255, 193, 7, 0.2);
  border: 2px solid rgba(255, 193, 7, 0.6);
  border-radius: 15px;
  padding: 20px;
  text-align: center;
  color: white;
}

.login-reminder p {
  margin: 0 0 15px 0;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

.btn-login {
  padding: 10px 25px;
  background: #FFC107;
  color: #333;
  border: none;
  border-radius: 20px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-login:hover {
  background: #FFB300;
  transform: translateY(-2px);
}

/* Feedback Modal */
.feedback-overlay {
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
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.feedback-modal {
  background: linear-gradient(135deg, rgba(156, 160, 181, 0.95) 0%, rgba(255, 255, 255, 0.9) 100%);
  border-radius: 25px;
  padding: 0;
  max-width: 600px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4);
  animation: slideUp 0.3s ease;
}

.feedback-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25px 30px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
}

.feedback-header h2 {
  color: white;
  font-size: 24px;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.close-btn {
  background: none;
  border: none;
  color: white;
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

.close-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: rotate(90deg);
}

.feedback-content {
  padding: 30px;
}

.feedback-summary {
  text-align: center;
  margin-bottom: 30px;
}

.points-earned h3 {
  color: #FFD700;
  font-size: 28px;
  margin: 0 0 10px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.points-earned p {
  color: white;
  font-size: 16px;
  margin: 0;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

.suggestions h3 {
  color: white;
  font-size: 20px;
  margin: 0 0 20px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.suggestion-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.suggestion-item {
  display: flex;
  gap: 15px;
  padding: 15px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.suggestion-icon {
  font-size: 24px;
  flex-shrink: 0;
}

.suggestion-content h4 {
  color: white;
  margin: 0 0 8px 0;
  font-size: 16px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

.suggestion-content p {
  color: white;
  opacity: 0.9;
  margin: 0;
  font-size: 14px;
  line-height: 1.5;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.8);
}

.feedback-actions {
  text-align: center;
  margin-top: 30px;
}

.btn-continue {
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
}

.btn-continue:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.6);
}

/* Animations */
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

/* Responsive Design */
@media (max-width: 768px) {
  .checklist-header {
    flex-direction: column;
    gap: 15px;
    padding: 15px 20px;
  }

  .header-left, .header-right {
    width: 100%;
  }

  .phase-selector {
    grid-template-columns: 1fr;
  }

  .checklist-container {
    padding: 20px 15px;
  }

  .checklist-item {
    flex-direction: column;
    gap: 10px;
  }

  .feedback-modal {
    width: 95%;
    margin: 20px;
  }


}
</style>
