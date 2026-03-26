import { http, apiResponseData } from './http'

export async function createReservation(payload) {
  const res = await http.post('/api/reservations', payload)
  return apiResponseData(res).data
}

export async function listMyReservations() {
  const res = await http.get('/api/reservations/my')
  return apiResponseData(res).data
}

export async function cancelReservation(reservationId, cancelReason) {
  const res = await http.post(`/api/reservations/${reservationId}/cancel`, { cancelReason })
  return apiResponseData(res).data
}

