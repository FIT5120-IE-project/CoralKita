/**
 * CDN配置文件
 * 管理CDN相关设置
 */

export const CDN_CONFIG = {
  // CDN基础URL
  baseUrl: 'http://static.coralkita.site',
  
  // 是否启用CDN
  enabled: true,
  
  // 缓存配置
  cache: {
    // 图片缓存时间（毫秒）
    imageCacheTime: 365 * 24 * 60 * 60 * 1000, // 1年
    
    // 最大缓存条目数
    maxCacheSize: 1000,
    
    // 是否启用本地存储缓存
    enableLocalStorage: true
  },
  
  // 降级配置
  fallback: {
    // 当CDN失败时是否降级到OSS签名URL
    enableFallback: true,
    
    // 降级重试次数
    retryCount: 3,
    
    // 降级重试延迟（毫秒）
    retryDelay: 1000,
    
    // SSL错误时是否自动降级
    enableSSLFallback: true,
    
    // 检测到SSL错误时是否禁用CDN
    disableCDNOnSSLError: true
  },
  
  // 性能监控
  monitoring: {
    // 是否启用性能监控
    enabled: true,
    
    // 慢请求阈值（毫秒）
    slowRequestThreshold: 2000
  }
}

export default CDN_CONFIG
