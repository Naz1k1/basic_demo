<template>
  <el-config-provider>
    <RouterView />
  </el-config-provider>
</template>

<script setup>
import { RouterView } from 'vue-router'
import { ElConfigProvider } from 'element-plus'
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
}
</style>