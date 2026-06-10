<template>
  <div><h3 style="margin-bottom:20px;">管理员管理</h3>
    <el-card>
      <div style="margin-bottom:15px;">
        <el-input v-model="form.username" placeholder="用户名" style="width:150px;margin-right:10px;" />
        <el-input v-model="form.phone" placeholder="手机号" style="width:150px;margin-right:10px;" />
        <el-input v-model="form.password" type="password" placeholder="密码" style="width:150px;margin-right:10px;" show-password />
        <el-button type="primary" @click="addAdmin">添加管理员</el-button>
      </div>
      <el-table :data="list" v-loading="loading" style="width:100%">
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column label="角色"><template #default="s">{{ s.row.role===1?'超级管理员':'普通管理员' }}</template></el-table-column>
        <el-table-column label="状态"><template #default="s"><el-tag :type="s.row.status===1?'success':'danger'">{{ s.row.status===1?'正常':'已禁用' }}</el-tag></template></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref,reactive,onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'
const list=ref([]); const loading=ref(false); const form=reactive({username:'',phone:'',password:''})
onMounted(async()=>{ loading.value=true; await load(); loading.value=false })
const load=async()=>{ const r=await request.get('/admin/users',{params:{page:1,size:50}})
  if(r.code===200) list.value=r.data.records }
const addAdmin=async()=>{
  if(!form.username||!form.phone||!form.password)return ElMessage.warning('请填写完整')
  await request.post('/admin/auth/admin-users',form)
  ElMessage.success('添加成功'); form.username=''; form.phone=''; form.password=''; await load() }
</script>
