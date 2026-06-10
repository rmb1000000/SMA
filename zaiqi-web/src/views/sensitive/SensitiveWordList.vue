<template>
  <div><h3 style="margin-bottom:20px;">敏感词管理</h3>
    <el-card>
      <div style="margin-bottom:15px;">
        <el-input v-model="newWord" placeholder="输入敏感词" style="width:200px;margin-right:10px;" />
        <el-button type="primary" @click="addWord">添加</el-button>
      </div>
      <el-table :data="list" v-loading="loading" style="width:100%">
        <el-table-column prop="word" label="敏感词" />
        <el-table-column prop="scope" label="作用范围" />
        <el-table-column label="操作" width="100"><template #default="s"><el-button type="danger" size="small" @click="delWord(s.row.id)">删除</el-button></template></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref,onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'
const list=ref([]); const loading=ref(false); const newWord=ref('')
const load=async()=>{ const r=await request.get('/admin/sensitive-words',{params:{page:1,size:100}}); if(r.code===200) list.value=r.data.records }
onMounted(async()=>{ loading.value=true; await load(); loading.value=false })
const addWord=async()=>{ if(!newWord.value)return; await request.post(`/admin/sensitive-words?word=${newWord.value}`); ElMessage.success('添加成功'); newWord.value=''; await load() }
const delWord=async id=>{ await request.delete(`/admin/sensitive-words/${id}`); ElMessage.success('删除成功'); await load() }
</script>
