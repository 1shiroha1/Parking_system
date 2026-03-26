import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'

import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Layout from '../views/Layout.vue'

import Vehicles from '../views/Vehicles.vue'
import Reservations from '../views/Reservations.vue'
import Bills from '../views/Bills.vue'

import AdminParking from '../views/AdminParking.vue'
import AdminEntryExit from '../views/AdminEntryExit.vue'
import AdminRepairTickets from '../views/AdminRepairTickets.vue'
import AdminUsers from '../views/AdminUsers.vue'
import AdminStats from '../views/AdminStats.vue'

import RepairTickets from '../views/RepairTickets.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'login', component: Login },
    { path: '/register', name: 'register', component: Register },
    {
      path: '/',
      component: Layout,
      redirect: '/vehicles',
      children: [
        { path: '/vehicles', name: 'vehicles', component: Vehicles, meta: { roles: ['ADMIN', 'USER'], requiresAuth: true } },
        { path: '/reservations', name: 'reservations', component: Reservations, meta: { roles: ['ADMIN', 'USER'], requiresAuth: true } },
        { path: '/bills', name: 'bills', component: Bills, meta: { roles: ['ADMIN', 'USER'], requiresAuth: true } },

        { path: '/admin/parking', name: 'adminParking', component: AdminParking, meta: { roles: ['ADMIN'], requiresAuth: true } },
        { path: '/admin/entry-exit', name: 'adminEntryExit', component: AdminEntryExit, meta: { roles: ['ADMIN'], requiresAuth: true } },
        { path: '/admin/repair-tickets', name: 'adminRepairTickets', component: AdminRepairTickets, meta: { roles: ['ADMIN'], requiresAuth: true } },
        { path: '/admin/users', name: 'adminUsers', component: AdminUsers, meta: { roles: ['ADMIN'], requiresAuth: true } },
        { path: '/admin/stats', name: 'adminStats', component: AdminStats, meta: { roles: ['ADMIN'], requiresAuth: true } },

        { path: '/repair/tickets', name: 'repairTickets', component: RepairTickets, meta: { roles: ['REPAIR'], requiresAuth: true } }
      ]
    }
  ]
})

router.beforeEach(async (to) => {
  const store = useUserStore()
  const requiresAuth = to.meta && to.meta.requiresAuth
  if (!requiresAuth) return true

  if (!store.isLoggedIn) {
    return { name: 'login' }
  }

  // If role is empty (e.g. token restored but not role), refresh me.
  if (!store.role) {
    try {
      await store.refreshMe()
    } catch (e) {
      store.logout()
      return { name: 'login' }
    }
  }

  const roles = to.meta.roles
  if (roles && roles.length > 0 && !roles.includes(store.role)) {
    return '/'
  }
  return true
})

export default router

