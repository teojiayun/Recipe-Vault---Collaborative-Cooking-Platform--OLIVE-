<script setup lang="ts">
import { ref } from 'vue'
import { useRecipeStore } from '../store/recipeStore'
import { useRouter } from 'vue-router'
import { apiService } from '../services/apiService'

const recipeStore = useRecipeStore()
const router = useRouter()

const title = ref('')
const difficulty = ref('EASY')
const ingredients = ref([''])
const instructions = ref('')
const creator = ref('')
const imageFile = ref<File | null>(null)
const imagePreview = ref<string | null>(null)
const loading = ref(false)

const addIngredient = () => ingredients.value.push('')
const removeIngredient = (index: number) => ingredients.value.splice(index, 1)

const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files[0]) {
    imageFile.value = target.files[0]

    const reader = new FileReader()
    reader.onload = (e) => {
      imagePreview.value = e.target?.result as string
    }
    reader.readAsDataURL(imageFile.value)
  }
}

const createRecipe = async () => {
  if (!title.value || !instructions.value || !creator.value || ingredients.value.some(ing => !ing.trim())) {
    alert("Please fill in all fields.");
    return;
  }

  loading.value = true;
  try {
    const formData = new FormData()
    formData.append("title", title.value)
    formData.append("difficulty", difficulty.value)
    formData.append("instructions", instructions.value)
    formData.append("creatorName", creator.value)
    formData.append("ingredients", JSON.stringify(ingredients.value))

    if (imageFile.value) {
      formData.append("image", imageFile.value)
    }

    await apiService.createRecipe(formData)
    await recipeStore.loadRecipes()
    router.push('/')
  } catch (error) {
    console.error("Error creating recipe:", error)
    alert("Failed to create recipe. Please try again.")
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="create-recipe">
    <h2>Create a New Recipe</h2>
    
    <el-form @submit.prevent="createRecipe" class="recipe-form">
      <el-form-item label="Title">
        <el-input v-model="title" required />
      </el-form-item>

      <el-form-item label="Difficulty">
        <el-select v-model="difficulty">
          <el-option label="Easy" value="EASY" />
          <el-option label="Medium" value="MEDIUM" />
          <el-option label="Hard" value="HARD" />
        </el-select>
      </el-form-item>

      <el-form-item label="Ingredients">
        <div class="ingredient-list">
          <div v-for="(ingredient, index) in ingredients" :key="index" class="ingredient-row">
            <el-input v-model="ingredients[index]" required />
            <el-button type="danger" @click="removeIngredient(index)" v-if="ingredients.length > 1">
              ❌
            </el-button>
          </div>
        </div>
        <el-button type="primary" @click="addIngredient">➕ Add Ingredient</el-button>
      </el-form-item>

      <el-form-item label="Instructions">
        <el-input v-model="instructions" type="textarea" required />
      </el-form-item>

      <el-form-item label="Creator">
        <el-input v-model="creator" required />
      </el-form-item>

      <el-form-item label="Recipe Image">
        <input type="file" @change="handleFileChange" accept="image/*" />
        <div v-if="imagePreview" class="image-preview-container">
          <img :src="imagePreview" alt="Image Preview" class="image-preview" />
        </div>
      </el-form-item>

      <el-button type="primary" native-type="submit" :loading="loading">
        {{ loading ? "Creating..." : "Create Recipe" }}
      </el-button>
    </el-form>
  </div>
</template>


<style scoped>
.create-recipe {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  text-align: center;
}

.recipe-form {
  background: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}

.ingredient-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.ingredient-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.image-preview-container {
  margin-top: 10px;
  display: flex;
  justify-content: center;
}

.image-preview {
  max-width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 5px;
}

.el-button {
  margin-top: 0px;
  margin-left: 10px;
}
</style>
