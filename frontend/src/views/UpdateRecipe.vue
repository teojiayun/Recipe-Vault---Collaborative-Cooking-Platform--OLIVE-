<script setup lang="ts">
import RecipeForm, { type RecipeFormData } from '../components/RecipeForm.vue'
import { useRecipeStore } from '../store/recipeStore'
import { useRoute, useRouter } from 'vue-router'
import { apiService } from '../services/apiService'

const recipeStore = useRecipeStore()
const route = useRoute()
const router = useRouter()

const recipeId = Number(route.params.id)
const existingRecipe = recipeStore.getRecipeById(recipeId)

if (!existingRecipe) {
  router.push('/')
}

const initialData: RecipeFormData = {
  title: existingRecipe?.title || '',
  difficulty: existingRecipe?.difficulty || 'EASY',
  ingredients: existingRecipe?.ingredients || [''],
  instructions: existingRecipe?.instructions || '',
  creator: existingRecipe?.creator || '',
  imageFile: null
}

const handleUpdate = async (formData: FormData) => {
  try {
    await apiService.updateRecipe(recipeId, formData)
    await recipeStore.loadRecipes()
    router.push(`/recipe/${recipeId}`)
  } catch (error) {
    console.error("Error updating recipe:", error)
    alert("Failed to update recipe. Please try again.")
  }
}
</script>

<template>
  <div>
    <h2>Update Recipe</h2>
    <RecipeForm
      :initialData="initialData"
      :initialImageUrl="existingRecipe?.imageUrl"
      @submit="handleUpdate"
      submitText="Update Recipe"
    />
  </div>
</template>
