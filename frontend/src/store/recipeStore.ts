import { defineStore } from 'pinia'
import { apiService } from '../services/apiService'

export interface Recipe {
  id: number
  title: string
  difficulty: 'EASY' | 'MEDIUM' | 'HARD'
  ingredients: string[]
  instructions: string
  imageUrl: string
  creator: string
  createdDate: string
}

export const useRecipeStore = defineStore('recipeStore', {
  state: () => ({
    recipes: [] as Recipe[]
  }),
  actions: {
    async loadRecipes() {
      this.recipes = await apiService.fetchRecipes()
    },
    // async addRecipe(recipe: Recipe) {
    //   try {
    //     await apiService.createRecipe(recipe)
    //     await this.loadRecipes() // ✅ Refresh list
    //   } catch (error) {
    //     console.error("Error adding recipe:", error)
    //   }
    // },
    getRecipeById(id: number) {
      return this.recipes.find(recipe => recipe.id === id)
    }
  }
})
