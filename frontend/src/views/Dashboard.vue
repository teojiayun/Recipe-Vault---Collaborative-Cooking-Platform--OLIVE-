<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRecipeStore } from '../store/recipeStore'
import { storeToRefs } from 'pinia'
import RecipeCard from '../components/RecipeCard.vue'
import { Search } from '@element-plus/icons-vue'

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

// Filter Logic
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

// Reset pagination when filters change
watch([searchQuery, selectedDifficulty, selectedIngredientCount], () => {
  currentPage.value = 1
})

// Reset Filters
const resetFilters = () => {
  searchQuery.value = ''
  selectedDifficulty.value = ''
  selectedIngredientCount.value = ''
}

// Pagination
const currentPage = ref(1)
const pageSize = ref(6)

const paginatedRecipes = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredRecipes.value.slice(start, start + pageSize.value)
})
</script>

<template>
  <div class="dashboard">
    <h2>All Recipes</h2>

    <el-card class="filter-container">
      <el-row :gutter="20" align="middle">
        <!-- Search Bar -->
        <el-col :span="18" :xs="24">
          <el-input v-model="searchQuery" placeholder="Search by title" clearable>
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>

        <!-- Filter Dropdown -->
        <el-col :span="6" :xs="24">
          <el-dropdown trigger="click" class="filter-dropdown">
            <el-button type="primary">
              Filter Options
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu class="large-dropdown">
                <!-- Difficulty Filter -->
                <div class="filter-item">
                  <span class="filter-label">Difficulty:</span>
                  <el-radio-group v-model="selectedDifficulty" size="large" class="radio-inline">
                    <el-radio-button label="EASY">Easy</el-radio-button>
                    <el-radio-button label="MEDIUM">Medium</el-radio-button>
                    <el-radio-button label="HARD">Hard</el-radio-button>
                  </el-radio-group>
                </div>

                <!-- Ingredient Count Filter (Prevent Close on Click) -->
                <div class="filter-item" @click.stop>
                  <span class="filter-label">Ingredient Count:</span>
                  <el-select v-model="selectedIngredientCount" placeholder="Select Ingredient Count" clearable class="filter-select">
                    <el-option v-for="count in [1, 2, 3, 4, 5, 6]" :key="count" :label="`${count} Ingredients`" :value="count" />
                  </el-select>
                </div>

                <!-- Reset Filters Button -->
                <div class="filter-item reset-item" @click.stop>
                  <el-button type="danger" plain @click="resetFilters">Reset Filters</el-button>
                </div>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-col>
      </el-row>
    </el-card>

    <!-- Recipe Grid -->
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

.filter-container {
  max-width: 600px; /* Set fixed width */
  width: 100%;
  margin: 0 auto;
  padding: 20px;
  border-radius: 8px;
  background-color: #f9f9f9;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

/* Ensure dropdown menu doesn't shrink */
.large-dropdown {
  width: 320px !important;
  padding: 15px;
}

.filter-dropdown {
  width: 100%;
  text-align: center;
}

/* Proper alignment for filters */
.filter-item {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 5px;
  padding: 8px 12px;
}

/* Ensure labels are visible */
.filter-label {
  color: black;
  font-weight: bold;
  font-size: 14px;
  margin-bottom: 5px;
}

/* Align radio buttons in a row */
.radio-inline {
  display: flex;
  gap: 10px;
  justify-content: space-between;
  width: 100%;
}

/* Make select input full width */
.filter-select {
  width: 100%;
}

/* Center reset button */
.reset-item {
  display: flex;
  justify-content: center;
  padding-top: 10px;
}

/* Recipe Grid */
.recipe-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
  justify-content: center;
  margin-top: 20px;
  width: 100%;
  max-width: 1200px;
}

/* Pagination */
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
