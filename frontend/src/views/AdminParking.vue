<template>
  <div>
    <h3>车位管理（管理员）</h3>

    <div style="border: 1px solid #eee; padding: 12px; margin-bottom: 16px;">
      <div style="margin-bottom: 8px;">
        <label>停车场</label>
        <select v-model="form.lotId" style="width: 100%; padding: 8px;" @change="refreshSpaces">
          <option v-for="lot in lots" :key="lot.id" :value="lot.id">{{ lot.name }} ({{ lot.id }})</option>
        </select>
      </div>

      <div style="margin-bottom: 12px;">
        <h4 style="margin: 12px 0 8px;">创建停车场</h4>
        <input v-model="lotCreate.name" placeholder="停车场名称" style="width: 100%; padding: 8px; margin-bottom: 8px;" />
        <input v-model="lotCreate.address" placeholder="地址" style="width: 100%; padding: 8px;" />
        <div style="margin-top: 8px;">
          <button @click="onCreateLot" :disabled="loading">创建</button>
        </div>
      </div>

      <div style="margin-bottom: 12px;">
        <h4 style="margin: 12px 0 8px;">创建车位</h4>
        <input v-model="spaceCreate.code" placeholder="车位编码（如 A1）" style="width: 100%; padding: 8px; margin-bottom: 8px;" />
        <button @click="onCreateSpace" :disabled="loading || !form.lotId">创建</button>
      </div>

      <div style="color: #b00;" v-if="error">{{ error }}</div>
    </div>

    <table border="1" cellpadding="6" cellspacing="0" style="width: 100%; border-collapse: collapse;">
      <thead>
        <tr>
          <th>车位编号</th>
          <th>车位编码</th>
          <th>当前状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="s in spaces" :key="s.id">
          <td>{{ s.id }}</td>
          <td>{{ s.code }}</td>
          <td>{{ spaceStatusZh[s.statusNow] || s.statusNow }}</td>
          <td>
            <button v-if="s.statusNow !== 'REPAIR'" @click="setStatus(s.id, 'REPAIR')" :disabled="loading">
              标记维修
            </button>
            <button v-else @click="setStatus(s.id, 'AVAILABLE')" :disabled="loading">
              设为可用
            </button>
          </td>
        </tr>
        <tr v-if="spaces.length === 0">
          <td colspan="4">暂无车位</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { createLot, createSpace, listLots, listSpaces, setSpaceRepairStatus } from '../api/parking'
import { spaceStatusZh } from '../utils/dict'

const lots = ref([])
const spaces = ref([])

const loading = ref(false)
const error = ref('')

const form = reactive({
  lotId: null
})

const lotCreate = reactive({
  name: '',
  address: ''
})

const spaceCreate = reactive({
  code: ''
})

async function refreshLots() {
  lots.value = await listLots()
  if (!form.lotId && lots.value.length > 0) form.lotId = lots.value[0].id
}

async function refreshSpaces() {
  if (!form.lotId) return
  spaces.value = await listSpaces(form.lotId)
}

async function onCreateLot() {
  error.value = ''
  loading.value = true
  try {
    await createLot({ name: lotCreate.name, address: lotCreate.address })
    lotCreate.name = ''
    lotCreate.address = ''
    await refreshLots()
    await refreshSpaces()
  } catch (e) {
    error.value = e?.response?.data?.message || '创建停车场失败'
  } finally {
    loading.value = false
  }
}

async function onCreateSpace() {
  if (!form.lotId) return
  error.value = ''
  loading.value = true
  try {
    await createSpace({ lotId: form.lotId, code: spaceCreate.code })
    spaceCreate.code = ''
    await refreshSpaces()
  } catch (e) {
    error.value = e?.response?.data?.message || '创建车位失败'
  } finally {
    loading.value = false
  }
}

async function setStatus(spaceId, status) {
  error.value = ''
  loading.value = true
  try {
    await setSpaceRepairStatus(spaceId, status)
    await refreshSpaces()
  } catch (e) {
    error.value = e?.response?.data?.message || '更新状态失败'
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await refreshLots()
  await refreshSpaces()
})
</script>

