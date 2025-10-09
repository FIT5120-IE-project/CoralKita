<template>
  <div class="language-switcher" @mouseenter="handleMouseEnter" @mouseleave="handleMouseLeave">
    <div class="current-language">
      <span class="current-lang-code">{{ currentLanguageCode }}</span>
      <span class="language-text">{{ currentLanguageName }}</span>
      <span class="dropdown-arrow">▼</span>
    </div>
    
    <div class="language-dropdown" v-show="showDropdown" @mouseenter="handleDropdownEnter" @mouseleave="handleDropdownLeave">
      <div 
        v-for="lang in languages" 
        :key="lang.code"
        class="language-option"
        :class="{ active: $i18n.locale === lang.code }"
        @click="changeLanguage(lang.code)"
      >
        <span class="lang-flag">{{ lang.flag }}</span>
        <span class="lang-name">{{ lang.name }}</span>
        <span class="lang-check" v-if="$i18n.locale === lang.code">✓</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LanguageSwitcher',
  data() {
    return {
      showDropdown: false,
      hoverTimeout: null,
      languages: [
        { code: 'en', name: 'English', flag: 'EN' },
        { code: 'zh', name: '中文', flag: 'ZH' },
        { code: 'ms', name: 'Bahasa Melayu', flag: 'MS' }
      ]
    }
  },
  computed: {
    currentLanguageName() {
      const current = this.languages.find(lang => lang.code === this.$i18n.locale)
      return current ? current.name : 'English'
    },
    currentLanguageCode() {
      const current = this.languages.find(lang => lang.code === this.$i18n.locale)
      return current ? current.flag : 'EN'
    }
  },
  methods: {
    handleMouseEnter() {
      // 清除任何待执行的隐藏操作
      if (this.hoverTimeout) {
        clearTimeout(this.hoverTimeout)
        this.hoverTimeout = null
      }
      this.showDropdown = true
    },
    
    handleMouseLeave() {
      // 延迟隐藏，给用户时间移动到下拉菜单
      this.hoverTimeout = setTimeout(() => {
        this.showDropdown = false
      }, 150) // 150ms 延迟
    },
    
    handleDropdownEnter() {
      // 鼠标进入下拉菜单，确保显示
      if (this.hoverTimeout) {
        clearTimeout(this.hoverTimeout)
        this.hoverTimeout = null
      }
      this.showDropdown = true
    },
    
    handleDropdownLeave() {
      // 鼠标离开下拉菜单，隐藏
      this.showDropdown = false
    },
    
    changeLanguage(locale) {
      this.$i18n.locale = locale
      // 保存到 localStorage
      localStorage.setItem('userLanguage', locale)
      this.showDropdown = false
      
      // 清除任何待执行的隐藏操作
      if (this.hoverTimeout) {
        clearTimeout(this.hoverTimeout)
        this.hoverTimeout = null
      }
      
      // 触发全局事件通知其他组件
      this.$root.$emit('languageChanged', locale)
      
      // 显示切换成功提示（可选）
      console.log(`Language changed to: ${locale}`)
    }
  },
  
  beforeDestroy() {
    // 组件销毁前清理定时器
    if (this.hoverTimeout) {
      clearTimeout(this.hoverTimeout)
    }
  }
}
</script>

<style scoped>
.language-switcher {
  position: relative;
  cursor: pointer;
  user-select: none;
}

.current-language {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  color: rgba(255, 255, 255, 0.7);
  transition: all 0.3s ease;
  border-radius: 8px;
  font-weight: 500;
}

.current-language:hover {
  color: white;
  background: rgba(255, 255, 255, 0.1);
}

.current-lang-code {
  font-size: 0.7rem;
  font-weight: bold;
  background: linear-gradient(135deg, #3b82f6, #10b981);
  color: white;
  padding: 3px 6px;
  border-radius: 4px;
  min-width: 24px;
  text-align: center;
  letter-spacing: 0.5px;
}

.language-text {
  font-size: 0.95rem;
}

.dropdown-arrow {
  font-size: 0.7rem;
  margin-left: 2px;
  transition: transform 0.3s ease;
}

.language-switcher:hover .dropdown-arrow {
  transform: rotate(180deg);
}

.language-dropdown {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(25px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15), 0 4px 16px rgba(0, 0, 0, 0.1);
  min-width: 180px;
  z-index: 1001;
  overflow: hidden;
  animation: dropdownSlideIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes dropdownSlideIn {
  from {
    opacity: 0;
    transform: translateY(-8px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.language-option {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 20px;
  color: #1f2937;
  cursor: pointer;
  transition: all 0.2s ease;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  font-weight: 500;
}

.language-option:last-child {
  border-bottom: none;
}

.language-option:hover {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.08), rgba(16, 185, 129, 0.08));
  color: #1e40af;
  transform: translateX(2px);
}

.language-option.active {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.12), rgba(16, 185, 129, 0.12));
  color: #1e40af;
  font-weight: 600;
}

.language-option:first-child:hover {
  border-radius: 16px 16px 0 0;
}

.language-option:last-child:hover {
  border-radius: 0 0 16px 16px;
}

.language-option:first-child:last-child:hover {
  border-radius: 16px;
}

.lang-flag {
  font-size: 0.8rem;
  font-weight: bold;
  background: linear-gradient(135deg, #3b82f6, #10b981);
  color: white;
  padding: 4px 8px;
  border-radius: 6px;
  min-width: 32px;
  text-align: center;
  letter-spacing: 0.5px;
}

.lang-name {
  flex: 1;
}

.lang-check {
  color: #10b981;
  font-weight: bold;
  font-size: 1.2rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .current-language {
    padding: 6px 12px;
  }
  
  .language-text {
    display: none;
  }
  
  .language-dropdown {
    min-width: 160px;
  }
  
  .language-option {
    padding: 12px 16px;
  }
}
</style>

