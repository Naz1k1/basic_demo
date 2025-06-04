<template>
  <div class="user-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="handleAdd">添加用户</el-button>
        </div>
      </template>

      <el-table :data="userList" style="width: 100%" v-loading="loading">
        <el-table-column prop="userId" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
                size="small"
                type="danger"
                @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
                size="small"
                :type="scope.row.status === 1 ? 'warning' : 'success'"
                @click="handleStatusChange(scope.row)"
            >
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next"
            @size-change="handleSizeChange"
            @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 用户表单对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="formType === 'add' ? '添加用户' : '编辑用户'"
        width="500px"
        @close="resetForm"
    >
      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item
            label="密码"
            prop="password"
            :rules="formType === 'add' ? rules.password : []"
        >
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { userApi } from '../../api/user.js'

// 数据状态
const loading = ref(false)
const userList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const formType = ref('add')
const formRef = ref(null)

// 表单数据
const form = ref({
  userId: undefined,
  username: '',
  password: '',
  email: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

// 获取用户列表
const getUserList = async () => {
  loading.value = true
  try {
    const res = await userApi.getUsers(pageNum.value, pageSize.value)
    if (res.data.code === 200) {
      userList.value = res.data.data.records || []
      total.value = res.data.data.total || 0
    } else {
      ElMessage.error(res.data.msg || '获取用户列表失败')
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  form.value = {
    userId: undefined,
    username: '',
    password: '',
    email: ''
  }
}

// 添加用户
const handleAdd = () => {
  formType.value = 'add'
  resetForm()
  dialogVisible.value = true
}

// 编辑用户
const handleEdit = (row) => {
  formType.value = 'edit'
  form.value = {
    userId: row.userId,
    username: row.username,
    email: row.email,
    password: '' // 编辑时密码为空
  }
  dialogVisible.value = true
}

// 删除用户
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该用户?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await userApi.deleteUser(row.userId)
      if (res.data.code === 200) {
        ElMessage.success('删除成功')
        await getUserList()
      } else {
        ElMessage.error(res.data.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除用户失败:', error)
      ElMessage.error('删除用户失败')
    }
  })
}

// 处理状态变更
const handleStatusChange = async (row) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1
    const res = await userApi.updateUserStatus(row.userId, newStatus)
    if (res.data.code === 200) {
      ElMessage.success(`${newStatus === 1 ? '启用' : '禁用'}成功`)
      await getUserList()
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    console.error('更新用户状态失败:', error)
    ElMessage.error('更新用户状态失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const api = formType.value === 'add' ? userApi.createUser : userApi.updateUser
        const res = await api(form.value)
        if (res.data.code === 200) {
          ElMessage.success(formType.value === 'add' ? '添加成功' : '更新成功')
          dialogVisible.value = false
          getUserList()
        } else {
          ElMessage.error(res.data.message || (formType.value === 'add' ? '添加失败' : '更新失败'))
        }
      } catch (error) {
        console.error(formType.value === 'add' ? '添加用户失败:' : '更新用户失败:', error)
        ElMessage.error(formType.value === 'add' ? '添加用户失败' : '更新用户失败')
      }
    }
  })
}

// 页码改变
const handlePageChange = (val) => {
  pageNum.value = val
  getUserList()
}

// 每页条数改变
const handleSizeChange = (val) => {
  pageSize.value = val
  pageNum.value = 1
  getUserList()
}

// 页面加载时获取数据
onMounted(() => {
  getUserList()
})
</script>

<style scoped>
.user-list {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>