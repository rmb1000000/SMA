import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../utils/request'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('admin_token') || '')
  const user = ref(JSON.parse(localStorage.getItem('admin_user') || '{}'))

  const login = async (phone, password) => {
    const res = await request.post('/admin/auth/login', { phone, password })
    if (res.code === 200) {
      token.value = res.data.accessToken
      user.value = { id: res.data.userId, name: res.data.nickname }
      localStorage.setItem('admin_token', res.data.accessToken)
      localStorage.setItem('admin_user', JSON.stringify(user.value))
    }
    return res
  }

  const logout = () => {
    token.value = ''
    user.value = {}
    localStorage.removeItem('admin_token')
    localStorage.removeItem('admin_user')
  }

  return { token, user, login, logout }
})
