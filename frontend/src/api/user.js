import axios from 'axios'
import { API_BASE_URL } from '@/config/config'

// Create axios instance - for non-user authentication related API calls
const api = axios.create({
  baseURL: API_BASE_URL, // Get backend service address from config file
  timeout: 10000
})

// Request interceptor - simplified version, no token authentication required
api.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// Response interceptor - simplified version, removed authentication error handling
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    return Promise.reject(error)
  }
)

export default api
