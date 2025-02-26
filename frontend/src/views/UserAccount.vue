<!-- src/views/UserAccount.vue -->
<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useUserStore } from '../store/userStore'
import { useRecipeStore } from '../store/recipeStore'
import RecipeCard from '../components/RecipeCard.vue'
import { ElCard, ElMessage, ElMessageBox, ElStatistic } from 'element-plus'
import router from '../router'

const userStore = useUserStore()
const recipeStore = useRecipeStore()

onMounted(async () => {
  await recipeStore.loadRecipes()
})

// Filter recipes to show only those created by the logged-in user
const userRecipes = computed(() => {
  return recipeStore.recipes.filter(recipe => recipe.userId === userStore.user?.userId)
})

// Aggregate statistics
const totalRecipes = computed(() => userRecipes.value.length)

const difficultyBreakdown = computed(() => {
  return userRecipes.value.reduce((acc, recipe) => {
    acc[recipe.difficulty] = (acc[recipe.difficulty] || 0) + 1
    return acc
  }, { EASY: 0, MEDIUM: 0, HARD: 0 })
})

const averageIngredients = computed(() => {
  if (userRecipes.value.length === 0) return 0;
  const totalIngredients = userRecipes.value.reduce((sum, recipe) => sum + recipe.ingredients.length, 0);
  return Number((totalIngredients / userRecipes.value.length).toFixed(1)); 
});

// Logout Function
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm("Are you sure you want to log out?", "Confirm Logout", {
      confirmButtonText: "Yes",
      cancelButtonText: "No",
      type: "warning",
    });

    userStore.logout() // Call logout method from userStore
    ElMessage.success("Logged out successfully");
    router.push("/login"); // Redirect to login page
  } catch (error) {
    console.error("Logout failed:", error);
    ElMessage.error("Logout failed. Please try again.");
  }
};
</script>

<template>
  <div class="user-account">
    <!-- User Info Card -->
    <el-card class="user-card">
      <template #header>
        <h2>User Profile</h2>
      </template>
      <div class="user-details">
        <p><strong>User ID:</strong> {{ userStore.user?.userId }}</p>
        <p><strong>Username:</strong> {{ userStore.user?.username }}</p>
        <p><strong>Full Name:</strong> {{ userStore.user?.fullName }}</p>
      </div>
      <el-button type="danger" @click="handleLogout" class="logout-button">Logout</el-button>
    </el-card>

    <!-- Statistics Section -->
    <div class="stats-container">
      <el-card class="stats-card">
        <el-statistic title="Total Recipes" :value="totalRecipes" />
      </el-card>
      <el-card class="stats-card">
        <el-statistic title="Easy Recipes" :value="difficultyBreakdown.EASY" />
      </el-card>
      <el-card class="stats-card">
        <el-statistic title="Medium Recipes" :value="difficultyBreakdown.MEDIUM" />
      </el-card>
      <el-card class="stats-card">
        <el-statistic title="Hard Recipes" :value="difficultyBreakdown.HARD" />
      </el-card>
      <el-card class="stats-card">
        <el-statistic title="Avg Ingredients" :value="averageIngredients" suffix="items" />
      </el-card>
    </div>

    <!-- Recipe Cards Grid -->
    <h3>Your Recipes</h3>
    <el-row :gutter="20" justify="center" class="recipe-grid">
      <el-col
        v-for="recipe in userRecipes"
        :key="recipe.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
        class="recipe-col"
      >
        <RecipeCard :recipe="recipe" />
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.user-account {
  padding: 20px;
  max-width: 1200px;
  margin: auto;
}

/* User Profile Card */
.user-card {
  margin-bottom: 20px;
  padding: 0px;
  text-align: center;
}

/* Stats Grid */
.stats-container {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 30px;
  justify-content: center;
}

.stats-card {
  width: 180px;
  text-align: center;
  padding: 10px;
}

.recipe-grid {
  display: flex;
  flex-wrap: wrap; /* Ensure items wrap properly */
  align-items: stretch; /* Make all cards stretch to equal height */
  row-gap: 10px;
  column-gap: 50px;
  justify-content: center;
}

.recipe-col {
  display: flex;
  justify-content: center;
}

</style>
