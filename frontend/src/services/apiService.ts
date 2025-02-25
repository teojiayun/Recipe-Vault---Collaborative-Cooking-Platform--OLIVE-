import axios from 'axios'
import type { Recipe } from '../store/recipeStore'

const API_BASE_URL = 'http://localhost:8080'

export const apiService = {
  async fetchRecipes(): Promise<Recipe[]> {
    try {
      const response = await axios.get(`${API_BASE_URL}/recipes`)
      return response.data.map((recipe: any) => ({
        id: recipe.id,
        title: recipe.title,
        difficulty: recipe.difficulty,
        instructions: recipe.instructions,
        imageUrl: recipe.imageUrl ? `${API_BASE_URL}/uploads/${recipe.imageUrl.split('/').pop()}` : '', // Default image
        creator: recipe.creatorName, // Adjust key mapping
        createdDate: recipe.createdDate,
        ingredients: recipe.ingredients.map((ing: any) => ing.ingredientName)
      }))
    } catch (error) {
      console.error('Error fetching recipes:', error)
      return []
    }
  },

  async fetchImage(imagePath: string): Promise<string> {
    try {
      const response = await axios.get(`${API_BASE_URL}/uploads/${imagePath.split('/').pop()}`, {
        responseType: 'blob' // Get the image as a binary blob
      })
      return URL.createObjectURL(response.data) // Convert to a usable URL
    } catch (error) {
      console.error('Error fetching image:', error)
      return 'https://via.placeholder.com/150' // Fallback image
    }
  },

  async createRecipe(formData: FormData) {
    try {
      const response = await axios.post(`${API_BASE_URL}/recipes`, formData, {
        headers: { "Content-Type": "multipart/form-data" }
      })
      return response.data
    } catch (error) {
      console.error("Error creating recipe:", error)
      throw error
    }
  },

  async updateRecipe(recipeId: number, updatedData: FormData) {
    try {
      const response = await axios.put(`${API_BASE_URL}/recipes/${recipeId}`, updatedData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      return response.data
    } catch (error) {
      console.error('Error updating recipe:', error)
      throw error
    }
  }
}
