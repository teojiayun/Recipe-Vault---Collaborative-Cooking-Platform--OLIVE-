import { defineStore } from 'pinia'
import { apiService } from '../services/apiService'

export interface Recipe {
  id: number
  title: string
  difficulty: 'EASY' | 'MEDIUM' | 'HARD'
  ingredients: string[]
  instructions: string
  imageUrl: string
  createdDate: string
  userId: number
  fullName: string
}

export const useRecipeStore = defineStore('recipeStore', {
  state: () => ({
    recipes: [] as Recipe[]
  }),
  actions: {
    async loadRecipes() {
      this.recipes = await apiService.fetchRecipes()
    },
    getRecipeById(id: number) {
      return this.recipes.find(recipe => recipe.id === id)
    }
  }
})
