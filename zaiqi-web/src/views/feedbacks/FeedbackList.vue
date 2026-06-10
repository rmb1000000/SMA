<template>
  <div><h3 style="margin-bottom:20px;">意见反馈</h3>
    <el-card><el-table :data="list" v-loading="loading" style="width:100%">
      <el-table-column prop="content" label="反馈内容" min-width="200" show-overflow-tooltip />
      <el-table-column label="状态"><template #default="s"><el-tag :type="s.row.status===0?'warning':'success'">{{ s.row.status===0?'待处理':'已处理' }}</el-tag></template></el-table-column>
      <el-table-column prop="createTime" label="时间" width="180" />
      <el-table-column label="操作" width="150"><template #default="s">
        <el-button size="small" type="primary" v-if="s.row.status===0" @click="showReply(s.row)">回复</el-button>
      </template></el-table-column>
    </el-table></el-card>
    <el-dialog v-model="dv" title="回复反馈" width="400">
      <el-input v-model="reply" type="textarea" :rows="4" placeholder="请输入回复" />
      <template #footer><el-button @click="dv=false">取消</el-button><el-button type="primary" @click="handleReply">确认</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref,onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'
const list=ref([]); const loading=ref(false); const dv=ref(false); const reply=ref(''); const curId=ref(null)
onMounted(async ()=>{ loading.value=true; const r=await request.get('/admin/feedbacks',{params:{page:1,size:50}})
  if(r.code===200) list.value=r.data.records; loading.value=false })
const showReply=r=>{ curId.value=r.id; reply.value=''; dv.value=true }
const handleReply=async()=>{
  await request.post(`/admin/feedbacks/${curId.value}/reply?reply=${reply.value}`)
  ElMessage.success('回复成功'); dv.value=false
  const r=await request.get('/admin/feedbacks',{params:{page:1,size:50}})
  if(r.code===200) list.value=r.data.records }
</script>
