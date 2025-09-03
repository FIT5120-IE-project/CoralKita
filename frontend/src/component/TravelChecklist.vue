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
        <div class="app-stats">
          <span class="stat-item">
            <i class="stat-icon">🌊</i>
            CoralKita Conservation Action
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
                 :disabled="item.disabled"
               />
               <label :for="`item-${currentPhase}-${index}`" class="checkbox-label" :class="{ disabled: item.disabled }">
                 <span class="checkmark">✓</span>
               </label>
             </div>
            <div class="item-content">
              <h4>{{ item.title }}</h4>
            </div>
          </div>
        </div>
      </div>

             <!-- Submit Section -->
       <div class="submit-section">
         <div class="submit-info">
           <div class="completion-status">
             New Completed: {{ getNewCompletedCount() }} | Total Completed: {{ getTotalCompleted() }}/{{ getTotalItems() }} items
           </div>
         </div>
         
         <!-- Show congratulation message when all completed -->
         <div v-if="isAllCompleted()" class="completion-message">
           <div class="badge-icon">🏆</div>
           <h3>You have completed all tasks!</h3>
           <p>Congratulations on earning the badge <strong>"Coral Guardian"</strong>!</p>
         </div>
         
         <!-- Show submit button when not all completed -->
         <button 
           v-else
           class="btn-submit" 
           @click="submitChecklist"
         >
           Submit Checklist
         </button>
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
            <div class="completion-earned">
              <h3>Great Progress!</h3>
              <p>You completed {{ getTotalCompleted() }} out of {{ getTotalItems() }} items!</p>
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

    <!-- Completion Results Modal -->
    <div v-if="showCompletionModal" class="completion-overlay">
      <div class="completion-modal">
        <div class="completion-header">
          <h2>🌊 Travel Checklist Results</h2>
          <button class="close-btn" @click="closeCompletionModal">×</button>
        </div>
        <div class="completion-content">
          <!-- Overall Medal Display -->
          <div v-if="medalInfo" class="medal-display">
            <div class="medal-icon" :style="{ color: medalInfo.color }">{{ medalInfo.icon }}</div>
            <h2 class="medal-title">{{ medalInfo.title }}</h2>
            <p class="medal-message">{{ medalInfo.message }}</p>
          </div>

          <!-- Phase Medals -->
          <div v-if="completionMedals.length > 0" class="phase-medals">
            <h3>🎖️ Phase Completion Medals</h3>
            <div class="phase-medals-grid">
              <div v-for="medal in completionMedals" :key="medal.type" class="phase-medal">
                <div class="phase-medal-icon" :style="{ color: medal.color }">{{ medal.icon }}</div>
                <h4>{{ medal.title }}</h4>
                <p>{{ medal.message }}</p>
              </div>
            </div>
          </div>

          <!-- Statistics -->
          <div class="completion-statistics">
            <div class="stats-grid">
              <div class="stat-card completed">
                <div class="stat-icon">✓</div>
                <div class="stat-info">
                  <span class="stat-number">{{ getTotalCompleted() }}</span>
                  <span class="stat-label">Completed</span>
                </div>
              </div>
              <div class="stat-card remaining">
                <div class="stat-icon">○</div>
                <div class="stat-info">
                  <span class="stat-number">{{ getTotalItems() - getTotalCompleted() }}</span>
                  <span class="stat-label">Remaining</span>
                </div>
              </div>
              <div class="stat-card total">
                <div class="stat-icon">📊</div>
                <div class="stat-info">
                  <span class="stat-number">{{ getTotalItems() }}</span>
                  <span class="stat-label">Total</span>
                </div>
              </div>
            </div>
            
            <div class="completion-summary">
              <h4>Checklist Results</h4>
              <p class="completion-text">Completed: <span class="completion-number">{{ getTotalCompleted() }}</span> / {{ getTotalItems() }}</p>
              <p class="progress-text">Progress: <span class="progress-number">{{ Math.round((getTotalCompleted() / getTotalItems()) * 100) }}%</span></p>
            </div>
          </div>

          <!-- Uncompleted Items Summary -->
          <div v-if="getUncompletedItems().length > 0" class="uncompleted-summary">
            <h4>💡 Items to Consider for Next Time</h4>
            <p class="uncompleted-count">{{ getUncompletedItems().length }} items not completed yet</p>
            <div class="uncompleted-list">
              <div v-for="item in getUncompletedItems().slice(0, 3)" :key="item.id" class="uncompleted-item">
                <span class="uncompleted-title">{{ item.title }}</span>
              </div>
              <div v-if="getUncompletedItems().length > 3" class="more-items">
                +{{ getUncompletedItems().length - 3 }} more items...
              </div>
            </div>
          </div>

          <!-- Share Section -->
          <div class="share-section">
            <div class="share-header">
              <h3>🎉 Share Your Progress!</h3>
              <p>Inspire others to practice sustainable coral reef tourism</p>
            </div>
            
            <div class="share-options">
              <button class="btn-share twitter" @click="shareToTwitter">
                <i class="share-icon">🐦</i>
                <span>Twitter</span>
              </button>
              
              <button class="btn-share facebook" @click="shareToFacebook">
                <i class="share-icon">📘</i>
                <span>Facebook</span>
              </button>
              
              <button class="btn-share copy" @click="copyShareLink">
                <i class="share-icon">🔗</i>
                <span>Copy Link</span>
              </button>
            </div>
          </div>

          <div class="completion-actions">
            <button class="btn-back-education" @click="goToEducation">
              Back to Education
            </button>
            <button class="btn-close-modal" @click="closeCompletionModal">
              Stay on Checklist
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
      showCompletionModal: false,
      medalInfo: null,
      completionMedals: [],
      // Question mapping: map frontend phases structure to backend q1-q17
      questionMapping: [
        // Plan & Prepare (phase 0)
        { phase: 0, itemIndex: 0, qNumber: 1 },  // Research and book eco-friendly operators
        { phase: 0, itemIndex: 1, qNumber: 2 },  // Use reef-safe sunscreen
        { phase: 0, itemIndex: 2, qNumber: 3 },  // Bring reusable items
        { phase: 0, itemIndex: 3, qNumber: 4 },  // Check reef health alerts
        
        // On-Site Behaviors (phase 1)
        { phase: 1, itemIndex: 0, qNumber: 5 },  // Never touch corals
        { phase: 1, itemIndex: 1, qNumber: 6 },  // Maintain safe distance
        { phase: 1, itemIndex: 2, qNumber: 7 },  // Practice neutral buoyancy
        { phase: 1, itemIndex: 3, qNumber: 8 },  // Avoid kicking up sand
        { phase: 1, itemIndex: 4, qNumber: 9 },  // Bring back all trash
        { phase: 1, itemIndex: 5, qNumber: 10 }, // Join community-led tours
        { phase: 1, itemIndex: 6, qNumber: 11 }, // Follow guide instructions
        
        // Post-Trip Action (phase 2)
        { phase: 2, itemIndex: 0, qNumber: 12 }, // Post good practice photos
        { phase: 2, itemIndex: 1, qNumber: 13 }, // Tag eco-friendly operators
        { phase: 2, itemIndex: 2, qNumber: 14 }, // Upload to monitoring platforms
        { phase: 2, itemIndex: 3, qNumber: 15 }, // Report bleaching/damage
        { phase: 2, itemIndex: 4, qNumber: 16 }, // Leave positive feedback
        { phase: 2, itemIndex: 5, qNumber: 17 }  // Pay attention to newsletters
      ],
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
               disabled: false,
               suggestion: 'Choosing eco-friendly operators ensures your tourism dollars support businesses committed to reef conservation. These operators follow strict guidelines to minimize environmental impact and often contribute to local conservation efforts.'
             },
                         {
               title: 'Use reef-safe sunscreen (oxybenzone & octinoxate free)',
               description: 'Use reef-safe sunscreen (oxybenzone & octinoxate free).',
               completed: false,
               disabled: false,
               suggestion: 'Chemical sunscreens containing oxybenzone and octinoxate have been proven to cause coral bleaching even in small concentrations. Reef-safe alternatives protect both your skin and marine life.'
             },
             {
               title: 'Bring reusable water bottles, shopping bags, and utensils',
               description: 'Bring reusable water bottles, shopping bags, and utensils.',
               completed: false,
               disabled: false,
               suggestion: 'Single-use plastics are a major threat to marine ecosystems. Plastic pollution can harm marine life through ingestion and entanglement, and contribute to the degradation of coral reef habitats.'
             },
             {
               title: 'Check current reef health alerts for your destination',
               description: 'Check current reef health alerts for your destination.',
               completed: false,
               disabled: false,
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
               disabled: false,
               suggestion: 'Even light touches can damage coral polyps and stress marine animals. Corals are living organisms that can take decades to grow, and human contact can kill them or make them vulnerable to disease.'
             },
                         {
               title: 'Maintain a safe distance when observing wildlife to avoid stress or harm',
               description: 'Maintain a safe distance when observing wildlife to avoid stress or harm.',
               completed: false,
               disabled: false,
               suggestion: 'Marine animals can experience stress when approached too closely, affecting their feeding, mating, and natural behaviors. Respecting their space ensures both their wellbeing and your safety.'
             },
             {
               title: 'Practice neutral buoyancy before entering sensitive reef zones',
               description: 'Practice neutral buoyancy before entering sensitive reef zones.',
               completed: false,
               disabled: false,
               suggestion: 'Proper buoyancy control is the most important skill for reef protection. Practice in open water or sandy areas first to avoid accidental damage to fragile coral ecosystems.'
             },
             {
               title: 'Avoid kicking up sand or sediment near corals',
               description: 'Avoid kicking up sand or sediment near corals.',
               completed: false,
               disabled: false,
               suggestion: 'Sediment can block sunlight essential for coral photosynthesis and physically damage delicate coral structures. Suspended particles can also reduce water quality and visibility for other divers.'
             },
             {
               title: 'Bring back all trash, including wrappers and bottles',
               description: 'Bring back all trash, including wrappers and bottles.',
               completed: false,
               disabled: false,
               suggestion: 'Marine pollution is one of the biggest threats to coral reefs. Small trash items can be ingested by marine life or become entangled around coral formations, causing long-term damage.'
             },
             {
               title: 'Join community-led eco-tours',
               description: 'Join community-led eco-tours.',
               completed: false,
               disabled: false,
               suggestion: 'Community-led tours provide economic benefits directly to local people, creating incentives for conservation. Local guides often have the best knowledge of marine life behavior and sensitive areas.'
             },
             {
               title: 'Follow the guide instructions, especially in marine protected areas',
               description: 'Follow the guide instructions, especially in marine protected areas.',
               completed: false,
               disabled: false,
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
               disabled: false,
               suggestion: 'Your social media posts influence others\' behavior. Showing responsible practices encourages followers to adopt similar behaviors, while harmful content can normalize destructive actions that damage reefs.'
             },
                         {
               title: 'Tag and promote eco-friendly operators in your posts',
               description: 'Tag and promote eco-friendly operators in your posts.',
               completed: false,
               disabled: false,
               suggestion: 'Promoting responsible operators helps them gain more customers and demonstrates market demand for sustainable tourism. This economic incentive encourages more businesses to adopt eco-friendly practices.'
             },
             {
               title: 'Upload reef photos to monitoring platforms to support citizen science',
               description: 'Upload reef photos to monitoring platforms to support citizen science.',
               completed: false,
               disabled: false,
               suggestion: 'Citizen science data is invaluable for reef conservation research. Your photos can help scientists monitor coral health changes, track species populations, and identify conservation priorities.'
             },
             {
               title: 'Report any bleaching or damage to local conservation groups',
               description: 'Report any bleaching or damage to local conservation groups.',
               completed: false,
               disabled: false,
               suggestion: 'Early reporting of reef damage helps conservationists respond quickly to threats. Your observations can trigger interventions that prevent further damage and support reef recovery efforts.'
             },
             {
               title: 'Leave positive feedback for eco-friendly operators',
               description: 'Leave positive feedback for eco-friendly operators.',
               completed: false,
               disabled: false,
               suggestion: 'Positive reviews help eco-friendly operators attract more customers and stand out from competitors. Your feedback also helps other travelers identify truly responsible tourism options.'
             },
             {
               title: 'Pay attention to newsletters to keep updated on reef health and tips',
               description: 'Pay attention to newsletters to keep updated on reef health and tips.',
               completed: false,
               disabled: false,
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
  async mounted() {
    // Page load, initialize local data
    console.log('Component mounted, initializing checklist data');
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    
    goToEducation() {
      // Set navigation marker to indicate this is route navigation not page refresh
      localStorage.setItem('hasNavigatedToEducation', 'true');
      this.$router.push('/education');
    },

    // Update frontend phases based on local data
    updatePhasesFromBackend(checklistData) {
      console.log('Updating frontend phases, backend data:', checklistData);
      this.questionMapping.forEach(mapping => {
        const phase = this.phases[mapping.phase];
        const item = phase.items[mapping.itemIndex];
        const qValue = checklistData[`q${mapping.qNumber}`];
        
        // Set completion status (0=not completed, 1=completed)
        item.completed = qValue === 1;
        
        // If completed, disable checkbox
        if (item.completed) {
          item.disabled = true;
        }
      });
    },

    // Convert frontend phases data to backend q1-q17 format
    convertPhasesToBackendFormat() {
      const checklistData = {
        userId: this.currentUser.id
      };
      
      // Initialize all q fields to 0
      for (let i = 1; i <= 17; i++) {
        checklistData[`q${i}`] = 0;
      }
      
      // Set completion status based on mapping relationship
      this.questionMapping.forEach(mapping => {
        const phase = this.phases[mapping.phase];
        const item = phase.items[mapping.itemIndex];
        const qNumber = mapping.qNumber;
        
        // Submit all completion status (including previously completed)
        checklistData[`q${qNumber}`] = item.completed ? 1 : 0;
      });
      
      console.log('Converted to backend format:', checklistData);
      return checklistData;
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
    
    getNewCompletedCount() {
      // Calculate newly completed items count (completed items that are not disabled)
      let newCompletedCount = 0;
      this.phases.forEach(phase => {
        phase.items.forEach(item => {
          if (item.completed && !item.disabled) {
            newCompletedCount++;
          }
        });
      });
      return newCompletedCount;
     },
     
     // 检查是否全部完成
     isAllCompleted() {
       return this.getTotalCompleted() === this.getTotalItems();
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
      const uncompletedItems = this.getUncompletedItems();
      if (uncompletedItems.length > 0) {
        this.showFeedback = true;
      } else {
        this.continueWithSubmission();
      }
    },
    
    async continueWithSubmission() {
      try {
        // 转换为后端格式并提交数据
        const checklistData = this.convertPhasesToBackendFormat();
        console.log('Ready to submit data:', checklistData);
        
        const response = await fetch('/checklist/submit', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(checklistData)
        });
        
        console.log('Submit response status:', response.status);
        
        if (response.ok) {
          const result = await response.json();
          console.log('Submit successful, response:', result);
          
                     this.closeFeedback();
           
          // 显示奖章和结果
          this.showCompletionResults();
        } else {
          const errorText = await response.text();
          console.error('Submit failed:', response.status, errorText);
          alert(`Submit failed: ${response.status} - ${errorText}`);
        }
      } catch (error) {
        console.error('Submit checklist failed:', error);
        alert('Submit failed, please check network connection');
      }
    },
    
    closeFeedback() {
      this.showFeedback = false;
    },

    // 计算总体奖章（根据完成数量）
    calculateOverallMedal(completedCount) {
      if (completedCount >= 3 && completedCount <= 8) {
        return {
          type: 'bronze',
          title: 'Bronze Conservation Guardian',
          message: 'You\'ve taken the first steps towards responsible coral reef tourism! Keep building your sustainable travel practices.',
          icon: '🥉',
          color: '#CD7F32'
        }
      } else if (completedCount >= 9 && completedCount <= 14) {
        return {
          type: 'silver',
          title: 'Silver Reef Protector',
          message: 'Excellent progress! You demonstrate strong commitment to coral reef conservation during your travels.',
          icon: '🥈',
          color: '#C0C0C0'
        }
      } else if (completedCount >= 15 && completedCount <= 17) {
        return {
          type: 'gold',
          title: 'Gold Coral Champion',
          message: 'Outstanding! You are a true coral reef conservation champion, leading by example in sustainable tourism.',
          icon: '🥇',
          color: '#FFD700'
        }
      }
      return null;
    },

    // 计算阶段奖章（每个大项完成获得）
    calculatePhaseMedals() {
      const medals = [];
      this.phases.forEach((phase, index) => {
        const completedInPhase = this.getCompletedCount(index);
        const totalInPhase = phase.items.length;
        
        if (completedInPhase === totalInPhase) {
          let medal = null;
          switch (index) {
            case 0: // Plan & Prepare
              medal = {
                type: 'planning',
                title: 'Master Planner',
                message: 'You\'ve mastered the art of sustainable travel planning!',
                icon: '📋',
                color: '#4CAF50'
              };
              break;
            case 1: // On-Site Behaviors
              medal = {
                type: 'behavior',
                title: 'Reef Guardian',
                message: 'Your on-site behavior protects coral reefs and marine life!',
                icon: '🤿',
                color: '#2196F3'
              };
              break;
            case 2: // Post-Trip Action
              medal = {
                type: 'advocacy',
                title: 'Conservation Advocate',
                message: 'You continue making a difference even after your trip ends!',
                icon: '🌍',
                color: '#FF9800'
              };
              break;
          }
          if (medal) medals.push(medal);
        }
      });
      return medals;
    },

    // 提交后显示奖章和结果
    showCompletionResults() {
      const completedCount = this.getTotalCompleted();
      this.medalInfo = this.calculateOverallMedal(completedCount);
      this.completionMedals = this.calculatePhaseMedals();
      this.showCompletionModal = true;
    },

    closeCompletionModal() {
      this.showCompletionModal = false;
    },

    // 分享功能方法
    shareToTwitter() {
      const completedCount = this.getTotalCompleted();
      const totalCount = this.getTotalItems();
      let shareText = `🌊 I just completed ${completedCount}/${totalCount} items on the CoralKita Responsible Travel Checklist! 🐠\n\n`;
      
      if (this.medalInfo) {
        shareText += `Earned: ${this.medalInfo.title} ${this.medalInfo.icon}\n`;
      }
      
      if (this.completionMedals.length > 0) {
        shareText += `Phase medals: ${this.completionMedals.map(m => `${m.title} ${m.icon}`).join(', ')}\n`;
      }
      
      shareText += `\nJoin me in protecting coral reefs through sustainable tourism! 🌊`;
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
      const completedCount = this.getTotalCompleted();
      const totalCount = this.getTotalItems();
      let shareText = `🌊 I just completed ${completedCount}/${totalCount} items on the CoralKita Responsible Travel Checklist! 🐠\n\n`;
      
      if (this.medalInfo) {
        shareText += `Earned: ${this.medalInfo.title} ${this.medalInfo.icon}\n`;
      }
      
      if (this.completionMedals.length > 0) {
        shareText += `Phase medals: ${this.completionMedals.map(m => `${m.title} ${m.icon}`).join(', ')}\n`;
      }
      
      shareText += `\nJoin me in protecting coral reefs through sustainable tourism! 🌊\n\n${this.generateShareUrl()}`;
      
      try {
        await navigator.clipboard.writeText(shareText);
        // Show notification briefly
        this.$nextTick(() => {
          alert('Share link copied to clipboard!');
        });
      } catch (err) {
        // Fallback for older browsers
        const textArea = document.createElement('textarea');
        textArea.value = shareText;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand('copy');
        document.body.removeChild(textArea);
        alert('Share link copied to clipboard!');
      }
    },

    generateShareUrl() {
      const baseUrl = window.location.origin;
      const completedCount = this.getTotalCompleted();
      const totalCount = this.getTotalItems();
      const params = new URLSearchParams({
        completed: `${completedCount}/${totalCount}`,
        accuracy: Math.round((completedCount / totalCount) * 100)
      });
      return `${baseUrl}/checklist?${params.toString()}`;
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

 /* Disabled state styles */
 .item-checkbox input[type="checkbox"]:disabled + .checkbox-label {
   opacity: 0.5;
   cursor: not-allowed;
   background: rgba(128, 128, 128, 0.3);
   border-color: rgba(128, 128, 128, 0.5);
 }

 .checkbox-label.disabled {
   opacity: 0.5;
   cursor: not-allowed;
   background: rgba(128, 128, 128, 0.3);
   border-color: rgba(128, 128, 128, 0.5);
 }

 .checkbox-label.disabled .checkmark {
   color: rgba(255, 255, 255, 0.5);
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
 
 /* Completion message styles */
 .completion-message {
   text-align: center;
   padding: 30px;
   background: linear-gradient(135deg, rgba(76, 175, 80, 0.2) 0%, rgba(76, 175, 80, 0.1) 100%);
   border: 2px solid rgba(76, 175, 80, 0.6);
   border-radius: 20px;
   margin-top: 20px;
 }
 
 .badge-icon {
   font-size: 48px;
   margin-bottom: 15px;
   animation: bounce 2s infinite;
 }
 
 .completion-message h3 {
   color: #4CAF50;
   font-size: 24px;
   margin: 0 0 10px 0;
   text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
 }
 
 .completion-message p {
   color: #4CAF50;
   font-size: 16px;
   margin: 0;
   text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
 }
 
 .completion-message strong {
   color: #FFD700;
   text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
 }
 
 /* Badge animation */
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

.completion-earned h3 {
  color: #FFD700;
  font-size: 28px;
  margin: 0 0 10px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.completion-earned p {
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

/* Completion Modal Styles */
.completion-overlay {
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

.completion-modal {
  background: linear-gradient(135deg, rgba(26, 29, 37, 0.95) 0%, rgba(1, 162, 235, 0.9) 100%);
  border-radius: 25px;
  padding: 0;
  max-width: 700px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4);
  animation: slideUp 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.completion-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25px 30px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
}

.completion-header h2 {
  color: white;
  font-size: 24px;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
}

.completion-content {
  padding: 30px;
}

/* Medal Display */
.medal-display {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0.05) 100%);
  border-radius: 20px;
  padding: 40px;
  margin-bottom: 30px;
  text-align: center;
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.medal-icon {
  font-size: 80px;
  margin-bottom: 20px;
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

/* Phase Medals */
.phase-medals {
  margin-bottom: 30px;
}

.phase-medals h3 {
  color: white;
  font-size: 20px;
  margin-bottom: 20px;
  text-align: center;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.phase-medals-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.phase-medal {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  text-align: center;
  border: 1px solid rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
}

.phase-medal-icon {
  font-size: 40px;
  margin-bottom: 10px;
}

.phase-medal h4 {
  color: white;
  font-size: 16px;
  margin: 0 0 8px 0;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.5);
}

.phase-medal p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
  margin: 0;
  line-height: 1.4;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

/* Statistics */
.completion-statistics {
  margin-bottom: 30px;
}

.completion-statistics .stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  margin-bottom: 25px;
}

.completion-statistics .stat-card {
  background: white;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 15px;
  transition: transform 0.3s ease;
}

.completion-statistics .stat-card:hover {
  transform: translateY(-2px);
}

.completion-statistics .stat-card.completed {
  border-left: 4px solid #4CAF50;
}

.completion-statistics .stat-card.remaining {
  border-left: 4px solid #FF9800;
}

.completion-statistics .stat-card.total {
  border-left: 4px solid #667eea;
}

.completion-statistics .stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
}

.completion-statistics .stat-card.completed .stat-icon {
  background: rgba(76, 175, 80, 0.1);
  color: #4CAF50;
}

.completion-statistics .stat-card.remaining .stat-icon {
  background: rgba(255, 152, 0, 0.1);
  color: #FF9800;
}

.completion-statistics .stat-card.total .stat-icon {
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
}

.completion-statistics .stat-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.completion-statistics .stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.completion-statistics .stat-label {
  font-size: 14px;
  color: #666;
  margin-top: 2px;
}

.completion-summary {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  text-align: center;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.completion-summary h4 {
  color: white;
  margin-bottom: 15px;
  font-size: 18px;
  font-weight: 600;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.5);
}

.completion-text, .progress-text {
  font-size: 16px;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  font-weight: 500;
  margin-bottom: 8px;
}

.completion-number, .progress-number {
  font-weight: bold;
  color: #FFD700;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.7);
  font-size: 20px;
}

/* Uncompleted Items Summary */
.uncompleted-summary {
  background: rgba(255, 152, 0, 0.1);
  border: 1px solid rgba(255, 152, 0, 0.3);
  border-radius: 15px;
  padding: 20px;
  margin-bottom: 30px;
}

.uncompleted-summary h4 {
  color: #FFB74D;
  margin-bottom: 10px;
  font-size: 18px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.5);
}

.uncompleted-count {
  color: white;
  margin-bottom: 15px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

.uncompleted-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.uncompleted-item {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.1);
  padding: 10px 15px;
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.uncompleted-title {
  color: white;
  font-size: 14px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.more-items {
  color: rgba(255, 255, 255, 0.7);
  font-style: italic;
  text-align: center;
  padding: 8px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

/* Share Section */
.completion-modal .share-section {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  padding: 25px;
  margin-bottom: 30px;
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  text-align: center;
}

.completion-modal .share-header h3 {
  color: white;
  font-size: 20px;
  margin-bottom: 8px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.completion-modal .share-header p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
  margin-bottom: 20px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.completion-modal .share-options {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.completion-modal .btn-share {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 15px 10px;
  border: none;
  border-radius: 12px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.completion-modal .btn-share:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.completion-modal .btn-share.twitter {
  background: linear-gradient(135deg, #1da1f2 0%, #0d8bd9 100%);
}

.completion-modal .btn-share.facebook {
  background: linear-gradient(135deg, #4267b2 0%, #365899 100%);
}

.completion-modal .btn-share.copy {
  background: linear-gradient(135deg, #28a745 0%, #20953b 100%);
}

.completion-modal .share-icon {
  font-size: 20px;
}

/* Completion Actions */
.completion-actions {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.btn-back-education,
.btn-close-modal {
  padding: 12px 25px;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-back-education {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.btn-back-education:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a5acd 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.btn-close-modal {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.btn-close-modal:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
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

  .completion-modal {
    width: 95%;
    margin: 20px;
  }

  .completion-statistics .stats-grid {
    grid-template-columns: 1fr;
    gap: 10px;
  }

  .completion-modal .share-options {
    grid-template-columns: 1fr;
    gap: 10px;
  }

  .phase-medals-grid {
    grid-template-columns: 1fr;
  }

  .completion-actions {
    flex-direction: column;
    align-items: center;
  }

  .medal-icon {
    font-size: 60px;
  }

  .medal-title {
    font-size: 24px;
  }
}
</style>
