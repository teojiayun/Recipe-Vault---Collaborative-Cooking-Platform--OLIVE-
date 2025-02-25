<script setup lang="ts">
import RecipeForm, { type RecipeFormData } from '../components/RecipeForm.vue'
import { useRecipeStore } from '../store/recipeStore'
import { useRouter } from 'vue-router'
import { apiService } from '../services/apiService'

const recipeStore = useRecipeStore()
const router = useRouter()

const handleCreate = async (formData: FormData) => {
  try {
    await apiService.createRecipe(formData)
    await recipeStore.loadRecipes()
    router.push('/')
  } catch (error) {
    console.error("Error creating recipe:", error)
    alert("Failed to create recipe. Please try again.")
  }
}
</script>

<template>
  <div>
    <h2>Create a New Recipe</h2>
    <RecipeForm ..submit="handleCreate" submitText="Create Recipe" />
  </div>
</template>