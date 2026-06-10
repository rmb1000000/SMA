import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 15000
})

request.interceptors.request.use(config => {
  const token = localStorage.getItem('admin_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

request.interceptors.response.use(
  res => {
    if (res.data.code === 401) {
      localStorage.removeItem('admin_token')
      router.push('/login')
      return Promise.reject(new Error('登录已过期'))
    }
    return res.data
  },
  err => {
    ElMessage.error(err.message || '请求失败')
    return Promise.reject(err)
  }
)

export default request
