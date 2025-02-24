<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRecipeStore } from '../store/recipeStore'
import { storeToRefs } from 'pinia'
import RecipeCard from '../components/RecipeCard.vue'

const recipeStore = useRecipeStore()
const { recipes } = storeToRefs(recipeStore)

const currentPage = ref(1);
const pageSize = ref(4);

onMounted(async () => {
  await recipeStore.loadRecipes()
})

const paginatedRecipes = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return recipes.value.slice(start, start + pageSize.value)
})
</script>

<template>
  <div class="dashboard">
    <h2>All Recipes</h2>

    <!-- Responsive Grid for Recipe Cards -->
    <!-- <div class="recipe-grid">
      <RecipeCard
        v-for="recipe in recipes"
        :key="recipe.id"
        :recipe="recipe"
      />
    </div> -->

    <!-- <el-table :data="recipes" border style="width: 100%" v-if="recipes.length">
      <el-table-column prop="title" label="Title" />
      <el-table-column prop="difficulty" label="Difficulty" />
      <el-table-column prop="creator" label="Chef Name" />
      <el-table-column label="Ingredients">
        <template #default="{ row }">
          {{ row.ingredients.length }}
        </template>
      </el-table-column>
      <el-table-column prop="createdDate" label="Created Date">
        <template #default="{ row }">
          {{ new Date(row.createdDate).toLocaleDateString() }}
        </template>
      </el-table-column>
    </el-table> -->

    <!-- Recipe Card Grid -->
    <el-row :gutter="20">
      <el-col
        v-for="recipe in paginatedRecipes"
        :key="recipe.id"
        :span="6"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
      >
        <RecipeCard :recipe="recipe" />
      </el-col>
    </el-row>

    <!-- Pagination -->
    <el-pagination
      v-if="recipes.length > pageSize"
      v-model:current-page="currentPage"
      :page-size="pageSize"
      layout="prev, pager, next"
      :total="recipes.length"
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
</style>
