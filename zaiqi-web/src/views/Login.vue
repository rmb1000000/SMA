<template>
  <div class="login-container">
    <div class="login-card">
      <h2 style="text-align:center; margin-bottom:8px;">再启管理后台</h2>
      <p style="text-align:center; color:#999; margin-bottom:30px; font-size:14px;">AI 再婚决策引擎</p>
      <el-form :model="form" :rules="rules" ref="formRef" @keyup.enter="handleLogin">
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" style="width:100%;" :loading="loading" @click="handleLogin">登 录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const auth = useAuthStore()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ phone: '13800000000', password: '' })
const rules = { phone: [{ required: true, message: '请输入手机号' }], password: [{ required: true, message: '请输入密码' }] }

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await auth.login(form.phone, form.password)
    if (res.code === 200) router.push('/dashboard')
    else ElMessage.error(res.message)
  } catch (e) { /* handled by interceptor */ }
  loading.value = false
}
</script>

<style scoped>
.login-container { height:100vh; display:flex; align-items:center; justify-content:center; background:linear-gradient(135deg,#1a1a2e 0%,#16213e 50%,#0f3460 100%); }
.login-card { width:400px; padding:40px; background:white; border-radius:12px; box-shadow:0 20px 60px rgba(0,0,0,0.3); }
</style>
