import axios from "axios"
import { defineStore } from "pinia"

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
          const response = await axios.post('http://localhost:8080/auth/login', { username, password })
          // The API returns { userId, username, fullName, token }
          this.user = response.data
          // Optionally, persist token and user details in localStorage
          localStorage.setItem('jwtToken', response.data.token)
          localStorage.setItem('user', JSON.stringify(response.data))
        } catch (error) {
          console.error("Login error:", error)
          throw error
        }
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