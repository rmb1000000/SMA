<template>
  <el-container style="height:100vh;">
    <el-header style="background:#1a1a2e; display:flex; align-items:center; justify-content:space-between; padding:0 20px; height:60px;">
      <span style="color:white; font-size:18px; font-weight:bold;">再启 · 管理后台</span>
      <el-dropdown trigger="click">
        <span style="color:white; cursor:pointer; display:flex; align-items:center; gap:5px;">
          <el-icon><User /></el-icon> {{ auth.user.name || '管理员' }}
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
        </template>
      </el-dropdown>
    </el-header>
    <el-container>
      <el-aside width="220px" style="background:#f5f5f5; border-right:1px solid #e0e0e0;">
        <el-menu :default-active="route.path" router style="border-right:none; background:transparent;">
          <el-menu-item index="/dashboard"><el-icon><DataAnalysis /></el-icon>仪表盘</el-menu-item>
          <el-menu-item index="/users"><el-icon><User /></el-icon>会员管理</el-menu-item>
          <el-menu-item index="/orders"><el-icon><List /></el-icon>订单管理</el-menu-item>
          <el-menu-item index="/posts"><el-icon><Collection /></el-icon>动态管理</el-menu-item>
          <el-menu-item index="/feedbacks"><el-icon><ChatDotSquare /></el-icon>意见反馈</el-menu-item>
          <el-menu-item index="/sensitive-words"><el-icon><Lock /></el-icon>敏感词管理</el-menu-item>
          <el-menu-item index="/admins"><el-icon><Setting /></el-icon>管理员管理</el-menu-item>
          <el-menu-item index="/logs"><el-icon><Document /></el-icon>操作日志</el-menu-item>
        </el-menu>
      </el-aside>
      <el-main style="background:#f0f2f5; padding:20px;"><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const handleLogout = () => { auth.logout(); router.push('/login') }
</script>
