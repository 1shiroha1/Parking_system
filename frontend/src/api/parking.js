import { http, apiResponseData } from './http'

export async function listLots() {
  const res = await http.get('/api/parking/lots')
  return apiResponseData(res).data
}

export async function listSpaces(lotId) {
  const res = await http.get(`/api/parking/spaces/${lotId}`)
  return apiResponseData(res).data
}

export async function createLot(payload) {
  const res = await http.post('/api/parking/admin/lots', payload)
  return apiResponseData(res).data
}

export async function createSpace(payload) {
  const res = await http.post('/api/parking/admin/spaces', payload)
  return apiResponseData(res).data
}

export async function setSpaceRepairStatus(spaceId, status) {
  const res = await http.patch(`/api/parking/admin/spaces/${spaceId}/repair-status`, null, {
    params: { status }
  })
  return apiResponseData(res).data
}

