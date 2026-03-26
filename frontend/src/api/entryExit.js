import { http, apiResponseData } from './http'

export async function adminCreateLog(payload) {
  const res = await http.post('/api/admin/entry-exit/logs', payload)
  return apiResponseData(res).data
}

