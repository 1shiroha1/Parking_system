<template>
  <div>
    <h3>我的维修工单</h3>

    <table border="1" cellpadding="6" cellspacing="0" style="width: 100%; border-collapse: collapse;">
      <thead>
        <tr>
          <th>工单ID</th>
          <th>车位</th>
          <th>描述</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="t in tickets" :key="t.id">
          <td>{{ t.id }}</td>
          <td>{{ t.parkingSpaceId }}</td>
          <td>{{ t.description }}</td>
          <td>{{ repairTicketStatusZh[t.status] || t.status }}</td>
          <td>
            <div style="display: flex; gap: 8px; align-items: center;">
              <select v-model="updateMap[t.id].status" style="padding: 6px;">
                <option value="OPEN">待处理</option>
                <option value="IN_PROGRESS">处理中</option>
                <option value="DONE">已完成</option>
                <option value="CLOSED">已关闭</option>
              </select>
              <input v-model="updateMap[t.id].repairCost" placeholder="费用" style="width: 90px; padding: 6px;" />
              <input v-model="updateMap[t.id].handlerNotes" placeholder="备注" style="width: 120px; padding: 6px;" />
              <button @click="update(t.id)" :disabled="loading">保存</button>
            </div>
          </td>
        </tr>
        <tr v-if="tickets.length === 0">
          <td colspan="5">暂无工单</td>
        </tr>
      </tbody>
    </table>

    <div style="color: #b00; margin-top: 12px;" v-if="error">{{ error }}</div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { listMyRepairTickets, updateRepairTicket } from '../api/repair'
import { repairTicketStatusZh } from '../utils/dict'

const tickets = ref([])
const loading = ref(false)
const error = ref('')

const updateMap = reactive({})

function initUpdateMap(t) {
  updateMap[t.id] = {
    status: t.status || 'OPEN',
    repairCost: t.repairCost || 0,
    handlerNotes: t.handlerNotes || ''
  }
}

async function refresh() {
  error.value = ''
  tickets.value = await listMyRepairTickets()
  tickets.value.forEach((t) => {
    if (!updateMap[t.id]) initUpdateMap(t)
  })
}

async function update(ticketId) {
  error.value = ''
  loading.value = true
  try {
    const payload = {
      status: updateMap[ticketId].status,
      repairCost: updateMap[ticketId].repairCost === '' ? null : Number(updateMap[ticketId].repairCost),
      handlerNotes: updateMap[ticketId].handlerNotes
    }
    await updateRepairTicket(ticketId, payload)
    await refresh()
  } catch (e) {
    error.value = e?.response?.data?.message || '更新失败'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  refresh()
})
</script>

