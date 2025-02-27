import { defineStore } from "pinia"
import { apiService } from "../services/apiService"

export interface User {
    userId: number
    username: string
    fullName: string
    token: string
}

export const useUserStore = defineStore('userStore', {
    state: () => ({
        user: null as User | null
    }),
    actions: {
      async login(username: string, password: string) {
        try {
          const userData = await apiService.login({ username, password })
          this.user = userData
          // Persist token and user details in localStorage
          localStorage.setItem('jwtToken', userData.token)
          localStorage.setItem('user', JSON.stringify(userData))
        } catch (error) {
          console.error("Login error:", error)
          throw error
        }
      },
      isTokenExpired() {
        const token = this.user?.token
        if (!token) return true // No token = expired
      
        const payload = JSON.parse(atob(token.split('.')[1])) // Decode JWT
        return payload.exp * 1000 < Date.now() // Compare expiration
      },
      logout() {
        this.user = null
        localStorage.removeItem('jwtToken')
        localStorage.removeItem('user')
      },
      loadUserFromStorage() {
        const stored = localStorage.getItem('user')
        if (stored) {
          this.user = JSON.parse(stored)
        }
      }
    }
})