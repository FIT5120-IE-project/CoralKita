import axios from 'axios'
import { API_BASE_URL } from '@/config/config'

// 创建axios实例
const api = axios.create({
  baseURL: API_BASE_URL, // 从配置文件获取后端服务地址
  timeout: 10000
})

// 请求拦截器 - 添加token
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 处理错误
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response && error.response.status === 401) {
      // token过期，清除本地存储并跳转到登录页
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// 用户登录
export const userLogin = (data) => {
  return api.post('/user/login', data)
}

// 用户注册
export const userRegister = (data) => {
  return api.post('/user/register', data)
}

// 用户退出
export const userLogout = () => {
  return api.post('/user/logout')
}

export default api
