<script setup lang="ts">

</script>

<template>
  <div>
    <!-- Navigation Bar -->
    <nav class="navbar">
      <router-link to="/" class="nav-link">Dashboard</router-link>
      <router-link to="/create" class="nav-link">Create Recipe</router-link>
      <router-link to="/account" class="nav-link">Account</router-link>
    </nav>

    <!-- Main Content -->
    <div class="main-content">
      <router-view></router-view>
    </div>
  </div>
</template>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-around;
  align-items: center;
  background: #333;
  padding: 12px;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-size: 18px;
  padding: 10px 20px;
  margin: 0 10px;
}

.nav-link:hover {
  background: #555;
  border-radius: 5px;
}

.main-content {
  max-width: 1200px;
  margin: auto;
  padding-top: 60px;
}
</style>
