import Vue from 'vue'
import VueI18n from 'vue-i18n'
import en from './en'
import zh from './zh'
import ms from './ms'

Vue.use(VueI18n)

// Get user's previously selected language from localStorage, default to English
const savedLocale = localStorage.getItem('userLanguage') || 'en'

const i18n = new VueI18n({
  locale: savedLocale, // Set default language
  fallbackLocale: 'en', // Set fallback language
  messages: {
    en,
    zh,
    ms
  }
})

export default i18n

