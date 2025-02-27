import axios from 'axios'
import type { Recipe } from '../store/recipeStore'
import { useUserStore } from '../store/userStore'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL
// PRODUCTION: "http://52.65.20.122:8080"; "http://3.107.98.238:8080"
// DEVELOPMENT: "http://localhost:8080"

const apiClient = axios.create({
  baseURL: API_BASE_URL
})

// Attach JWT as Bearer token to every request
apiClient.interceptors.request.use((config) => {
  const userStore = useUserStore()
  if (userStore.user && userStore.user.token) {
    config.headers['Authorization'] = `Bearer ${userStore.user.token}`
  }
  return config
})

export const apiService = {

  async login(credentials: { username: string; password: string }) {
    const response = await apiClient.post('/auth/login', credentials)
    return response.data
  },

  async fetchRecipes(): Promise<Recipe[]> {
    try {
      const response = await apiClient.get("/recipes")
      return response.data.map((recipe: any) => ({
        id: recipe.id,
        title: recipe.title,
        difficulty: recipe.difficulty,
        instructions: recipe.instructions,
        imageUrl: recipe.imageUrl 
            ? `${API_BASE_URL}/uploads/${recipe.imageUrl.split('/').pop()}` 
            : `${API_BASE_URL}/uploads/placeholder.jpg`,
        createdDate: recipe.createdDate,
        ingredients: recipe.ingredients.map((ing: any) => ing.ingredientName),
        userId: recipe.userId,
        fullName: recipe.fullName
      }))
    } catch (error) {
      console.error('Error fetching recipes:', error)
      return []
    }
  },

  async fetchImage(imagePath: string): Promise<string> {
    try {
      const response = await apiClient.get(`/uploads/${imagePath.split('/').pop()}`, {
        responseType: 'blob' // Get the image as a binary blob
      })
      return URL.createObjectURL(response.data) // Convert to a usable URL
    } catch (error) {
      console.error('Error fetching image:', error)
      return `${API_BASE_URL}/uploads/placeholder.jpg`; // Fallback image
    }
  },

  async createRecipe(formData: FormData) {
    try {
      console.log([...formData.entries()]);
      const response = await apiClient.post(`/recipes`, formData, {
        headers: { "Content-Type": "multipart/form-data" }
      })
      console.log("API Response:", response); // Log entire response object
      console.log("API Response Data:", response.data); // Log actual response data
      return response.data
    } catch (error) {
      console.error("Error creating recipe:", error)
      throw error
    }
  },

  async updateRecipe(recipeId: number, updatedData: FormData) {
    try {
      const response = await apiClient.put(`/recipes/${recipeId}`, updatedData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      console.log(response)
      return response.data
    } catch (error) {
      console.error('Error updating recipe:', error)
      throw error
    }
  },

  async deleteRecipe(recipeId?: number) {
    try {
      const response = await apiClient.delete(`/recipes/${recipeId}`)
      return response.data
    } catch (error) {
      console.error('Error deleting recipe: ', error)
      throw error
    }
  }
}
