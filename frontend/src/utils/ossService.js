import { API_BASE_URL } from '@/config/config.js'
import { CDN_CONFIG } from '@/config/cdn.js'

/**
 * OSS service utility class
 * Supports both CDN direct access and OSS signed URL modes
 */
class OssService {
  constructor() {
    this.baseURL = API_BASE_URL
    this.cache = new Map() // Cache signed URLs to avoid duplicate requests
    this.cdnBaseUrl = CDN_CONFIG.baseUrl // CDN base URL
    this.useCDN = CDN_CONFIG.enabled // Whether to use CDN
    this.config = CDN_CONFIG
  }

  /**
   * Get file URL (prefer CDN)
   * @param {string} objectKey - File path in OSS
   * @param {number} expireSeconds - Expiration time (seconds), default 3600 seconds
   * @returns {Promise<string>} File URL
   */
  async getFileUrl(objectKey, expireSeconds = 3600) {
    const startTime = Date.now()
    
    try {
      // Check local cache
      const cachedUrl = this.getCachedUrl(objectKey)
      if (cachedUrl) {
        return cachedUrl
      }
      
      // Force use CDN, no checks performed
      const cdnUrl = this.getCDNUrl(objectKey)
      
      // Cache CDN URL
      this.setCachedUrl(objectKey, cdnUrl)
      
      // Performance monitoring
      if (this.config.monitoring.enabled) {
        const loadTime = Date.now() - startTime
        if (loadTime > this.config.monitoring.slowRequestThreshold) {
          console.warn(`CDN request slow: ${objectKey}, time: ${loadTime}ms`)
        }
      }
      
      console.log(`Using CDN URL: ${cdnUrl}`)
      return cdnUrl
    } catch (error) {
      console.error('Failed to get CDN URL:', error)
      // Even if error occurs, return CDN URL, do not downgrade to OSS
      const cdnUrl = this.getCDNUrl(objectKey)
      console.log(`强制使用CDN URL: ${cdnUrl}`)
      return cdnUrl
    }
  }

  /**
   * 获取CDN文件URL
   * @param {string} objectKey - 文件在OSS中的路径
   * @returns {string} CDN URL
   */
  getCDNUrl(objectKey) {
    return `${this.cdnBaseUrl}/${objectKey}`
  }

  /**
   * 获取OSS文件的签名URL
   * @param {string} objectKey - 文件在OSS中的路径
   * @param {number} expireSeconds - 过期时间（秒），默认3600秒
   * @returns {Promise<string>} 签名URL
   */
  async getSignedUrl(objectKey, expireSeconds = 3600) {
    // No longer request backend OSS signed URL, directly return CDN URL
    console.log(`getSignedUrl被调用，但强制返回CDN URL: ${objectKey}`)
    return this.getCDNUrl(objectKey)
  }

  /**
   * 获取主页背景图片的URL
   * @returns {Promise<string>} 背景图片URL
   */
  async getMainPageBackgroundUrl() {
    return this.getFileUrl('assets/bg_mainpage.jpg')
  }

  /**
   * 获取彩色珊瑚礁图片的URL
   * @returns {Promise<string>} 彩色珊瑚礁图片URL
   */
  async getColorfulCoralUrl() {
    return this.getFileUrl('assets/mainpage_colorful.jpg')
  }

  /**
   * 获取白化珊瑚礁图片的URL
   * @returns {Promise<string>} 白化珊瑚礁图片URL
   */
  async getBleachingCoralUrl() {
    return this.getFileUrl('assets/mainpage_bleading.png')
  }

  /**
   * 获取完全白化珊瑚礁图片的URL
   * @returns {Promise<string>} 完全白化珊瑚礁图片URL
   */
  async getBleachedCoralUrl() {
    return this.getFileUrl('assets/mainpage_bleacheedCoral.webp')
  }

