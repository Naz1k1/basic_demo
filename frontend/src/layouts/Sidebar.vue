<script setup>
import { ref } from 'vue'
import {
  Menu as IconMenu,
  Location,
  Document,
  Setting,
} from '@element-plus/icons-vue'

const isCollapse = ref(false)
</script>

<template>
  <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar">
    <div class="logo">
      <img src="/vite.svg" alt="logo" />
      <span v-show="!isCollapse">Admin System</span>
    </div>

    <el-menu
      :collapse="isCollapse"
      class="menu"
      router
      :default-active="$route.path"
    >
      <el-menu-item index="/dashboard">
        <el-icon><IconMenu /></el-icon>
        <template #title>仪表盘</template>
      </el-menu-item>
      
      <el-sub-menu index="1">
        <template #title>
          <el-icon><Location /></el-icon>
          <span>系统管理</span>
        </template>
        <el-menu-item index="/system/user">用户管理</el-menu-item>
        <el-menu-item index="/role">角色管理</el-menu-item>
      </el-sub-menu>

      <el-menu-item index="/settings">
        <el-icon><Setting /></el-icon>
        <template #title>系统设置</template>
      </el-menu-item>
    </el-menu>

    <div class="sidebar-footer">
      <el-button
        type="primary"
        :icon="isCollapse ? 'el-icon-d-arrow-right' : 'el-icon-d-arrow-left'"
        circle
        @click="isCollapse = !isCollapse"
      />
    </div>
  </el-aside>
</template>

<style scoped>
.sidebar {
  background-color: #fff;
  border-right: 1px solid #e4e7ed;
  transition: width 0.3s;
  overflow: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #e4e7ed;
  overflow: hidden;
}

.logo img {
  height: 32px;
  margin-right: 12px;
}

.menu {
  border-right: none;
  height: calc(100% - 120px);
}

.sidebar-footer {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-top: 1px solid #e4e7ed;
}
</style>