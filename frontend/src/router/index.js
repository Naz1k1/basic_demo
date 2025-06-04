import { createRouter, createWebHistory } from 'vue-router'
import Login from "../views/Login.vue";
import MainLayout from "../layouts/MainLayout.vue";
import { authApi } from '../api/auth'

const routes = [
    {
        path: '/',
        redirect: '/Login'
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: { 
            title: '登录',
            requiresAuth: false
        }
    },
    {
        path: '/',
        component: MainLayout,
        children: [
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: () => import('../views/Dashboard.vue'),
                meta: { 
                    title: '仪表盘',
                    requiresAuth: true
                }
            },
            {
                path: 'users',
                name: 'users',
                component: () => import('../views/system/user.vue'),
                meta: { 
                    title: '用户管理',
                    requiresAuth: true
                }
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
    // 设置页面标题
    document.title = to.meta.title ? `${to.meta.title} - 后台管理系统` : '后台管理系统'
    
    // 判断该路由是否需要登录权限
    if (to.meta.requiresAuth === false) {
        next()
        return
    }

    try {
        // 获取当前登录用户信息
        const res = await authApi.getCurrentUser()
        if (res.data.code === 200) {
            next()
        } else {
            next({
                path: '/login',
                query: { redirect: to.fullPath }
            })
        }
    } catch (error) {
        console.error('验证用户登录状态失败:', error)
        next({
            path: '/login',
            query: { redirect: to.fullPath }
        })
    }
})

export default router