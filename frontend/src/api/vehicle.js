import { http, apiResponseData } from './http'

export async function createVehicle(payload) {
  const res = await http.post('/api/vehicles', payload)
  return apiResponseData(res).data
}

export async function listVehicles() {
  const res = await http.get('/api/vehicles')
  return apiResponseData(res).data
}

