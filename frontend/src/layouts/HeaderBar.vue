<script setup lang="ts">

import { Monitor, Setting } from '@element-plus/icons-vue'

const onSettingClick = () => {
  console.log('跳转到设置页')
  // 例如使用 router.push('/settings')
}
</script>

<template>
  <el-header class="header-bar">
    <div class="left-section">
      <el-icon><Monitor /></el-icon>
      <span class="title">仪表盘</span>
    </div>
    <div class="right-section">
      <!-- 可添加搜索、通知、设置等 -->
      <el-button icon="Setting" circle @click="onSettingClick" />
    </div>
  </el-header>
</template>

<style scoped>
.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  padding: 0 24px;
  height: 60px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  border-bottom: 1px solid #f0f0f0;
}

.left-section {
  display: flex;
  align-items: center;
  gap: 8px;
}

.title {
  font-size: 18px;
  font-weight: 500;
  color: #333;
}

.right-section {
  display: flex;
  align-items: center;
  gap: 12px;
}
</style>