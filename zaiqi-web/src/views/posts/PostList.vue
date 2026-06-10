<template>
  <div><h3 style="margin-bottom:20px;">动态管理</h3>
    <el-card><el-table :data="list" v-loading="loading" style="width:100%">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="content" label="内容" min-width="250" show-overflow-tooltip />
      <el-table-column prop="likeCount" label="点赞" width="60" />
      <el-table-column prop="commentCount" label="评论" width="60" />
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column label="操作" width="100"><template #default="s"><el-button type="danger" size="small" @click="del(s.row.id)">删除</el-button></template></el-table-column>
    </el-table></el-card>
  </div>
</template>
<script setup>
import { ref,onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'
const list=ref([]); const loading=ref(false)
onMounted(async()=>{ loading.value=true; const r=await request.get('/admin/posts',{params:{page:1,size:50}})
  if(r.code===200) list.value=r.data.records; loading.value=false })
const del=async id=>{ await request.delete(`/admin/posts/${id}`); ElMessage.success('已删除');
  const r=await request.get('/admin/posts',{params:{page:1,size:50}}); if(r.code===200) list.value=r.data.records }
</script>
