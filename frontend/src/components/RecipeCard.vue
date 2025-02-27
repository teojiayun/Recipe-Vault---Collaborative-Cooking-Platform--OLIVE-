<script setup lang="ts">
import { onMounted, ref, type PropType } from 'vue'
import type { Recipe } from '../store/recipeStore'
import { apiService } from '../services/apiService'
import { useRouter } from 'vue-router'

const props = defineProps({
  recipe: {
    type: Object as PropType<Recipe>,
    required: true
  }
})

const router = useRouter()
const imageSrc = ref('')

onMounted(async () => {
  if (props.recipe.imageUrl) {
    imageSrc.value = await apiService.fetchImage(props.recipe.imageUrl)
  }
})

// Difficulty color mapping
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
  <el-card class="recipe-card" shadow="always" @click="router.push(`/recipe/${recipe.id}`)">
    <img class="recipe-image" :src="imageSrc" alt="Recipe Image" />

    <div class="recipe-info">
      <h3 class="recipe-title">{{ recipe.title }}</h3>
      <p class="recipe-creator">{{ recipe.fullName }}</p>
      <p><strong>Ingredients:</strong> {{ recipe.ingredients.length }}</p>
      <p><strong>Created:</strong> {{ new Date(recipe.createdDate).toLocaleDateString() }}</p>
    </div>
    
    <!-- Difficulty Tag -->
    <el-tag :type="getDifficultyTagType(recipe.difficulty)" class="difficulty-tag">
      {{ recipe.difficulty }}
    </el-tag>
  </el-card>
</template>

<style scoped>
.recipe-card {
  width: 300px;
  height: 450px; /* Keeps all cards uniform */
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 15px;
  transition: transform 0.2s ease-in-out;
}

.recipe-card:hover {
  transform: scale(1.05);
}

.recipe-info {
  margin-top: 10px;
}

.recipe-image {
  width: 260px;
  height: 180px;
  object-fit: cover;
  border-radius: 5px;
  margin-bottom: 0px;
}

.recipe-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 5px;
  margin-top: 10px;
}

.difficulty-tag {
  font-size: 17px; 
  padding: 10px 20px; 
  border-radius: 8px; 
  text-transform: uppercase; 
  letter-spacing: 1px; 
}

.recipe-creator {
  font-size: 18px;
  color: rgb(71, 70, 70);
  margin-top: 10px;
  margin-bottom: 10px;
}

</style>
