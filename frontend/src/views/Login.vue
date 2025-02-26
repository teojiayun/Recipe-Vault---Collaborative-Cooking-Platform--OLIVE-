<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/userStore'

const username = ref('')
const password = ref('')
const errorMsg = ref('')
const loading = ref(false)

const router = useRouter()
const userStore = useUserStore()

const login = async () => {
  loading.value = true
  errorMsg.value = ''
  try {
    await userStore.login(username.value, password.value)
    router.push('/') // Navigate to dashboard or account page on success
  } catch (error) {
    errorMsg.value = 'Invalid username or password'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <h2>Login</h2>
    <el-form @submit.prevent="login" class="login-form">
      <el-form-item label="Username">
        <el-input v-model="username" />
      </el-form-item>
      <el-form-item label="Password">
        <el-input type="password" v-model="password" />
      </el-form-item>
      <div v-if="errorMsg" class="error">{{ errorMsg }}</div>
      <el-button type="primary" native-type="submit" :loading="loading">
        Login
      </el-button>
    </el-form>
  </div>
</template>

<style scoped>
.login-container {
  max-width: 400px;
  margin: auto;
  padding: 20px;
  text-align: center;
}
.error {
  color: red;
  margin-bottom: 10px;
}
</style>
