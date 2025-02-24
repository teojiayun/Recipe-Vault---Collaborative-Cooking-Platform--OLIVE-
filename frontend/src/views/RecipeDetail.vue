<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useRecipeStore } from '../store/recipeStore'
import { ref } from 'vue'
import { apiService } from '../services/apiService'

const route = useRoute()
const router = useRouter()
const recipeStore = useRecipeStore()

// Fetch the recipe based on the ID from the URL
const recipe = computed(() => recipeStore.getRecipeById(Number(route.params.id)))

// Image handling
const imageSrc = ref('https://via.placeholder.com/300')

onMounted(async () => {
  if (!recipe.value) {
    router.push('/') // Redirect if recipe is not found
  } else if (recipe.value.imageUrl) {
    imageSrc.value = await apiService.fetchImage(recipe.value.imageUrl)
  }
})

const getDifficultyTagType = (difficulty: 'EASY' | 'MEDIUM' | 'HARD') => {
  switch (difficulty) {
    case 'EASY': return 'success'
    case 'MEDIUM': return 'warning'
    case 'HARD': return 'danger'
    default: return ''
  }
}
</script>

<template>
  <el-container v-if="recipe">
    <el-card class="detail-card">
      <img class="recipe-image" :src="imageSrc" alt="Recipe Image" />
      
      <h2>{{ recipe.title }}</h2>
      <el-tag :type="getDifficultyTagType(recipe.difficulty)">
        {{ recipe.difficulty }}
      </el-tag>

      <p class="creator"><strong>Chef:</strong> {{ recipe.creator }}</p>
      <p class="created-date"><strong>Created:</strong> {{ new Date(recipe.createdDate).toLocaleDateString() }}</p>

      <h3>Ingredients</h3>
      <ul>
        <li v-for="ingredient in recipe.ingredients" :key="ingredient">{{ ingredient }}</li>
      </ul>

      <h3>Instructions</h3>
      <p>{{ recipe.instructions }}</p>

      <el-button type="primary" @click="router.push('/')">Back to Dashboard</el-button>
    </el-card>
  </el-container>
</template>

<style scoped>
.detail-card {
  max-width: 600px;
  margin: auto;
  padding: 20px;
  text-align: center;
}

.recipe-image {
  width: 100%;
  height: 250px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 10px;
}

.creator,
.created-date {
  font-size: 16px;
  margin: 5px 0;
}

ul {
  list-style: none;
  padding: 0;
}

ul li {
  background: #f2f2f2;
  padding: 8px;
  margin: 5px 0;
  border-radius: 5px;
}
</style>
