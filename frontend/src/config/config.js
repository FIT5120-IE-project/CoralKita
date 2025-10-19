// Environment configuration
const config = {
  // Development environment
  development: {
    baseURL: 'http://localhost:8080',
    frontendPort: 3001
  },
  // Production environment
  production: {
    baseURL: 'https://www.coralkita.site', // Production environment address, using HTTPS, removed trailing slash
    frontendPort: 443
  }
};

// Get configuration based on current environment
const env = process.env.NODE_ENV || 'development';
const currentConfig = config[env];

// Export configuration
export const API_BASE_URL = currentConfig.baseURL;
export const FRONTEND_PORT = currentConfig.frontendPort;

// Can also export the entire configuration object
export default currentConfig;
