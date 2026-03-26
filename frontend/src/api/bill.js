import { http, apiResponseData } from './http'

export async function listMyBills() {
  const res = await http.get('/api/bills/my')
  return apiResponseData(res).data
}

export async function payBillMock(billId) {
  const res = await http.post(`/api/bills/${billId}/pay`)
  return apiResponseData(res).data
}

