<script setup lang="ts">
import { reactive, ref, onMounted, watch } from 'vue'
import { Delete, Plus } from '@element-plus/icons-vue'
import type { FormInstance, FormRules, UploadFile } from 'element-plus'

// Define the shape of the form data
export interface RecipeFormData {
  title: string
  difficulty: string
  ingredients: string[]
  instructions: string
  creator: string
  imageFile: File | null
}

// Define component props
const props = defineProps<{
    initialData?: RecipeFormData
    initialImageUrl?: string
    submitText?: string
}>()

// Define the submit event
const emit = defineEmits<{
  (e: 'submit', formData: FormData): void
}>()

const formRef = ref<FormInstance | null>(null)

// Initialize form data (use initialData if provided, otherwise default values)
const recipeForm = reactive<RecipeFormData>({
  title: props.initialData?.title ?? '',
  difficulty: props.initialData?.difficulty ?? 'EASY',
  ingredients: props.initialData?.ingredients ? [...props.initialData.ingredients] : [''],
  instructions: props.initialData?.instructions ?? '',
  creator: props.initialData?.creator ?? '',
  imageFile: null
})

// Validation rules for the form
const rules: FormRules = {
  title: [{ required: true, message: 'Title is required', trigger: 'blur' }],
  difficulty: [{ required: true, message: 'Difficulty is required', trigger: 'change' }],
  creator: [{ required: true, message: 'Creator name is required', trigger: 'blur' }],
  instructions: [{ required: true, message: 'Instructions are required', trigger: 'blur' }],
  ingredients: [
    { required: true, message: 'At least one ingredient is required', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
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

// Image upload handling
const fileList = ref<UploadFile[]>([])
const previewUrl = ref('')

// If an initial image URL is provided (update mode), use it for the preview
onMounted(() => {
  if (props.initialImageUrl) {
    previewUrl.value = props.initialImageUrl;
    fileList.value = [
      {
        name: 'Existing Image',
        url: props.initialImageUrl,
        status: 'success', // Use 'success' instead of 'finished'
        uid: 'existing-image'
      } as unknown as UploadFile
    ];
  }
});

// Handle image file change (limit to one file)
const handleFileChange = (file: UploadFile) => {
  fileList.value = [file]
  recipeForm.imageFile = file.raw as File

  const reader = new FileReader()
  reader.onload = (e) => {
    previewUrl.value = e.target?.result as string
  }
  reader.readAsDataURL(file.raw as File)
}

watch(
  () => props.initialImageUrl,
  (newVal) => {
    if (newVal) {
      previewUrl.value = newVal;
      fileList.value = [
        {
          name: 'Existing Image',
          url: newVal,
          status: 'success', // Use 'success' here as well
          uid: 'existing-image'
        } as unknown as UploadFile
      ];
    }
  }
);

// Remove image from the upload list and clear preview
const handleRemove = () => {
  fileList.value = []
  recipeForm.imageFile = null
  previewUrl.value = ''
  dialogVisible.value = false
}

// Open preview dialog for the uploaded image
const handlePictureCardPreview = () => {
  if (previewUrl.value) {
    dialogVisible.value = true
  }
}

// Ingredient management
const addIngredient = () => {
  recipeForm.ingredients.push('')
}
const removeIngredient = (index: number) => {
  if (recipeForm.ingredients.length > 1) {
    recipeForm.ingredients.splice(index, 1)
  }
}

// When the form is submitted, validate and build the FormData then emit the "submit" event
const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (!valid) return

    loading.value = true
    const formData = new FormData()
    formData.append("title", recipeForm.title)
    formData.append("difficulty", recipeForm.difficulty)
    formData.append("instructions", recipeForm.instructions)
    formData.append("creatorName", recipeForm.creator)
    formData.append("ingredients", JSON.stringify(recipeForm.ingredients))
    if (recipeForm.imageFile) {
      formData.append("image", recipeForm.imageFile)
    }
    emit("submit", formData)
    loading.value = false
  })
}
</script>

<template>
  <div class="recipe-form-container">
    <el-form ref="formRef" :model="recipeForm" :rules="rules" class="recipe-form" label-width="120px">
      <!-- Title -->
      <el-form-item label="Title" prop="title">
        <el-input v-model="recipeForm.title" />
      </el-form-item>

      <!-- Difficulty -->
      <el-form-item label="Difficulty" prop="difficulty">
        <el-select v-model="recipeForm.difficulty">
          <el-option label="Easy" value="EASY" />
          <el-option label="Medium" value="MEDIUM" />
          <el-option label="Hard" value="HARD" />
        </el-select>
      </el-form-item>

      <!-- Creator -->
      <el-form-item label="Creator" prop="creator">
        <el-input v-model="recipeForm.creator" />
      </el-form-item>

      <!-- Instructions -->
      <el-form-item label="Instructions" prop="instructions">
        <el-input v-model="recipeForm.instructions" type="textarea" rows="4" />
      </el-form-item>

      <!-- Ingredients -->
      <el-form-item label="Ingredients" prop="ingredients">
        <div class="ingredient-list">
          <div v-for="(ingredient, index) in recipeForm.ingredients" :key="index" class="ingredient-row">
            <el-input v-model="recipeForm.ingredients[index]" />
            <el-button
              type="danger"
              :icon="Delete"
              @click="removeIngredient(index)"
              v-if="recipeForm.ingredients.length > 1"
            />
          </div>
        </div>
        <el-button type="primary" :icon="Plus" @click="addIngredient">Add Ingredient</el-button>
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
      </el-dialog>

      <!-- Submit Button -->
      <el-form-item>
        <el-button type="primary" native-type="submit" :loading="loading" class="submit-button" @click="handleSubmit">
          {{ submitText || "Submit Recipe" }}
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped>
.recipe-form-container {
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
.submit-button {
  width: 100%;
  margin-top: 10px;
}

.el-button {
  margin-top: 0px;
  margin-left: 10px;
}
</style>
