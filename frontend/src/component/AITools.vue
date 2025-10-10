<template>
  <div class="ai-classification-container">
    <!-- Top Navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <!-- Left side logo -->
        <img :src="appIconUrl" alt="logo" class="nav-logo" @click="goToHome" />
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
    <div class="ai-classification-header">
      <h1>{{ $t('aiTools.title') }}</h1>
      <p>{{ $t('aiTools.subtitle') }}</p>
      <div class="disclaimer">
        <p>{{ $t('aiTools.disclaimer.warning') }}</p>
        <p><strong>{{ $t('aiTools.disclaimer.accuracy') }}</strong></p>
      </div>
    </div>

    <!-- Main Content Area -->
    <div class="ai-classification-content">
      <!-- 上传区域 -->
      <div class="upload-section">
        <div class="upload-container">
          <div class="upload-area" 
               :class="{ 'drag-over': isDragOver, 'has-image': selectedImage }"
               @dragover.prevent="handleDragOver"
               @dragleave.prevent="handleDragLeave"
               @drop.prevent="handleDrop"
               @click="triggerFileInput">
            
            <!-- 上传提示 -->
            <div v-if="!selectedImage" class="upload-prompt">
              <div class="upload-icon">📸</div>
              <h3>{{ $t('aiTools.upload.title') }}</h3>
              <p>{{ $t('aiTools.upload.description') }}</p>
              <div class="upload-formats">
                <span class="format-tag" v-for="format in $t('aiTools.upload.formats')" :key="format">{{ format }}</span>
              </div>
              <p class="file-size-limit">{{ $t('aiTools.upload.fileSizeLimit') }}</p>
            </div>

            <!-- 图片预览 -->
            <div v-if="selectedImage" class="image-preview">
              <img :src="imagePreview" alt="Selected coral image" />
              <div class="image-overlay">
                <button class="change-image-btn" @click.stop="triggerFileInput">
                  <span>🔄</span> {{ $t('aiTools.upload.changeImage') }}
                </button>
                <button class="remove-image-btn" @click.stop="removeImage">
                  <span>❌</span> {{ $t('aiTools.upload.remove') }}
                </button>
              </div>
            </div>

            <!-- 隐藏的文件输入 -->
            <input 
              ref="fileInput"
              type="file" 
              accept="image/*" 
              @change="handleFileSelect"
              style="display: none"
            />
          </div>

          <!-- 上传按钮 -->
          <div class="upload-actions">
            <button 
              class="classify-btn" 
              :disabled="!selectedImage || isClassifying"
              @click="classifyImage">
              <span v-if="isClassifying" class="loading-spinner"></span>
              <span v-else>🔍</span>
              {{ isClassifying ? $t('aiTools.upload.classifying') : $t('aiTools.upload.classify') }}
            </button>
          </div>
        </div>
      </div>

      <!-- 结果区域 -->
      <div v-if="classificationResult" class="result-section">
        <div class="result-container">
          <h3>{{ $t('aiTools.results.title') }}</h3>
          <div class="result-card">
            <div class="result-header">
              <div class="result-icon">🐠</div>
              <div class="result-info">
                <h4>{{ classificationResult }}</h4>
                <p>{{ $t('aiTools.results.species') }}</p>
              </div>
            </div>
            <div class="result-actions">
              <button class="new-classification-btn" @click="resetClassification">
                <span>🔄</span> {{ $t('aiTools.results.classifyAnother') }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 错误弹窗 -->
      <div v-if="errorMessage" class="modal-overlay" @click="closeErrorModal">
        <div class="modal-dialog" @click.stop>
          <div class="modal-header">
            <div class="modal-icon">⚠️</div>
            <h3>{{ $t('aiTools.error.title') }}</h3>
            <button class="modal-close" @click="closeErrorModal">{{ $t('aiTools.error.close') }}</button>
          </div>
          <div class="modal-body">
            <p>{{ errorMessage }}</p>
          </div>
          <div class="modal-footer">
            <button class="modal-btn" @click="closeErrorModal">确定</button>
          </div>
        </div>
      </div>

      <!-- 使用说明 -->
      <div class="instructions-section">
        <div class="instructions-container">
          <h3>{{ $t('aiTools.howToUse.title') }}</h3>
          <div class="instructions-grid">
            <div class="instruction-item">
              <div class="instruction-icon">1️⃣</div>
              <h4>{{ $t('aiTools.howToUse.step1.title') }}</h4>
              <p>{{ $t('aiTools.howToUse.step1.description') }}</p>
            </div>
            <div class="instruction-item">
              <div class="instruction-icon">2️⃣</div>
              <h4>{{ $t('aiTools.howToUse.step2.title') }}</h4>
              <p>{{ $t('aiTools.howToUse.step2.description') }}</p>
            </div>
            <div class="instruction-item">
              <div class="instruction-icon">3️⃣</div>
              <h4>{{ $t('aiTools.howToUse.step3.title') }}</h4>
              <p>{{ $t('aiTools.howToUse.step3.description') }}</p>
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
import { API_BASE_URL } from '../config/config.js'
import ossService from '@/utils/ossService.js'

import LanguageSwitcher from './LanguageSwitcher.vue'

export default {
  name: 'AIClassification',
  components: {
    LanguageSwitcher
  },
  data() {
    return {
      showTravelDropdown: false,
      showEducationDropdown: false,
      currentLanguage: 'en',
      travelIslands: ['Mertang', 'P Singa', 'Sipadan', 'Pulau Lima', 'Seri Buat'],
      // 图片上传相关
      selectedImage: null,
      imagePreview: null,
      isDragOver: false,
      isClassifying: false,
      classificationResult: null,
      errorMessage: null,
      
      // 背景图片URL
      backgroundImageUrl: null,
      
      // 应用图标URL
      appIconUrl: null
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
    
    goToActionHub() {
      this.$router.push('/action-hub').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToPolicies() {
      this.$router.push('/policies').catch(err => {
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

    toggleLanguage() {
      this.currentLanguage = this.currentLanguage === 'en' ? 'zh' : 'en'
    },

    // 文件上传相关方法
    triggerFileInput() {
      this.$refs.fileInput.click()
    },

    handleFileSelect(event) {
      const file = event.target.files[0]
      if (file) {
        this.processFile(file)
      }
    },

    handleDragOver(event) {
      event.preventDefault()
      this.isDragOver = true
    },

    handleDragLeave(event) {
      event.preventDefault()
      this.isDragOver = false
    },

    handleDrop(event) {
      event.preventDefault()
      this.isDragOver = false
      
      const files = event.dataTransfer.files
      if (files.length > 0) {
        this.processFile(files[0])
      }
    },

    processFile(file) {
      // 验证文件类型
      if (!file.type.startsWith('image/')) {
        this.errorMessage = 'Please select a valid image file'
        return
      }

      // 验证文件大小 (3MB)
      if (file.size > 3 * 1024 * 1024) {
        this.errorMessage = 'Image file size must not exceed 3MB'
        return
      }

      this.selectedImage = file
      this.errorMessage = null
      this.classificationResult = null

      // 创建预览
      const reader = new FileReader()
      reader.onload = (e) => {
        this.imagePreview = e.target.result
      }
      reader.readAsDataURL(file)
    },

    removeImage() {
      this.selectedImage = null
      this.imagePreview = null
      this.classificationResult = null
      this.errorMessage = null
      this.$refs.fileInput.value = ''
    },

    async classifyImage() {
      if (!this.selectedImage) return

      this.isClassifying = true
      this.errorMessage = null
      this.classificationResult = null

      try {
        const formData = new FormData()
        formData.append('image', this.selectedImage)

        const response = await fetch(`${API_BASE_URL}/predict/upload`, {
          method: 'POST',
          body: formData
        })

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }

        const result = await response.json()
        
        if (result.code === 1 && result.data) {
          this.classificationResult = result.data
        } else {
          this.errorMessage = result.msg || result.message || 'Classification failed'
        }
      } catch (error) {
        console.error('Classification error:', error)
        
        // 尝试解析错误响应
        if (error.response) {
          try {
            const errorResult = await error.response.json()
            this.errorMessage = errorResult.msg || errorResult.message || 'Classification failed'
          } catch (parseError) {
            this.errorMessage = 'Failed to classify image. Please try again.'
          }
        } else {
          this.errorMessage = 'Failed to classify image. Please try again.'
        }
      } finally {
        this.isClassifying = false
      }
    },

    resetClassification() {
      this.removeImage()
    },

    closeErrorModal() {
      this.errorMessage = null
    },

    /**
     * 加载应用图标
     */
    async loadAppIcon() {
      try {
        this.appIconUrl = await ossService.getAppIconUrl()
      } catch (error) {
        console.warn('加载应用图标失败，使用默认图标:', error)
        this.appIconUrl = null
      }
    },

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
    }
  },
  
  mounted() {
    console.log('AI Classification page loaded')
    
    // 加载应用图标
    this.loadAppIcon()
    
    // 加载背景图片
    this.loadBackgroundImage()
  }
}
</script>

<style scoped>
.ai-classification-container {
  margin: 0;
  min-height: 100vh;
  background-image: var(--bg-image, url("http://static.coralkita.site/ai_interface.webp"));
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-position: center;
  background-size: cover;
  overflow-x: hidden;
  padding: 0;
}

/* 导航栏样式 - 与Education页面保持一致 */
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
  z-index: 1001;
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

.ai-classification-header {
  position: relative;
  z-index: 2;
  text-align: center;
  color: white;
  padding: 120px 20px 18px;
  margin-bottom: 24px;
  margin-top: 0;
}

.ai-classification-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="waves" x="0" y="0" width="100" height="100" patternUnits="userSpaceOnUse"><path d="M0,50 Q25,30 50,50 T100,50 L100,100 L0,100 Z" fill="rgba(255,255,255,0.1)"/></pattern></defs><rect width="100" height="100" fill="url(%23waves)"/></svg>');
  opacity: 0.3;
}

.ai-classification-header h1 {
  font-size: 3rem;
  margin-bottom: 15px;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.8);
  font-weight: 700;
}

.ai-classification-header p {
  font-size: 1.3rem;
  opacity: 0.95;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.6);
  margin-bottom: 25px;
}

.disclaimer {
  margin-top: 20px;
  position: relative;
  z-index: 2;
}

.disclaimer p {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1), 0 4px 16px rgba(0, 0, 0, 0.05);
  padding: 18px 24px;
  margin: 12px 0;
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.95);
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
  transition: all 0.3s ease;
}

