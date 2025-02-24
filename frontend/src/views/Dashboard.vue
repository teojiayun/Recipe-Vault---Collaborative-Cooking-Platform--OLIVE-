<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRecipeStore } from '../store/recipeStore'
import { storeToRefs } from 'pinia'
import RecipeCard from '../components/RecipeCard.vue'

// Load Recipes
const recipeStore = useRecipeStore()
const { recipes } = storeToRefs(recipeStore)

onMounted(async () => {
  await recipeStore.loadRecipes()
})

// Search & Filter
const searchQuery = ref('')
const selectedDifficulty = ref('')
const selectedIngredientCount = ref('')

const filteredRecipes = computed(() => {
  return recipes.value.filter(recipe => {
    const matchesSearch = recipe.title.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesDifficulty = selectedDifficulty.value ? recipe.difficulty === selectedDifficulty.value : true
    const matchesIngredientCount = selectedIngredientCount.value
      ? recipe.ingredients.length === Number(selectedIngredientCount.value)
      : true
    return matchesSearch && matchesDifficulty && matchesIngredientCount
  })
})

// Pagination
const currentPage = ref(1);
const pageSize = ref(3);

const paginatedRecipes = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredRecipes.value.slice(start, start + pageSize.value)
})


</script>

<template>
  <div class="dashboard">
    <h2>All Recipes</h2>

    <!-- Search & Filter Controls -->
    <div class="filters">
      <el-input v-model="searchQuery" placeholder="Search by title..." clearable />
      
      <el-select v-model="selectedDifficulty" placeholder="Filter by difficulty" clearable>
        <el-option label="Easy" value="EASY" />
        <el-option label="Medium" value="MEDIUM" />
        <el-option label="Hard" value="HARD" />
      </el-select>

      <el-select v-model="selectedIngredientCount" placeholder="Filter by ingredient count" clearable>
        <el-option v-for="count in [1, 2, 3, 4, 5, 6]" :key="count" :label="`${count} Ingredients`" :value="count" />
      </el-select>
    </div>

    <!-- Responsive Grid for Recipe Cards -->
    <div class="recipe-grid">
      <RecipeCard
        v-for="recipe in paginatedRecipes"
        :key="recipe.id"
        :recipe="recipe"
      />
    </div>

    <!-- Pagination -->
    <el-pagination
      v-if="filteredRecipes.length > pageSize"
      v-model:current-page="currentPage"
      :page-size="pageSize"
      layout="prev, pager, next"
      :total="filteredRecipes.length"
      class="pagination"
    />
  </div>
</template>

<style scoped>
.dashboard {
  padding: 20px;
  max-width: 1200px;
  margin: auto;
  text-align: center;
}

.filters {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.recipe-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
  justify-content: center;
  margin-top: 20px;
  width: 100%;
  max-width: 1200px;
}

@media (max-width: 768px) {
  .recipe-grid {
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); /* ✅ 1-2 per row */
    gap: 15px;
  }
}

@media (max-width: 480px) {
  .recipe-grid {
    grid-template-columns: 1fr; /* ✅ Only 1 per row on small screens */
    gap: 10px;
  }
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
