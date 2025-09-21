// 环境配置
const config = {
  // 开发环境
  development: {
    baseURL: 'http://localhost:8080',
    frontendPort: 3001
  },
  // 生产环境
  production: {
    baseURL: 'https://iter2.coralkita.site', // 生产环境地址，使用HTTPS，移除末尾斜杠
    frontendPort: 443
  }
};

// 根据当前环境获取配置
const env = process.env.NODE_ENV || 'development';
const currentConfig = config[env];

// 导出配置
export const API_BASE_URL = currentConfig.baseURL;
export const FRONTEND_PORT = currentConfig.frontendPort;

// 也可以导出整个配置对象
export default currentConfig;
