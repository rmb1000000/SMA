<template>
  <div>
    <h3 style="margin-bottom:20px;">仪表盘</h3>
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in stats" :key="item.label">
        <el-card shadow="hover" style="margin-bottom:20px;">
          <div style="text-align:center;">
            <div style="color:#999;font-size:14px;">{{ item.label }}</div>
            <div style="font-size:28px;font-weight:bold;color:#333;margin-top:5px;">{{ item.value }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-card>
      <template #header><span>最新订单</span></template>
      <el-table :data="orders" style="width:100%" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column prop="productName" label="商品" />
        <el-table-column label="金额"><template #default="s">{{ '¥' + (s.row.amount/100).toFixed(1) }}</template></el-table-column>
        <el-table-column label="状态"><template #default="s"><el-tag :type="s.row.status===1?'success':'warning'">{{ s.row.status===1?'已支付':'待支付' }}</el-tag></template></el-table-column>
        <el-table-column prop="createTime" label="时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'
const loading = ref(false)
const orders = ref([])
const stats = reactive([{label:'总用户数',value:0},{label:'今日新增',value:0},{label:'高级会员',value:0},{label:'当日收入',value:0}])
onMounted(async () => {
  const s = await request.get('/admin/dashboard/stats')
  if (s.code === 200) { stats[0].value=s.data.totalUsers; stats[1].value=s.data.todayNewUsers; stats[2].value=s.data.vipUsers; }
  const o = await request.get('/admin/orders',{params:{page:1,size:10}})
  if (o.code === 200) orders.value = o.data.records
})
</script>
