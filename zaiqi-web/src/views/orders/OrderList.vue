<template>
  <div><h3 style="margin-bottom:20px;">订单管理</h3>
    <el-card><el-table :data="orders" v-loading="loading" style="width:100%">
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="productName" label="商品" />
      <el-table-column label="金额"><template #default="s">{{ '¥'+(s.row.amount/100).toFixed(1) }}</template></el-table-column>
      <el-table-column label="状态"><template #default="s"><el-tag :type="s.row.status===1?'success':'warning'">{{ {0:'待支付',1:'已支付',2:'已退款',3:'已关闭'}[s.row.status] }}</el-tag></template></el-table-column>
      <el-table-column prop="paidTime" label="支付时间" width="180" />
    </el-table></el-card>
  </div>
</template>
<script setup>
import { ref,onMounted } from 'vue'
import request from '../../utils/request'
const orders=ref([]); const loading=ref(false)
onMounted(async ()=>{
  loading.value=true; const r=await request.get('/admin/orders',{params:{page:1,size:50}})
  if(r.code===200) orders.value=r.data.records; loading.value=false
})
</script>
