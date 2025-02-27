<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/userStore'
import { ElMessage } from 'element-plus'

const username = ref('')
const password = ref('')
const errorMsg = ref('')
const loading = ref(false)

const router = useRouter()
const userStore = useUserStore()

// Check for expired token and redirect to login
onMounted(() => {
  if (userStore.isTokenExpired()) {
    userStore.logout()
    ElMessage.warning("Session expired. Please log in again.")
    router.push('/login')
  }
})

const login = async () => {
  loading.value = true
  errorMsg.value = ''
  try {
    await userStore.login(username.value, password.value)
    ElMessage.success("Login successful!")
    router.push('/') // Navigate to dashboard or account page on success
  } catch (error) {
    errorMsg.value = 'Invalid username or password'
    ElMessage.error("Invalid username or password")
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>Login</h2>
      <el-form @submit.prevent="login" class="login-form">
        <el-form-item label="Username">
          <el-input v-model="username" placeholder="Enter your username" clearable />
        </el-form-item>
        <el-form-item label="Password">
          <el-input type="password" v-model="password" placeholder="Enter your password" show-password clearable />
        </el-form-item>
        <div v-if="errorMsg" class="error">{{ errorMsg }}</div>
        <el-button type="primary" native-type="submit" :loading="loading" class="login-button">
          Login
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f4f4f4;
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 30px;
  text-align: center;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.error {
  color: red;
  margin-bottom: 10px;
  font-size: 14px;
}

.login-button {
  width: 100%;
}
</style>
