<template>
  <div>
    <h3>用户管理（管理员）</h3>
    <table border="1" cellpadding="6" cellspacing="0" style="width: 100%; border-collapse: collapse;">
      <thead>
        <tr>
          <th>编号</th>
          <th>用户名</th>
          <th>昵称</th>
          <th>角色</th>
          <th>启用</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="u in users" :key="u.id">
          <td>{{ u.id }}</td>
          <td>{{ u.username }}</td>
          <td>{{ u.displayName }}</td>
          <td>{{ roleZh[u.role] || u.role }}</td>
          <td>{{ u.enabled }}</td>
          <td>
            <button @click="toggle(u)" :disabled="loading">
              {{ u.enabled ? '禁用' : '启用' }}
            </button>
          </td>
        </tr>
        <tr v-if="users.length === 0">
          <td colspan="6">暂无用户</td>
        </tr>
      </tbody>
    </table>
    <div style="color: #b00; margin-top: 12px;" v-if="error">{{ error }}</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { listAdminUsers, setUserEnabled } from '../api/users'
import { roleZh } from '../utils/dict'

const users = ref([])
const loading = ref(false)
const error = ref('')

async function refresh() {
  error.value = ''
  users.value = await listAdminUsers()
}

async function toggle(u) {
  error.value = ''
  loading.value = true
  try {
    await setUserEnabled(u.id, !u.enabled)
    await refresh()
  } catch (e) {
    error.value = e?.response?.data?.message || '操作失败'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  refresh()
})
</script>

