import { http, apiResponseData } from './http'

export async function createRepairWorker(payload) {
  const res = await http.post('/api/admin/repair-workers', payload)
  return apiResponseData(res).data
}

export async function createRepairTicket(payload) {
  const res = await http.post('/api/admin/repair-tickets', payload)
  return apiResponseData(res).data
}

export async function listAdminRepairTickets() {
  const res = await http.get('/api/admin/repair-tickets')
  return apiResponseData(res).data
}

export async function listMyRepairTickets() {
  const res = await http.get('/api/repair-tickets/my')
  return apiResponseData(res).data
}

export async function updateRepairTicket(ticketId, payload) {
  const res = await http.patch(`/api/repair-tickets/${ticketId}`, payload)
  return apiResponseData(res).data
}

