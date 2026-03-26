<template>
  <div>
    <h3>维修工单（管理员）</h3>

    <div style="border: 1px solid #eee; padding: 12px; margin-bottom: 16px;">
      <h4>创建维修人员</h4>
      <div style="display: flex; gap: 8px; margin-bottom: 8px;">
        <input v-model="workerForm.username" placeholder="用户名" style="flex: 1; padding: 8px;" />
        <input v-model="workerForm.password" type="password" placeholder="密码" style="flex: 1; padding: 8px;" />
      </div>
      <div style="display: flex; gap: 8px; margin-bottom: 8px;">
        <input v-model="workerForm.workerName" placeholder="姓名" style="flex: 1; padding: 8px;" />
        <input v-model="workerForm.phone" placeholder="电话" style="flex: 1; padding: 8px;" />
      </div>
      <button @click="createWorker" :disabled="loading">创建维修人员</button>
    </div>

    <div style="border: 1px solid #eee; padding: 12px; margin-bottom: 16px;">
      <h4>创建维修工单</h4>
      <div style="margin-bottom: 8px;">
        <label>停车场</label>
        <select v-model="ticketForm.lotId" style="width: 100%; padding: 8px;" @change="refreshSpaces">
          <option v-for="lot in lots" :key="lot.id" :value="lot.id">{{ lot.name }} ({{ lot.id }})</option>
        </select>
      </div>
      <div style="margin-bottom: 8px;">
        <label>车位</label>
        <select v-model="ticketForm.parkingSpaceId" style="width: 100%; padding: 8px;">
          <option v-for="s in spaces" :key="s.id" :value="s.id">{{ s.code }} - {{ spaceStatusZh[s.statusNow] || s.statusNow }}</option>
        </select>
      </div>
      <div style="margin-bottom: 8px;">
        <label>指派维修人员ID（可选）</label>
        <select v-model="ticketForm.assignedWorkerUserId" style="width: 100%; padding: 8px;">
          <option :value="null">未指派</option>
          <option v-for="w in repairWorkers" :key="w.id" :value="w.id">{{ w.workerName ? w.workerName : (w.displayName || w.username) }} ({{ w.id }})</option>
        </select>
      </div>
      <div style="margin-bottom: 8px;">
        <input v-model="ticketForm.description" placeholder="故障/维修描述" style="width: 100%; padding: 8px;" />
      </div>
      <button @click="createTicket" :disabled="loading">创建工单</button>

      <div style="color: #b00; margin-top: 8px;" v-if="error">{{ error }}</div>
    </div>

    <table border="1" cellpadding="6" cellspacing="0" style="width: 100%; border-collapse: collapse;">
      <thead>
        <tr>
          <th>工单ID</th>
          <th>车位</th>
          <th>描述</th>
          <th>状态</th>
          <th>指派维修人员</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="t in tickets" :key="t.id">
          <td>{{ t.id }}</td>
          <td>{{ t.parkingSpaceId }}</td>
          <td>{{ t.description }}</td>
          <td>{{ repairTicketStatusZh[t.status] || t.status }}</td>
          <td>{{ t.assignedWorkerUserId }}</td>
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
          <td colspan="6">暂无工单</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { listLots, listSpaces } from '../api/parking'
import { createRepairTicket, createRepairWorker, listAdminRepairTickets, updateRepairTicket } from '../api/repair'
import { listAdminUsers } from '../api/users'
import { repairTicketStatusZh, spaceStatusZh } from '../utils/dict'

const loading = ref(false)
const error = ref('')

const lots = ref([])
const spaces = ref([])
const repairWorkers = ref([])
const tickets = ref([])

const workerForm = reactive({
  username: '',
  password: '',
  workerName: '',
  phone: ''
})

const ticketForm = reactive({
  lotId: null,
  parkingSpaceId: null,
  assignedWorkerUserId: null,
  description: ''
})

const updateMap = reactive({})

function initUpdateMap(t) {
  updateMap[t.id] = {
    status: t.status || 'OPEN',
    repairCost: t.repairCost || 0,
    handlerNotes: t.handlerNotes || ''
  }
}

async function refreshSpaces() {
  if (!ticketForm.lotId) return
  spaces.value = await listSpaces(ticketForm.lotId)
  ticketForm.parkingSpaceId = spaces.value[0]?.id || null
}

async function refreshAll() {
  tickets.value = await listAdminRepairTickets()
  tickets.value.forEach((t) => {
    if (!updateMap[t.id]) initUpdateMap(t)
  })
  const allUsers = await listAdminUsers()
  repairWorkers.value = allUsers.filter((u) => u.role === 'REPAIR')
}

async function createWorker() {
  error.value = ''
  loading.value = true
  try {
    await createRepairWorker(workerForm)
    workerForm.username = ''
    workerForm.password = ''
    workerForm.workerName = ''
    workerForm.phone = ''
    await refreshAll()
  } catch (e) {
    error.value = e?.response?.data?.message || '创建维修人员失败'
  } finally {
    loading.value = false
  }
}

async function createTicket() {
  error.value = ''
  loading.value = true
  try {
    const payload = {
      parkingLotId: null,
      parkingSpaceId: ticketForm.parkingSpaceId,
      assignedWorkerUserId: ticketForm.assignedWorkerUserId,
      description: ticketForm.description
    }
    await createRepairTicket(payload)
    ticketForm.description = ''
    await refreshAll()
  } catch (e) {
    error.value = e?.response?.data?.message || '创建工单失败'
  } finally {
    loading.value = false
  }
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
    await refreshAll()
  } catch (e) {
    error.value = e?.response?.data?.message || '更新失败'
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  lots.value = await listLots()
  ticketForm.lotId = lots.value[0]?.id || null
  await refreshSpaces()
  await refreshAll()
})
</script>

