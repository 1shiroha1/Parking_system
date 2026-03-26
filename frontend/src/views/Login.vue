<template>
  <div style="max-width: 420px; margin: 60px auto; padding: 16px; border: 1px solid #eee;">
    <h2>登录</h2>
    <div style="margin-bottom: 8px;">
      <input v-model="username" placeholder="用户名" style="width: 100%; padding: 8px;" />
    </div>
    <div style="margin-bottom: 8px;">
      <input v-model="password" type="password" placeholder="密码" style="width: 100%; padding: 8px;" />
    </div>
    <div style="margin-bottom: 8px;">
      <button style="width: 100%; padding: 10px;" @click="doLogin" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>
    </div>
    <div style="color: #b00; margin-bottom: 8px;" v-if="error">{{ error }}</div>
    <div>
      <router-link to="/register">注册</router-link>
    </div>

    <div style="margin-top: 20px; font-size: 12px; color: #666;">
      默认账号：<br />
      管理员：admin / admin123 <br />
      维修人员：repair / repair123
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()

const username = ref('admin')
const password = ref('admin123')
const loading = ref(false)
const error = ref('')

async function doLogin() {
  error.value = ''
  loading.value = true
  try {
    await userStore.doLogin({ username: username.value, password: password.value })
    router.push('/')
  } catch (e) {
    error.value = e?.response?.data?.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

