<template>
  <div>
    <h3>进出场管理（管理员）</h3>

    <div style="border: 1px solid #eee; padding: 12px; margin-bottom: 16px;">
      <div style="margin-bottom: 8px;">
        <label>车辆</label>
        <select v-model="form.vehicleId" style="width: 100%; padding: 8px;">
          <option v-for="v in vehicles" :key="v.id" :value="v.id">
            {{ v.plateNumber }} ({{ v.id }})
          </option>
        </select>
      </div>

      <div style="display: flex; gap: 8px; margin-bottom: 8px;">
        <div style="flex: 1;">
          <label>停车场</label>
          <select v-model="form.lotId" style="width: 100%; padding: 8px;" @change="refreshSpaces">
            <option v-for="lot in lots" :key="lot.id" :value="lot.id">{{ lot.name }} ({{ lot.id }})</option>
          </select>
        </div>
        <div style="flex: 1;">
          <label>车位</label>
          <select v-model="form.spaceId" style="width: 100%; padding: 8px;">
            <option v-for="s in spaces" :key="s.id" :value="s.id">
              {{ s.code }} - {{ spaceStatusZh[s.statusNow] || s.statusNow }}
            </option>
          </select>
        </div>
      </div>

      <div style="display: flex; gap: 8px; margin-bottom: 8px;">
        <div style="flex: 1;">
          <label>入场时间</label>
          <input type="datetime-local" v-model="form.entryTime" style="width: 100%; padding: 8px;" />
        </div>
        <div style="flex: 1;">
          <label>出场时间</label>
          <input type="datetime-local" v-model="form.exitTime" style="width: 100%; padding: 8px;" />
        </div>
      </div>

      <div style="margin-bottom: 8px;">
        <label>预约ID（可选）</label>
        <input v-model="form.reservationId" placeholder="例如：1" style="width: 100%; padding: 8px;" />
      </div>

      <button @click="create" :disabled="loading">
        {{ loading ? '创建中...' : '创建记录并生成账单' }}
      </button>

      <div style="color: #b00; margin-top: 8px;" v-if="error">{{ error }}</div>

      <div v-if="result" style="margin-top: 12px;">
        <div><b>记录ID：</b> {{ result.id }}</div>
        <div><b>账单ID：</b> {{ result.billId }}</div>
        <div><b>金额：</b> {{ result.amount }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { listLots, listSpaces } from '../api/parking'
import { listVehicles } from '../api/vehicle'
import { adminCreateLog } from '../api/entryExit'
import { spaceStatusZh } from '../utils/dict'

const vehicles = ref([])
const lots = ref([])
const spaces = ref([])

const loading = ref(false)
const error = ref('')
const result = ref(null)

const form = reactive({
  vehicleId: null,
  lotId: null,
  spaceId: null,
  entryTime: '',
  exitTime: '',
  reservationId: ''
})

function toBackendDateTime(v) {
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

async function create() {
  error.value = ''
  loading.value = true
  try {
    const payload = {
      vehicleId: form.vehicleId,
      spaceId: form.spaceId,
      entryTime: toBackendDateTime(form.entryTime),
      exitTime: toBackendDateTime(form.exitTime),
      reservationId: form.reservationId ? Number(form.reservationId) : null
    }
    result.value = await adminCreateLog(payload)
  } catch (e) {
    error.value = e?.response?.data?.message || '创建失败'
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  vehicles.value = await listVehicles()
  lots.value = await listLots()
  form.lotId = lots.value[0]?.id || null
  await refreshSpaces()
  form.vehicleId = vehicles.value[0]?.id || null
})
</script>

