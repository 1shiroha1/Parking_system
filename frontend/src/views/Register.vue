<template>
  <div style="max-width: 420px; margin: 60px auto; padding: 16px; border: 1px solid #eee;">
    <h2>注册</h2>

    <div style="margin-bottom: 8px;">
      <input v-model="username" placeholder="用户名" style="width: 100%; padding: 8px;" />
    </div>
    <div style="margin-bottom: 8px;">
      <input v-model="password" type="password" placeholder="密码" style="width: 100%; padding: 8px;" />
    </div>
    <div style="margin-bottom: 8px;">
      <input v-model="displayName" placeholder="昵称（可选）" style="width: 100%; padding: 8px;" />
    </div>
    <div style="margin-bottom: 8px;">
      <button style="width: 100%; padding: 10px;" @click="doRegister" :disabled="loading">
        {{ loading ? '注册中...' : '注册' }}
      </button>
    </div>
    <div style="color: #b00; margin-bottom: 8px;" v-if="error">{{ error }}</div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()

const username = ref('')
const password = ref('')
const displayName = ref('')
const loading = ref(false)
const error = ref('')

async function doRegister() {
  error.value = ''
  loading.value = true
  try {
    await userStore.doRegister({ username: username.value, password: password.value, displayName: displayName.value })
    router.push('/login')
  } catch (e) {
    error.value = e?.response?.data?.message || '注册失败'
  } finally {
    loading.value = false
  }
}
</script>

