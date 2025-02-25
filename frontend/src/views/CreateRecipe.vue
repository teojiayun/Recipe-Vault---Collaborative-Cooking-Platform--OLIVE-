<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRecipeStore } from '../store/recipeStore'
import { useRouter } from 'vue-router'
import { apiService } from '../services/apiService'
import { Delete, Plus, ZoomIn } from '@element-plus/icons-vue'
import type { FormInstance, FormRules, UploadFile } from 'element-plus'

const recipeStore = useRecipeStore()
const router = useRouter()

const formRef = ref<FormInstance | null>(null)

// Recipe Form Data
const recipeForm = reactive({
  title: '',
  difficulty: 'EASY',
  ingredients: [''],
  instructions: '',
  creator: '',
  imageFile: null as File | null
})

// Validation Rules
const rules: FormRules = {
  title: [{ required: true, message: 'Title is required', trigger: 'blur' }],
  difficulty: [{ required: true, message: 'Difficulty is required', trigger: 'change' }],
  creator: [{ required: true, message: 'Creator name is required', trigger: 'blur' }],
  instructions: [{ required: true, message: 'Instructions are required', trigger: 'blur' }],
  ingredients: [
    { required: true, message: 'At least one ingredient is required', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value.some((ing: string) => !ing.trim())) {
          callback(new Error('Ingredient cannot be empty'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const dialogVisible = ref(false)
const loading = ref(false)

// // Add/Remove Ingredients
// const addIngredient = () => ingredients.value.push('')
// const removeIngredient = (index: number) => ingredients.value.splice(index, 1)

// Image Upload Handling
const fileList = ref<UploadFile[]>([])
const previewUrl = ref('')

// Handle File Upload (Limit to 1)
const handleFileChange = (file: UploadFile) => {
  fileList.value = [file] // Only allow one file
  recipeForm.imageFile = file.raw as File

  const reader = new FileReader()
  reader.onload = (e) => {
    previewUrl.value = e.target?.result as string
  }
  reader.readAsDataURL(file.raw as File)
}

// Handle Remove Image
const handleRemove = () => {
  fileList.value = []
  recipeForm.imageFile = null
  previewUrl.value = ''
  dialogVisible.value = false
}

// Open Image Preview Dialog
const handlePictureCardPreview = () => {
  if (previewUrl.value) {
    dialogVisible.value = true
  }
}

// Submit Recipe
const createRecipe = async () => {
  formRef.value?.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      const formData = new FormData()
      formData.append("title", recipeForm.title)
      formData.append("difficulty", recipeForm.difficulty)
      formData.append("instructions", recipeForm.instructions)
      formData.append("creatorName", recipeForm.creator)
      formData.append("ingredients", JSON.stringify(recipeForm.ingredients))

      if (recipeForm.imageFile) {
        formData.append("image", recipeForm.imageFile)
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
  })
}
</script>

<template>
  <div class="create-recipe">
    <h2>Create a New Recipe</h2>

    <el-form ref="formRef" :model="recipeForm" :rules="rules" class="recipe-form" label-width="120px">
      <!-- Title -->
      <el-form-item label="Title" prop="title">
        <el-input v-model="recipeForm.title" />
      </el-form-item>

      <!-- Difficulty -->
      <el-form-item label="Difficulty">
        <el-select v-model="recipeForm.difficulty">
          <el-option label="Easy" value="EASY" />
          <el-option label="Medium" value="MEDIUM" />
          <el-option label="Hard" value="HARD" />
        </el-select>
      </el-form-item>

      <!-- Creator Name -->
      <el-form-item label="Creator" prop="creator">
        <el-input v-model="recipeForm.creator" />
      </el-form-item>

      <!-- Instructions -->
      <el-form-item label="Instructions" prop="instructions">
        <el-input v-model="recipeForm.instructions" type="textarea" rows="4" />
      </el-form-item>

      <!-- Ingredients List -->
      <el-form-item label="Ingredients" prop="ingredients">
        <div class="ingredient-list">
          <div v-for="(ingredient, index) in recipeForm.ingredients" :key="index" class="ingredient-row">
            <el-input v-model="recipeForm.ingredients[index]" />
            <el-button type="danger" :icon="Delete" @click="recipeForm.ingredients.splice(index, 1)" v-if="recipeForm.ingredients.length > 1" />
          </div>
        </div>
        <el-button type="primary" :icon="Plus" @click="recipeForm.ingredients.push('')">Add Ingredient</el-button>
      </el-form-item>

      <!-- Image Upload with Preview -->
      <el-form-item label="Recipe Image">
        <el-upload
          action="#"
          list-type="picture-card"
          :auto-upload="false"
          :limit="1"
          :on-change="handleFileChange"
          :on-remove="handleRemove"
          :file-list="fileList"
          @preview="handlePictureCardPreview"
        >
          <el-icon><Plus /></el-icon>
        </el-upload>
      </el-form-item>

      <!-- Image Preview Dialog -->
      <el-dialog v-model="dialogVisible" title="Image Preview">
        <img class="preview-img" :src="previewUrl" alt="Preview Image" />
        <div class="dialog-buttons">
          <el-button type="danger" @click="handleRemove">
            <el-icon><Delete /></el-icon> Delete
          </el-button>
          <el-button type="primary" @click="dialogVisible = false">
            Close
          </el-button>
        </div>
      </el-dialog>

      <!-- Submit Button -->
      <el-form-item>
        <el-button type="primary" native-type="submit" :loading="loading" class="submit-button" @click="createRecipe">
          {{ loading ? "Creating..." : "Create Recipe" }}
        </el-button>
      </el-form-item>
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

.preview-img {
  width: 100%;
  max-height: 300px;
  object-fit: contain;
}

.dialog-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.el-button {
  margin-top: 0px;
  margin-left: 10px;
}
</style>
