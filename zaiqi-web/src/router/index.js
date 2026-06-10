import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  {
    path: '/',
    component: () => import('../views/layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/Dashboard.vue'), meta: { title: '仪表盘' } },
      { path: 'users', component: () => import('../views/users/UserList.vue'), meta: { title: '会员管理' } },
      { path: 'orders', component: () => import('../views/orders/OrderList.vue'), meta: { title: '订单管理' } },
      { path: 'posts', component: () => import('../views/posts/PostList.vue'), meta: { title: '动态管理' } },
      { path: 'feedbacks', component: () => import('../views/feedbacks/FeedbackList.vue'), meta: { title: '意见反馈' } },
      { path: 'sensitive-words', component: () => import('../views/sensitive/SensitiveWordList.vue'), meta: { title: '敏感词管理' } },
      { path: 'admins', component: () => import('../views/admins/AdminList.vue'), meta: { title: '管理员管理' } },
      { path: 'logs', component: () => import('../views/logs/LogList.vue'), meta: { title: '操作日志' } },
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('admin_token')
  if (to.path !== '/login' && !token) next('/login')
  else next()
})

export default router
