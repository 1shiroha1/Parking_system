import { http, apiResponseData } from './http'

export async function login(username, password) {
  const res = await http.post('/api/auth/login', { username, password })
  const body = apiResponseData(res)
  return body.data
}

export async function register(username, password, displayName) {
  const res = await http.post('/api/auth/register', { username, password, displayName })
  const body = apiResponseData(res)
  return body.data
}

export async function getMe() {
  const res = await http.get('/api/auth/me')
  const body = apiResponseData(res)
  return body.data
}

