<template>
  <div>
    <h3>车辆管理</h3>

    <div style="border: 1px solid #eee; padding: 12px; margin-bottom: 16px;">
      <div style="margin-bottom: 8px;">
        <input v-model="form.plateNumber" placeholder="车牌号" style="width: 100%; padding: 8px;" />
      </div>
      <div style="display: flex; gap: 8px; margin-bottom: 8px;">
        <input v-model="form.brand" placeholder="品牌" style="flex: 1; padding: 8px;" />
        <input v-model="form.color" placeholder="颜色" style="flex: 1; padding: 8px;" />
      </div>
      <div style="margin-bottom: 8px;">
        <input v-model="form.vehicleType" placeholder="车型" style="width: 100%; padding: 8px;" />
      </div>
      <button @click="onCreateVehicle" :disabled="loading">
        {{ loading ? '保存中...' : '添加车辆' }}
      </button>
      <div style="color: #b00; margin-top: 8px;" v-if="error">{{ error }}</div>
    </div>

    <table border="1" cellpadding="6" cellspacing="0" style="width: 100%; border-collapse: collapse;">
      <thead>
        <tr>
          <th>编号</th>
          <th>车牌</th>
          <th>品牌</th>
          <th>颜色</th>
          <th>车型</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="v in vehicles" :key="v.id">
          <td>{{ v.id }}</td>
          <td>{{ v.plateNumber }}</td>
          <td>{{ v.brand }}</td>
          <td>{{ v.color }}</td>
          <td>{{ v.vehicleType }}</td>
        </tr>
        <tr v-if="vehicles.length === 0">
          <td colspan="5">暂无数据</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { createVehicle, listVehicles } from '../api/vehicle'

const vehicles = ref([])
const loading = ref(false)
const error = ref('')
const form = reactive({
  plateNumber: '',
  brand: '',
  color: '',
  vehicleType: ''
})

async function refresh() {
  error.value = ''
  vehicles.value = await listVehicles()
}

async function onCreateVehicle() {
  error.value = ''
  loading.value = true
  try {
    await createVehicle(form)
    form.plateNumber = ''
    form.brand = ''
    form.color = ''
    form.vehicleType = ''
    await refresh()
  } catch (e) {
    error.value = e?.response?.data?.message || '保存失败'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  refresh()
})
</script>

