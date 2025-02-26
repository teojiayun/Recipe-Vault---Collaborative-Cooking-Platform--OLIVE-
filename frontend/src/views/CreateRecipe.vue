<script setup lang="ts">
import RecipeForm, { type RecipeFormData } from '../components/RecipeForm.vue'
import { useRecipeStore } from '../store/recipeStore'
import { useRouter } from 'vue-router'
import { apiService } from '../services/apiService'

const recipeStore = useRecipeStore()
const router = useRouter()

const handleCreate = async (formData: FormData) => {
  try {
    const response = await apiService.createRecipe(formData)
    console.log('Response: ', response)
    await recipeStore.loadRecipes()

    if (response.id) {
      router.push(`/recipes/${response.id}`);
    } else {
      console.error("No ID returned from API.");
    }
  } catch (error) {
    console.error("Error creating recipe:", error)
    alert("Failed to create recipe. Please try again.")
  }
}
</script>

<template>
  <div>
    <h2>Create a New Recipe</h2>
    <RecipeForm @submit="handleCreate" submitText="Create Recipe" />
  </div>
</template>