import axios from 'axios'
import { API_BASE_URL } from '@/config/config'

// 创建axios实例 - 用于非用户认证相关的API调用
const api = axios.create({
  baseURL: API_BASE_URL, // 从配置文件获取后端服务地址
  timeout: 10000
})

// 请求拦截器 - 简化版，无需token认证
api.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 简化版，移除认证错误处理
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    return Promise.reject(error)
  }
)

export default api
