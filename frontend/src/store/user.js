import { defineStore } from 'pinia'
import { getMe, login, register } from '../api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userId: Number(localStorage.getItem('userId') || 0) || 0,
    username: localStorage.getItem('username') || '',
    roleAuthority: localStorage.getItem('roleAuthority') || '',
    role: localStorage.getItem('role') || ''
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.role === 'ADMIN',
    isRepair: (state) => state.role === 'REPAIR',
    isUser: (state) => state.role === 'USER'
  },
  actions: {
    normalizeRole(roleAuthority) {
      if (!roleAuthority) return ''
      if (roleAuthority.startsWith('ROLE_')) return roleAuthority.slice(5)
      return roleAuthority
    },
    async doLogin({ username, password }) {
      const data = await login(username, password)
      this.token = data.token
      this.userId = data.userId
      this.username = data.username
      this.roleAuthority = data.roleAuthority
      this.role = this.normalizeRole(data.roleAuthority)

      localStorage.setItem('token', this.token)
      localStorage.setItem('userId', String(this.userId))
      localStorage.setItem('username', this.username)
      localStorage.setItem('roleAuthority', this.roleAuthority)
      localStorage.setItem('role', this.role)
    },
    async doRegister({ username, password, displayName }) {
      await register(username, password, displayName)
    },
    logout() {
      this.token = ''
      this.userId = 0
      this.username = ''
      this.roleAuthority = ''
      this.role = ''

      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('username')
      localStorage.removeItem('roleAuthority')
      localStorage.removeItem('role')
    },
    async refreshMe() {
      if (!this.token) return
      const me = await getMe()
      // backend me: {userId, username, roleAuthority}
      this.userId = me.userId
      this.username = me.username
      this.roleAuthority = me.roleAuthority
      this.role = this.normalizeRole(me.roleAuthority)
      localStorage.setItem('userId', String(this.userId))
      localStorage.setItem('username', this.username)
      localStorage.setItem('roleAuthority', this.roleAuthority)
      localStorage.setItem('role', this.role)
    }
  }
})

