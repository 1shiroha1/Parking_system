<template>
  <div style="display: flex; min-height: 100vh;">
    <aside style="width: 220px; padding: 16px; border-right: 1px solid #eee;">
      <div style="font-weight: 700; margin-bottom: 16px;">智能停车管理系统</div>

      <div style="margin-bottom: 12px;">
        <div v-if="userStore.isLoggedIn">
          <div>用户名: {{ userStore.username }}</div>
          <div>角色: {{ roleZh[userStore.role] || userStore.role }}</div>
        </div>
      </div>

      <nav>
        <div v-if="userStore.isUser || userStore.isAdmin">
          <div style="margin: 8px 0;">
            <router-link to="/vehicles">车辆管理</router-link>
          </div>
          <div style="margin: 8px 0;">
            <router-link to="/reservations">车位预约</router-link>
          </div>
          <div style="margin: 8px 0;">
            <router-link to="/bills">停车账单</router-link>
          </div>
        </div>

        <div v-if="userStore.isAdmin" style="margin-top: 14px;">
          <div style="margin: 8px 0;">
            <router-link to="/admin/parking">车位管理（管理员）</router-link>
          </div>
          <div style="margin: 8px 0;">
            <router-link to="/admin/entry-exit">进出场管理（管理员）</router-link>
          </div>
          <div style="margin: 8px 0;">
            <router-link to="/admin/repair-tickets">维修工单（管理员）</router-link>
          </div>
          <div style="margin: 8px 0;">
            <router-link to="/admin/users">用户管理（管理员）</router-link>
          </div>
          <div style="margin: 8px 0;">
            <router-link to="/admin/stats">统计报表</router-link>
          </div>
        </div>

        <div v-if="userStore.isRepair" style="margin-top: 14px;">
          <div style="margin: 8px 0;">
            <router-link to="/repair/tickets">我的维修工单</router-link>
          </div>
        </div>
      </nav>

      <div style="margin-top: 18px;">
        <button v-if="userStore.isLoggedIn" @click="onLogout">退出登录</button>
      </div>
    </aside>

    <main style="flex: 1; padding: 16px;">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useUserStore } from '../store/user'
import { roleZh } from '../utils/dict'

const userStore = useUserStore()

function onLogout() {
  userStore.logout()
}
</script>

