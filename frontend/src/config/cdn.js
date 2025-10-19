/**
 * CDN configuration file
 * Manage CDN related settings
 */

export const CDN_CONFIG = {
  // CDN base URL
  baseUrl: 'http://static.coralkita.site',
  
  // Whether to enable CDN
  enabled: true,
  
  // Cache configuration
  cache: {
    // Image cache time (milliseconds)
    imageCacheTime: 365 * 24 * 60 * 60 * 1000, // 1 year
    
    // Maximum cache entries
    maxCacheSize: 1000,
    
    // Whether to enable local storage cache
    enableLocalStorage: true
  },
  
  // Fallback configuration
  fallback: {
    // Whether to fallback to OSS signed URL when CDN fails
    enableFallback: true,
    
    // Fallback retry count
    retryCount: 3,
    
    // Fallback retry delay (milliseconds)
    retryDelay: 1000,
    
    // SSL错误时是否自动降级
    enableSSLFallback: true,
    
    // Whether to disable CDN when SSL error is detected
    disableCDNOnSSLError: true
  },
  
  // Performance monitoring
  monitoring: {
    // Whether to enable performance monitoring
    enabled: true,
    
    // Slow request threshold (milliseconds)
    slowRequestThreshold: 2000
  }
}

export default CDN_CONFIG
