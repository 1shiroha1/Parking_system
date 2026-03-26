<template>
  <div>
    <h3>车位预约</h3>

    <div style="border: 1px solid #eee; padding: 12px; margin-bottom: 16px;">
      <div style="margin-bottom: 8px;">
        <label>停车场</label>
        <select v-model="form.lotId" style="width: 100%; padding: 8px;" @change="refreshSpaces">
          <option v-for="lot in lots" :key="lot.id" :value="lot.id">{{ lot.name }} ({{ lot.id }})</option>
        </select>
      </div>

      <div style="margin-bottom: 8px;">
        <label>车位</label>
        <select v-model="form.spaceId" style="width: 100%; padding: 8px;">
          <option v-for="s in spaces" :key="s.id" :value="s.id">
            {{ s.code }} - {{ spaceStatusZh[s.statusNow] || s.statusNow }}
          </option>
        </select>
      </div>

      <div style="display: flex; gap: 8px; margin-bottom: 8px;">
        <div style="flex: 1;">
          <label>开始时间</label>
          <input type="datetime-local" v-model="form.startTime" style="width: 100%; padding: 8px;" />
        </div>
        <div style="flex: 1;">
          <label>结束时间</label>
          <input type="datetime-local" v-model="form.endTime" style="width: 100%; padding: 8px;" />
        </div>
      </div>

      <button @click="create" :disabled="loading">
        {{ loading ? '创建中...' : '创建预约' }}
      </button>
      <div style="color: #b00; margin-top: 8px;" v-if="error">{{ error }}</div>
    </div>

    <table border="1" cellpadding="6" cellspacing="0" style="width: 100%; border-collapse: collapse;">
      <thead>
        <tr>
          <th>预约ID</th>
          <th>车位</th>
          <th>开始时间</th>
          <th>结束时间</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="r in reservations" :key="r.id">
          <td>{{ r.id }}</td>
          <td>{{ r.spaceId }}</td>
          <td>{{ r.startTime }}</td>
          <td>{{ r.endTime }}</td>
          <td>{{ reservationStatusZh[r.status] || r.status }}</td>
          <td>
            <button v-if="r.status === 'CONFIRMED'" @click="cancel(r.id)">取消</button>
            <span v-else>—</span>
          </td>
        </tr>
        <tr v-if="reservations.length === 0">
          <td colspan="6">暂无数据</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { listLots, listSpaces } from '../api/parking'
import { cancelReservation, createReservation, listMyReservations } from '../api/reservation'
import { reservationStatusZh, spaceStatusZh } from '../utils/dict'

const lots = ref([])
const spaces = ref([])
const reservations = ref([])

const loading = ref(false)
const error = ref('')

const form = reactive({
  lotId: null,
  spaceId: null,
  startTime: '',
  endTime: ''
})

function toBackendDateTime(v) {
  // datetime-local: 'YYYY-MM-DDTHH:mm' -> 'YYYY-MM-DD HH:mm:ss'
  if (!v) return null
  const [datePart, timePart] = v.split('T')
  const normalizedTime = timePart.length === 5 ? `${timePart}:00` : timePart
  return `${datePart} ${normalizedTime}`
}

async function refreshSpaces() {
  if (!form.lotId) return
  spaces.value = await listSpaces(form.lotId)
  form.spaceId = spaces.value[0]?.id || null
}

async function refreshReservations() {
  reservations.value = await listMyReservations()
}

async function create() {
  error.value = ''
  loading.value = true
  try {
    const payload = {
      spaceId: form.spaceId,
      startTime: toBackendDateTime(form.startTime),
      endTime: toBackendDateTime(form.endTime)
    }
    await createReservation(payload)
    form.startTime = ''
    form.endTime = ''
    await refreshReservations()
  } catch (e) {
    error.value = e?.response?.data?.message || '创建预约失败'
  } finally {
    loading.value = false
  }
}

async function cancel(reservationId) {
  error.value = ''
  loading.value = true
  try {
    await cancelReservation(reservationId, '用户取消预约')
    await refreshReservations()
  } catch (e) {
    error.value = e?.response?.data?.message || '取消预约失败'
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  lots.value = await listLots()
  form.lotId = lots.value[0]?.id || null
  await refreshSpaces()
  await refreshReservations()
})
</script>

