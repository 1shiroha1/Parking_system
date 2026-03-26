import axios from 'axios'
import { useUserStore } from '../store/user'

const baseURL = 'http://localhost:8080'

export const http = axios.create({
  baseURL
})

http.interceptors.request.use((config) => {
  const store = useUserStore()
  if (store.token) {
    config.headers.Authorization = `Bearer ${store.token}`
  }
  return config
})

export function apiResponseData(res) {
  // backend: {code,message,data,timestamp}
  return res.data
}

