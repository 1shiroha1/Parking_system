<template>
  <div>
    <h3>停车账单</h3>

    <table border="1" cellpadding="6" cellspacing="0" style="width: 100%; border-collapse: collapse;">
      <thead>
        <tr>
          <th>账单ID</th>
          <th>车牌</th>
          <th>车位ID</th>
          <th>金额(元)</th>
          <th>状态</th>
          <th>入场时间</th>
          <th>出场时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="b in bills" :key="b.id">
          <td>{{ b.id }}</td>
          <td>{{ b.plateNumber }}</td>
          <td>{{ b.spaceId }}</td>
          <td>{{ b.amount }}</td>
          <td>{{ billStatusZh[b.status] || b.status }}</td>
          <td>{{ b.entryTime }}</td>
          <td>{{ b.exitTime }}</td>
          <td>
            <button v-if="b.status === 'UNPAID'" @click="pay(b.id)" :disabled="payingId === b.id">
              {{ payingId === b.id ? '支付中...' : '支付' }}
            </button>
            <span v-else>--</span>
          </td>
        </tr>
        <tr v-if="bills.length === 0">
          <td colspan="8">暂无账单</td>
        </tr>
      </tbody>
    </table>

    <div style="color: #b00; margin-top: 12px;" v-if="error">{{ error }}</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { listMyBills, payBillMock } from '../api/bill'
import { billStatusZh } from '../utils/dict'

const bills = ref([])
const error = ref('')
const payingId = ref(null)

async function refresh() {
  error.value = ''
  bills.value = await listMyBills()
}

async function pay(billId) {
  error.value = ''
  payingId.value = billId
  try {
    await payBillMock(billId)
    await refresh()
  } catch (e) {
    error.value = e?.response?.data?.message || '支付失败'
  } finally {
    payingId.value = null
  }
}

onMounted(() => {
  refresh()
})
</script>

