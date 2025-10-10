import Vue from 'vue'
import VueI18n from 'vue-i18n'
import en from './en'
import zh from './zh'
import ms from './ms'

Vue.use(VueI18n)

// 从 localStorage 获取用户之前选择的语言，默认为英文
const savedLocale = localStorage.getItem('userLanguage') || 'en'

const i18n = new VueI18n({
  locale: savedLocale, // 设置默认语言
  fallbackLocale: 'en', // 设置备用语言
  messages: {
    en,
    zh,
    ms
  }
})

export default i18n

