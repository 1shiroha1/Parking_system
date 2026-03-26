import { http, apiResponseData } from './http'

export async function listAdminUsers() {
  const res = await http.get('/api/admin/users')
  return apiResponseData(res).data
}

export async function setUserEnabled(userId, enabled) {
  const res = await http.patch(`/api/admin/users/${userId}/enabled`, { enabled })
  return apiResponseData(res).data
}

