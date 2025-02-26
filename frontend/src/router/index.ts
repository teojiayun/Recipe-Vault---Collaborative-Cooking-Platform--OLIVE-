import { createRouter, createWebHistory } from 'vue-router'

import Dashboard from '../views/Dashboard.vue'
import CreateRecipe from '../views/CreateRecipe.vue'
import RecipeDetail from '../views/RecipeDetail.vue'
import UpdateRecipe from '../views/UpdateRecipe.vue'
import UserAccount from '../views/UserAccount.vue'
import Login from '../views/Login.vue'
import { useUserStore } from '../store/userStore'

const routes = [
  { path: '/', component: Dashboard, meta: { requiresAuth: true}},
  { path: '/create', component: CreateRecipe, meta: { requiresAuth: true} },
  { path: '/recipe/:id', component: RecipeDetail},
  { path: '/update-recipe/:id', component: UpdateRecipe, meta: { requiresAuth: true}},
  { path: '/account', component: UserAccount, meta: { requiresAuth: true}},
  { path: '/login', component: Login}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Redirect if user is not authenticated
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  userStore.loadUserFromStorage() // Ensure userStore is updated
  if (to.meta.requiresAuth && !userStore.user) {
    next('/login')
  } else {
    next()
  }
})

export default router
