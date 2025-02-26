<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useRecipeStore } from "../store/recipeStore";
import { useUserStore } from "../store/userStore";
import { apiService } from "../services/apiService";
import { ElMessage, ElMessageBox } from "element-plus";

const route = useRoute();
const router = useRouter();
const recipeStore = useRecipeStore();
const userStore = useUserStore();

// State to track loading
const isLoading = ref(true);

// Get recipe by ID with a fallback empty object
const recipe = computed(() => recipeStore.getRecipeById(Number(route.params.id)) || null);

// Check if logged-in user is the owner of this recipe
const isOwner = computed(() => recipe.value?.userId === userStore.user?.userId);

// Image handling
const imageSrc = ref("");
const imagePreviewVisible = ref(false); // Control image preview modal

onMounted(async () => {
  if (!recipe.value) {
    router.push("/"); // Redirect if recipe is not found
  } else {
    if (recipe.value.imageUrl) {
      imageSrc.value = await apiService.fetchImage(recipe.value.imageUrl);
    }
    isLoading.value = false; // Mark loading as finished
  }
});

// Difficulty tag color logic
const getDifficultyTagType = (difficulty: "EASY" | "MEDIUM" | "HARD") => {
  switch (difficulty) {
    case "EASY":
      return "success";
    case "MEDIUM":
      return "warning";
    case "HARD":
      return "danger";
    default:
      return "";
  }
};

// Handle recipe deletion
const deleteRecipe = async () => {
  try {
    await ElMessageBox.confirm("Are you sure you want to delete this recipe?", "Warning", {
      confirmButtonText: "Yes",
      cancelButtonText: "No",
      type: "warning",
    });
    await apiService.deleteRecipe(recipe.value?.id);
    ElMessage.success("Recipe deleted successfully");
    router.push("/");
  } catch (error) {
    ElMessage.error("Failed to delete recipe");
  }
};
</script>

<template>
  <!-- Show loading while fetching the recipe -->
  <el-container v-if="isLoading" class="loading-container">
    <el-empty description="Loading recipe..." />
  </el-container>

  <!-- Display recipe when data is available -->
  <el-container v-else-if="recipe">
    <el-main>
      <el-card class="detail-card">
        <!-- Recipe Image with Click-to-Preview -->
        <el-image
          class="recipe-image"
          :src="imageSrc"
          fit="cover"
          @click="imagePreviewVisible = true"
        />
        <el-dialog v-model="imagePreviewVisible" title="Recipe Image">
          <img class="preview-img" :src="imageSrc" alt="Recipe Image" />
        </el-dialog>

        <!-- Recipe Title & Difficulty -->
        <h2 class="recipe-title">{{ recipe?.title }}</h2>
        <el-tag :type="getDifficultyTagType(recipe?.difficulty)" class="difficulty-tag">
          {{ recipe?.difficulty }}
        </el-tag>

        <!-- Metadata -->
        <p class="created-date">
          <strong>Created:</strong> {{ recipe?.createdDate ? new Date(recipe.createdDate).toLocaleDateString() : "N/A" }}
        </p>

        <el-divider />

        <!-- Ingredients Section -->
        <h3>Ingredients</h3>
        <el-row :gutter="10">
          <el-col v-for="ingredient in recipe?.ingredients" :key="ingredient" :span="8">
            <el-card shadow="hover" class="ingredient-card">{{ ingredient }}</el-card>
          </el-col>
        </el-row>

        <el-divider />

        <!-- Instructions -->
        <h3>Instructions</h3>
        <el-card shadow="never" class="instructions-card">
          <p>{{ recipe?.instructions }}</p>
        </el-card>

        <el-divider />

        <!-- Action Buttons (Edit/Delete only for Owner) -->
        <el-row :gutter="10" justify="center">
          <el-col v-if="isOwner" :span="12">
            <el-button type="primary" @click="router.push(`/update-recipe/${recipe.id}`)" round>
              Edit Recipe
            </el-button>
          </el-col>
          <el-col v-if="isOwner" :span="12">
            <el-button type="danger" @click="deleteRecipe" round>
              Delete Recipe
            </el-button>
          </el-col>
          <el-col :span="24" class="back-button">
            <el-button type="info" @click="router.push('/')" round>
              Back to Home
            </el-button>
          </el-col>
        </el-row>
      </el-card>
    </el-main>
  </el-container>

  <!-- If recipe is not found -->
  <el-container v-else class="not-found-container">
    <el-empty description="Recipe not found." />
  </el-container>
</template>

<style scoped>
.recipe-details-container {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.loading-container,
.not-found-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60vh;
}

.detail-card {
  max-width: 800px;
  width: 100%;
  padding: 20px;
  text-align: center;
}

.recipe-title {
  font-size: 28px;
  margin-top: 15px;
}

.recipe-image {
  width: 100%;
  max-height: 350px;
  object-fit: cover;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.2s ease-in-out;
}

.recipe-image:hover {
  transform: scale(1.02);
}

.preview-img {
  width: 100%;
  max-height: 500px;
  object-fit: contain;
}

.created-date {
  font-size: 16px;
  color: #666;
}

.ingredient-card {
  text-align: center;
  font-size: 16px;
  padding: 10px;
  border-radius: 8px;
  margin-bottom: 10px;
}

.instructions-card {
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 8px;
  text-align: left;
}

.el-divider {
  margin: 20px 0;
}

.el-row {
  margin-top: 20px;
}

.difficulty-tag {
  font-size: 18px;
  font-weight: bold;
  padding: 10px 20px;
  border-radius: 8px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.back-button {
  text-align: center;
  margin-top: 15px;
}
</style>
