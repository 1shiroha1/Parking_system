import { http, apiResponseData } from './http'

export async function getAdminStats(days) {
  const res = await http.get('/api/admin/stats', { params: { days } })
  return apiResponseData(res).data
}

