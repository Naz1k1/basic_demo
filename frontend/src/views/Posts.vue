<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">文章管理</h1>
      <el-button type="primary" @click="handleCreate">
        <el-icon class="mr-1"><Plus /></el-icon>
        新建文章
      </el-button>
    </div>

    <el-card shadow="never">
      <div class="flex justify-between mb-4">
        <el-input
            v-model="searchQuery"
            placeholder="搜索文章标题"
            style="width: 300px"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>

        <el-button-group>
          <el-button :type="statusFilter === '' ? 'primary' : ''" @click="changeStatus('')">全部</el-button>
          <el-button :type="statusFilter === 'published' ? 'primary' : ''" @click="changeStatus('published')">已发布</el-button>
          <el-button :type="statusFilter === 'draft' ? 'primary' : ''" @click="changeStatus('draft')">草稿</el-button>
          <el-button :type="statusFilter === 'trash' ? 'primary' : ''" @click="changeStatus('trash')">回收站</el-button>
        </el-button-group>
      </div>

      <el-table :data="posts" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题">
          <template #default="{ row }">
            <router-link :to="`/content/posts/edit/${row.id}`" class="text-blue-500 hover:underline">
              {{ row.title }}
            </router-link>
            <el-tag v-if="row.status === 'draft'" size="small" type="warning" class="ml-2">草稿</el-tag>
            <el-tag v-if="row.status === 'trash'" size="small" type="danger" class="ml-2">回收站</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="tags" label="标签" width="150">
          <template #default="{ row }">
            <el-tag v-for="tag in row.tags" :key="tag" size="small" class="mr-1 mb-1">{{ tag }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="views" label="浏览" width="80" />
        <el-table-column prop="comments" label="评论" width="80" />
        <el-table-column prop="date" label="发布时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-dropdown trigger="click" @command="handleCommand($event, row)">
              <el-button size="small">
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="view">查看</el-dropdown-item>
                  <el-dropdown-item command="delete" v-if="row.status !== 'trash'">删除</el-dropdown-item>
                  <el-dropdown-item command="restore" v-if="row.status === 'trash'">恢复</el-dropdown-item>
                  <el-dropdown-item command="permanentDelete" divided v-if="row.status === 'trash'">永久删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="fetchPosts"
            @current-change="fetchPosts"
        />
      </div>
    </el-card>

    <!-- 新建/编辑文章对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="80%">
      <el-form :model="postForm" label-width="80px">
        <el-form-item label="文章标题">
          <el-input v-model="postForm.title" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="postForm.category" placeholder="请选择分类">
            <el-option
                v-for="item in categories"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="标签">
          <el-select
              v-model="postForm.tags"
              multiple
              filterable
              allow-create
              placeholder="请选择或输入标签"
          >
            <el-option
                v-for="item in allTags"
                :key="item"
                :label="item"
                :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="postForm.status">
            <el-radio label="published">发布</el-radio>
            <el-radio label="draft">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内容">
          <div style="border: 1px solid #dcdfe6; border-radius: 4px;">
            <TiptapEditor v-model="postForm.content" />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import TiptapEditor from '@/components/TiptapEditor.vue'

const loading = ref(false)
const searchQuery = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const posts = ref([
  {
    id: 1,
    title: 'Vue 3 新特性详解',
    category: '前端',
    tags: ['Vue', '前端'],
    views: 1024,
    comments: 23,
    date: '2023-05-01 10:30',
    status: 'published'
  },
  {
    id: 2,
    title: 'Element Plus 使用指南',
    category: '前端',
    tags: ['Element', 'UI'],
    views: 856,
    comments: 15,
    date: '2023-05-02 14:20',
    status: 'published'
  },
  {
    id: 3,
    title: 'Node.js 性能优化',
    category: '后端',
    tags: ['Node.js', '性能'],
    views: 723,
    comments: 8,
    date: '2023-05-03 09:15',
    status: 'draft'
  },
  {
    id: 4,
    title: '数据库设计原则',
    category: '数据库',
    tags: ['MySQL', '设计'],
    views: 432,
    comments: 5,
    date: '2023-05-04 16:45',
    status: 'trash'
  }
])

const categories = ref([
  { id: 1, name: '前端' },
  { id: 2, name: '后端' },
  { id: 3, name: '数据库' },
  { id: 4, name: '其他' }
])

const allTags = ref(['Vue', 'React', 'Node.js', '前端', '后端', '数据库', '设计', '性能'])

const dialogVisible = ref(false)
const dialogTitle = ref('新建文章')
const postForm = ref({
  id: null,
  title: '',
  category: '',
  tags: [],
  status: 'published',
  content: ''
})

const fetchPosts = async () => {
  loading.value = true
  // 这里应该是API调用
  // const res = await getPosts({
  //   page: currentPage.value,
  //   size: pageSize.value,
  //   search: searchQuery.value,
  //   status: statusFilter.value
  // })
  // posts.value = res.data.list
  // total.value = res.data.total
  loading.value = false
}

const handleSearch = () => {
  currentPage.value = 1
  fetchPosts()
}

const changeStatus = (status) => {
  statusFilter.value = status
  currentPage.value = 1
  fetchPosts()
}

const handleCreate = () => {
  dialogTitle.value = '新建文章'
  postForm.value = {
    id: null,
    title: '',
    category: '',
    tags: [],
    status: 'published',
    content: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑文章'
  postForm.value = {
    id: row.id,
    title: row.title,
    category: row.category,
    tags: [...row.tags],
    status: row.status,
    content: '这里是文章内容...'
  }
  dialogVisible.value = true
}

const handleCommand = (command, row) => {
  switch (command) {
    case 'view':
      // 查看文章
      break
    case 'delete':
      // 删除到回收站
      break
    case 'restore':
      // 从回收站恢复
      break
    case 'permanentDelete':
      // 永久删除
      break
  }
}

const handleSubmit = async () => {
  // 这里应该是API调用
  // if (postForm.value.id) {
  //   await updatePost(postForm.value)
  // } else {
  //   await createPost(postForm.value)
  // }
  dialogVisible.value = false
  fetchPosts()
}

onMounted(() => {
  fetchPosts()
})
</script>