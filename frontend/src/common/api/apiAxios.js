// common/api/api.js
import axios from 'axios'
import { useSettingsStore } from '@/common/stores/settingsStore'
import { useAuthStore } from '@/common/stores/authStore'
import autenticacionService from '../services/autenticacion.service'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1',
  withCredentials: true // 🔥 NECESARIO para cookies HttpOnly
})

// ---------------- REQUEST ----------------
// 👉 Access token automático
api.interceptors.request.use((config) => {
  const auth = useAuthStore()
  if (auth.acceso_token) {
    config.headers.Authorization = `Bearer ${auth.acceso_token}`
  }
  return config
})

// 👉 Refresh automático si expira
let refreshing = false
let queue = []

// ---------------- RESPONSE ----------------
api.interceptors.response.use(
  res => res,
  async error => {
    const auth = useAuthStore()
    const original = error.config

    if (error.response?.status === 401 && !original._retry) {
      if (refreshing) {
        return new Promise(resolve => {
          queue.push(token => {
            original.headers.Authorization = `Bearer ${token}`
            resolve(api(original))
          })
        })
      }

      original._retry = true
      refreshing = true

      try {
        const { data } = await autenticacionService.refresh();

        const acceso_token = data?.contenido?.acceso_token;
        auth.acceso_token = acceso_token;
        queue.forEach(cb => cb(acceso_token))
        queue = []

        return api(original)
      } catch (e) {
        auth.logout()
        window.location.href = '/acceder'
      } finally {
        refreshing = false
      }
    }

    return Promise.reject(error)
  }
)

export default api
