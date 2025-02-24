<script setup lang="ts">
import { useRoute } from 'vue-router'
import { useRecipeStore } from '../store/recipeStore'

const route = useRoute()
const recipeStore = useRecipeStore()
const recipe = recipeStore.getRecipeById(Number(route.params.id))
</script>

<template>
  <el-card v-if="recipe">
    <h2>{{ recipe.title }}</h2>
    <p><strong>Difficulty:</strong> {{ recipe.difficulty }}</p>
    <p><strong>Creator:</strong> {{ recipe.creator }}</p>
    <p><strong>Created Date:</strong> {{ recipe.createdDate }}</p>
    <h3>Ingredients:</h3>
    <ul>
      <li v-for="ingredient in recipe.ingredients" :key="ingredient">{{ ingredient }}</li>
    </ul>
    <h3>Instructions:</h3>
    <p>{{ recipe.instructions }}</p>
  </el-card>
</template>
