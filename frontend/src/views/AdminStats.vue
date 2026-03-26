<template>
  <div>
    <h3>统计报表</h3>

    <div style="border: 1px solid #eee; padding: 12px; margin-bottom: 16px;">
      <div style="display: flex; gap: 16px; flex-wrap: wrap;">
        <div style="min-width: 200px;">
          <div style="color: #666;">停车位数量</div>
          <div style="font-size: 22px; font-weight: 700;">{{ stats.parkingSpaceCount }}</div>
        </div>
        <div style="min-width: 200px;">
          <div style="color: #666;">利用率</div>
          <div style="font-size: 22px; font-weight: 700;">{{ stats.utilizationRate }}%</div>
        </div>
        <div style="min-width: 200px;">
          <div style="color: #666;">统计天数</div>
          <div style="font-size: 22px; font-weight: 700;">{{ stats.days }}</div>
        </div>
      </div>

      <div style="margin-top: 12px;">
        <label>天数：</label>
        <select v-model.number="days" @change="loadStats" style="padding: 6px;">
          <option :value="3">3</option>
          <option :value="7">7</option>
          <option :value="14">14</option>
          <option :value="30">30</option>
        </select>
      </div>
    </div>

    <div style="border: 1px solid #eee; padding: 12px; margin-bottom: 16px;">
      <h4>收入（已支付账单）</h4>
      <table border="1" cellpadding="6" cellspacing="0" style="width: 100%; border-collapse: collapse;">
        <thead>
          <tr>
            <th>日期</th>
            <th>金额（元）</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in stats.revenueByDay" :key="r.date">
            <td>{{ r.date }}</td>
            <td>{{ r.amount }}</td>
          </tr>
          <tr v-if="!stats.revenueByDay || stats.revenueByDay.length === 0">
            <td colspan="2">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div style="border: 1px solid #eee; padding: 12px; margin-bottom: 16px;">
      <h4>预约量（已确认）</h4>
      <table border="1" cellpadding="6" cellspacing="0" style="width: 100%; border-collapse: collapse;">
        <thead>
          <tr>
            <th>日期</th>
            <th>数量</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="r in stats.reservationByDay" :key="r.date">
            <td>{{ r.date }}</td>
            <td>{{ r.count }}</td>
          </tr>
          <tr v-if="!stats.reservationByDay || stats.reservationByDay.length === 0">
            <td colspan="2">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div style="border: 1px solid #eee; padding: 12px;">
      <h4>维修工单（按状态）</h4>
      <div v-if="stats.repairTicketByStatus && Object.keys(stats.repairTicketByStatus).length > 0">
        <table border="1" cellpadding="6" cellspacing="0" style="width: 100%; border-collapse: collapse;">
          <thead>
            <tr>
              <th>状态</th>
              <th>数量</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(v, k) in stats.repairTicketByStatus" :key="k">
              <td>{{ repairTicketStatusZh[k] || k }}</td>
              <td>{{ v }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-else>暂无数据</div>
    </div>

    <div style="color: #b00; margin-top: 12px;" v-if="error">{{ error }}</div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { getAdminStats } from '../api/stats'
import { repairTicketStatusZh } from '../utils/dict'

const days = ref(7)

const stats = reactive({
  days: 7,
  parkingSpaceCount: 0,
  utilizationRate: 0,
  revenueByDay: [],
  reservationByDay: [],
  repairTicketByStatus: {}
})

const error = ref('')

async function loadStats() {
  error.value = ''
  try {
    const data = await getAdminStats(days.value)
    stats.days = data.days
    stats.parkingSpaceCount = data.parkingSpaceCount
    stats.utilizationRate = data.utilizationRate
    stats.revenueByDay = data.revenueByDay || []
    stats.reservationByDay = data.reservationByDay || []
    stats.repairTicketByStatus = data.repairTicketByStatus || {}
  } catch (e) {
    error.value = e?.response?.data?.message || '加载统计失败'
  }
}

onMounted(() => {
  loadStats()
})
</script>

