import { createRouter, createWebHistory } from 'vue-router'

import Dashboard from '../views/Dashboard.vue'
import CreateRecipe from '../views/CreateRecipe.vue'
import RecipeDetail from '../views/RecipeDetail.vue'

const routes = [
  { path: '/', component: Dashboard },
  { path: '/create', component: CreateRecipe },
  { path: '/recipe/:id', component: RecipeDetail }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