  /**
   * 获取箭头图标的URL
   * @returns {Promise<string>} 箭头图标URL
   */
  async getArrowIconUrl() {
    return this.getFileUrl('assets/icons/arrow.png')
  }

  /**
   * 获取应用图标的URL
   * @returns {Promise<string>} 应用图标URL
   */
  async getAppIconUrl() {
    return this.getFileUrl('assets/icon.png')
  }

  /**
   * 获取AI工具页面背景图片的URL (JPG格式)
   * @returns {Promise<string>} AI工具页面背景图片URL
   */
  async getAIToolsBackgroundUrl() {
    return this.getFileUrl('assets/ai_interface.jpg')
  }

  /**
   * 获取岛屿详情页面背景图片的URL
   * @returns {Promise<string>} 岛屿详情页面背景图片URL
   */
  async getIslandDetailBackgroundUrl() {
    return this.getFileUrl('assets/island.webp')
  }

  /**
   * 获取岛屿轮播图片的URL
   * @param {string} islandName - 岛屿名称
   * @param {number} imageIndex - 图片索引 (1-5)
   * @returns {Promise<string>} 轮播图片URL
   */
  async getIslandCarouselImageUrl(islandName, imageIndex) {
    const imagePath = `assets/${islandName}/pic${imageIndex}.png`
    return this.getFileUrl(imagePath)
  }

  /**
   * 获取岛屿酒店图片的URL
   * @param {string} islandName - 岛屿名称
   * @param {string} hotelName - 酒店名称
   * @returns {Promise<string>} 酒店图片URL
   */
  async getIslandHotelImageUrl(islandName, hotelName) {
    const imagePath = `assets/${islandName}/${hotelName}`
    return this.getFileUrl(imagePath)
  }

  /**
   * 获取岛屿活动图片的URL
   * @param {string} islandName - 岛屿名称
   * @param {string} activityName - 活动名称
   * @returns {Promise<string>} 活动图片URL
   */
  async getIslandActivityImageUrl(islandName, activityName) {
    const imagePath = `assets/${islandName}/${activityName}`
    return this.getFileUrl(imagePath)
  }

  /**
   * 获取P Singa页面所有图片的URL
   * @returns {Promise<Object>} P Singa页面所有图片URL的对象
   */
  async getPSingaImages() {
    try {
      const imagePromises = [
        // Carousel images
        this.getIslandCarouselImageUrl('P Singa', 1),
        this.getIslandCarouselImageUrl('P Singa', 2),
        this.getIslandCarouselImageUrl('P Singa', 3),
        this.getIslandCarouselImageUrl('P Singa', 4),
        this.getIslandCarouselImageUrl('P Singa', 5),
        // Hotel images
        this.getFileUrl('assets/P Singa/Datai Langkawi.jpg'),
        this.getFileUrl('assets/P Singa/The Danna Langkawi.jpg'),
        this.getFileUrl('assets/P Singa/The Frangipani Langkawi Resort & Spa.jpg'),
        this.getFileUrl('assets/P Singa/Ambong ambong.png'),
        // Activity images
        this.getFileUrl('assets/P Singa/tour 1.png'),
        this.getFileUrl('assets/P Singa/tour 2.png')
      ]

      const urls = await Promise.all(imagePromises)
      
      return {
        carousel: urls.slice(0, 5),
        hotels: {
          datai: urls[5],
          danna: urls[6],
          frangipani: urls[7],
          ambong: urls[8]
        },
        activities: {
          tour1: urls[9],
          tour2: urls[10]
        }
      }
    } catch (error) {
      console.error('获取P Singa图片失败:', error)
      throw error
    }
  }

  /**
   * 清除缓存
   */
  clearCache() {
    this.cache.clear()
  }

  /**
   * 获取缓存大小
   * @returns {number} 缓存条目数量
   */
  getCacheSize() {
    return this.cache.size
  }

