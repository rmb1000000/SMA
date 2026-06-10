<template>
  <div>
    <h3 style="margin-bottom:20px;">会员管理</h3>
    <el-card>
      <el-table :data="users" v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="ID" width="100" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column label="会员等级"><template #default="s">{{ s.row.level===1?'高级':'基础' }}</template></el-table-column>
        <el-table-column label="状态"><template #default="s"><el-tag :type="s.row.status===1?'success':'danger'">{{ s.row.status===1?'正常':'已禁用' }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="s">
            <el-button :type="s.row.status===1?'danger':'success'" size="small" @click="toggleStatus(s.row)">{{ s.row.status===1?'禁用':'启用' }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../utils/request'
import { ElMessage } from 'element-plus'
const users = ref([])
const loading = ref(false)
onMounted(async () => {
  loading.value = true
  const res = await request.get('/admin/users',{params:{page:1,size:50}})
  if (res.code === 200) users.value = res.data.records
  loading.value = false
})
const toggleStatus = async (row) => {
  await request.put(`/admin/users/${row.id}/status?status=${row.status===1?0:1}`)
  row.status = row.status === 1 ? 0 : 1
  ElMessage.success('操作成功')
}
</script>