.disclaimer p:last-child {
  margin-bottom: 0;
}

.disclaimer p:hover {
  background: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.5);
  transform: translateY(-2px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15), 0 6px 20px rgba(0, 0, 0, 0.1);
}

.ai-classification-content {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 上传区域样式 */
.upload-section {
  margin-bottom: 40px;
}

.upload-container {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.upload-area {
  border: 3px dashed #3282b8;
  border-radius: 15px;
  padding: 60px 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(50, 130, 184, 0.05);
  min-height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.upload-area:hover {
  border-color: #0f4c75;
  background: rgba(50, 130, 184, 0.1);
  transform: translateY(-2px);
}

.upload-area.drag-over {
  border-color: #0f4c75;
  background: rgba(50, 130, 184, 0.15);
  transform: scale(1.02);
}

.upload-area.has-image {
  border-style: solid;
  border-color: #28a745;
  background: rgba(40, 167, 69, 0.05);
  padding: 0;
  min-height: auto;
}

.upload-prompt {
  color: #0f4c75;
}

.upload-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.upload-prompt h3 {
  font-size: 1.8rem;
  margin-bottom: 10px;
  color: #0f4c75;
}

.upload-prompt p {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 20px;
}

.upload-formats {
  display: flex;
  gap: 10px;
  justify-content: center;
}

.format-tag {
  background: #3282b8;
  color: white;
  padding: 6px 12px;
  border-radius: 15px;
  font-size: 0.9rem;
  font-weight: 500;
}

.file-size-limit {
  color: #000;
  font-size: 0.85rem;
  margin-top: 8px;
  text-align: center;
  font-weight: bold;
}

.image-preview {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 15px;
  overflow: hidden;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.image-preview:hover .image-overlay {
  opacity: 1;
}

.change-image-btn,
.remove-image-btn {
  background: rgba(255, 255, 255, 0.9);
  border: none;
  padding: 10px 15px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 5px;
}

.change-image-btn:hover {
  background: #3282b8;
  color: white;
  transform: translateY(-2px);
}

.remove-image-btn:hover {
  background: #dc3545;
  color: white;
  transform: translateY(-2px);
}

.upload-actions {
  margin-top: 30px;
  text-align: center;
}

.classify-btn {
  background: linear-gradient(135deg, #3282b8, #0f4c75);
  color: white;
  border: none;
  padding: 15px 30px;
  border-radius: 25px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  min-width: 200px;
  justify-content: center;
}

.classify-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(50, 130, 184, 0.4);
}

.classify-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}


.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 结果区域样式 */
.result-section {
  margin-bottom: 40px;
}

.result-container {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.result-container h3 {
  color: #0f4c75;
  font-size: 1.8rem;
  margin-bottom: 20px;
  text-align: center;
}

.result-card {
  background: linear-gradient(135deg, #28a745, #20c997);
  color: white;
  border-radius: 15px;
  padding: 25px;
  text-align: center;
}

.result-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  margin-bottom: 20px;
}

.result-icon {
  font-size: 3rem;
}

.result-info h4 {
  font-size: 1.8rem;
  margin-bottom: 5px;
}

.result-info p {
  opacity: 0.9;
  font-size: 1rem;
}

.result-actions {
  text-align: center;
}

.new-classification-btn {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.3);
  padding: 10px 20px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.new-classification-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
  transform: translateY(-2px);
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal-dialog {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  max-width: 400px;
  width: 90%;
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.modal-header {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px 25px;
  border-bottom: 1px solid #eee;
  position: relative;
}

.modal-icon {
  font-size: 2rem;
}

.modal-header h3 {
  color: #dc3545;
  font-size: 1.3rem;
  margin: 0;
  flex: 1;
}

.modal-close {
  position: absolute;
  top: 15px;
  right: 20px;
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #999;
  transition: color 0.3s ease;
}

.modal-close:hover {
  color: #dc3545;
}

.modal-body {
  padding: 20px 25px;
}

.modal-body p {
  color: #666;
  font-size: 1rem;
  line-height: 1.5;
  margin: 0;
}

.modal-footer {
  padding: 15px 25px 25px;
  text-align: center;
}

.modal-btn {
  background: #dc3545;
  color: white;
  border: none;
  padding: 10px 30px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.modal-btn:hover {
  background: #c82333;
  transform: translateY(-2px);
}

/* 使用说明样式 */
.instructions-section {
  margin-bottom: 40px;
}

.instructions-container {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.instructions-container h3 {
  color: #0f4c75;
  font-size: 2rem;
  margin-bottom: 30px;
  text-align: center;
}

.instructions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.instruction-item {
  text-align: center;
  padding: 20px;
}

.instruction-icon {
  font-size: 3rem;
  margin-bottom: 15px;
}

.instruction-item h4 {
  color: #0f4c75;
  font-size: 1.3rem;
  margin-bottom: 10px;
}

.instruction-item p {
  color: #666;
  line-height: 1.6;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .nav-items {
    gap: 20px;
  }
  
  .nav-item {
    font-size: 0.9rem;
    padding: 6px 12px;
  }
}

@media (max-width: 768px) {
  .top-nav {
    padding: 8px 16px;
  }
  
  .nav-left .logo {
    font-size: 1.3rem;
  }
  
  .nav-logo {
    height: 38px;
  }
  
  .nav-items {
    gap: 16px;
    flex-wrap: wrap;
  }
  
  .nav-item {
    font-size: 0.85rem;
    padding: 4px 8px;
  }
  
  .lang-btn {
    padding: 4px 8px;
    font-size: 0.8rem;
    min-width: 35px;
  }
  
  .ai-classification-header h1 {
    font-size: 2.2rem;
  }
  
  .ai-classification-header p {
    font-size: 1.1rem;
  }
  
  .disclaimer p {
    font-size: 0.9rem;
    padding: 10px 15px;
  }
  
  .ai-classification-header {
    padding: 100px 20px 40px;
  }
  
  .ai-classification-content {
    padding: 20px 15px;
  }
  
  .upload-container {
    padding: 20px;
  }
  
  .upload-area {
    padding: 40px 15px;
    min-height: 250px;
  }
  
  .upload-icon {
    font-size: 3rem;
  }
  
  .upload-prompt h3 {
    font-size: 1.5rem;
  }
  
  .instructions-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .error-container {
    flex-direction: column;
    text-align: center;
    gap: 15px;
  }
  
  .result-header {
    flex-direction: column;
    gap: 15px;
  }
}

@media (max-width: 480px) {
  .nav-items {
    gap: 8px;
  }
  
  .nav-item {
    font-size: 0.75rem;
    padding: 4px 6px;
  }
  
  /* Map & Recommendation换行显示 */
  .nav-item-wrapper .nav-item {
    font-size: 0.7rem;
    text-align: center;
    line-height: 1.2;
    padding: 2px 4px;
  }
  
  .ai-classification-header h1 {
    font-size: 1.8rem;
  }
  
  .upload-container {
    padding: 15px;
  }
  
  .upload-area {
    padding: 30px 10px;
    min-height: 200px;
  }
  
  .upload-icon {
    font-size: 2.5rem;
  }
  
  .upload-prompt h3 {
    font-size: 1.3rem;
  }
  
  .classify-btn {
    min-width: 150px;
    padding: 12px 20px;
    font-size: 1rem;
  }
  
  .instruction-icon {
    font-size: 2.5rem;
  }
  
  .result-icon {
    font-size: 2.5rem;
  }
  
  .result-info h4 {
    font-size: 1.5rem;
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