  /**
   * 切换CDN模式
   * @param {boolean} useCDN - 是否使用CDN
   */
  setCDNMode(useCDN) {
    this.useCDN = useCDN
    console.log(`CDN模式已${useCDN ? '启用' : '禁用'}`)
  }

  /**
   * 获取当前CDN状态
   * @returns {boolean} 是否使用CDN
   */
  isCDNEnabled() {
    return this.useCDN
  }

  /**
   * 获取CDN基础URL
   * @returns {string} CDN基础URL
   */
  getCDNBaseUrl() {
    return this.cdnBaseUrl
  }

  /**
   * 从本地存储获取缓存的URL
   * @param {string} objectKey - 文件路径
   * @returns {string|null} 缓存的URL
   */
  getCachedUrl(objectKey) {
    if (!this.config.cache.enableLocalStorage) return null
    
    try {
      const cacheKey = `cdn_url_${objectKey}`
      const cached = localStorage.getItem(cacheKey)
      if (cached) {
        const { url, timestamp } = JSON.parse(cached)
        // Check if expired
        if (Date.now() - timestamp < this.config.cache.imageCacheTime) {
          return url
        }
        // Delete if expired
        localStorage.removeItem(cacheKey)
      }
    } catch (error) {
      console.warn('读取本地缓存失败:', error)
    }
    return null
  }

  /**
   * 将URL缓存到本地存储
   * @param {string} objectKey - 文件路径
   * @param {string} url - 要缓存的URL
   */
  setCachedUrl(objectKey, url) {
    if (!this.config.cache.enableLocalStorage) return
    
    try {
      const cacheKey = `cdn_url_${objectKey}`
      const cacheData = {
        url,
        timestamp: Date.now()
      }
      localStorage.setItem(cacheKey, JSON.stringify(cacheData))
    } catch (error) {
      console.warn('保存本地缓存失败:', error)
    }
  }

  /**
   * 清除所有本地缓存
   */
  clearLocalCache() {
    if (!this.config.cache.enableLocalStorage) return
    
    try {
      const keys = Object.keys(localStorage)
      keys.forEach(key => {
        if (key.startsWith('cdn_url_')) {
          localStorage.removeItem(key)
        }
      })
      console.log('本地缓存已清除')
    } catch (error) {
      console.warn('清除本地缓存失败:', error)
    }
  }

  /**
   * 检查是否是SSL错误
   * @param {Error} error - 错误对象
   * @returns {boolean} 是否是SSL错误
   */
  isSSLError(error) {
    if (!error) return false
    
    const errorMessage = error.message || error.toString()
    const sslErrorPatterns = [
      'ERR_SSL_VERSION_OR_CIPHER_MISMATCH',
      'ERR_SSL_PROTOCOL_ERROR',
      'ERR_CERT_AUTHORITY_INVALID',
      'ERR_CERT_COMMON_NAME_INVALID',
      'ERR_CERT_DATE_INVALID',
      'SSL_ERROR'
    ]
    
    return sslErrorPatterns.some(pattern => 
      errorMessage.includes(pattern)
    )
  }

  /**
   * 检查CDN URL的SSL错误
   * @param {string} url - CDN URL
   * @returns {Promise<boolean>} 是否有SSL错误
   */
  async checkSSLError(url) {
    try {
      // Create a test image element to detect SSL errors
      return new Promise((resolve) => {
        const img = new Image()
        const timeout = setTimeout(() => {
          resolve(false) // Timeout does not count as SSL error
        }, 3000)
        
        img.onload = () => {
          clearTimeout(timeout)
          resolve(false) // Load successful, no SSL error
        }
        
        img.onerror = (error) => {
          clearTimeout(timeout)
          const isSSL = this.isSSLError(error)
          resolve(isSSL)
        }
        
        img.src = url
      })
    } catch (error) {
      return this.isSSLError(error)
    }
  }
}

// Create singleton instance
const ossService = new OssService()

export default ossService
