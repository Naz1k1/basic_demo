<script setup>
import Sidebar from "./Sidebar.vue";
import HeaderBar from "./HeaderBar.vue";
</script>

<template>
  <el-container class="main-container">
    <Sidebar />
    <el-container class="right-container">
      <HeaderBar />
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.main-container {
  height: 100vh;
  width: 100vw;
}

.right-container {
  flex-direction: column;
  overflow: hidden;
}

.main-content {
  padding: 20px;
  background-color: #f7f9fc;
  overflow-y: auto;
  height: calc(100vh - 60px);
}
</style>