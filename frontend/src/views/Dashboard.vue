<template>
  <div>
    <h1 class="text-2xl font-bold mb-6">仪表盘</h1>

    <el-row :gutter="20" class="mb-6">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="flex items-center">
            <el-icon class="text-blue-500 mr-3" :size="40"><User /></el-icon>

          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="flex items-center">
            <el-icon class="text-green-500 mr-3" :size="40"><Document /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="flex items-center">
            <el-icon class="text-orange-500 mr-3" :size="40"><ChatLineRound /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="flex items-center">
            <el-icon class="text-purple-500 mr-3" :size="40"><View /></el-icon>

          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="flex justify-between items-center">

            </div>
          </template>
          <div id="visitChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">

          <div id="contentChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-6">
      <el-col :span="12">
        <el-card shadow="hover">


        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">


        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {ChatLineRound, Document, User, View} from "@element-plus/icons-vue";



</script>