import { createRouter, createWebHistory } from 'vue-router'
import { authApi } from '../api/auth'

// 路由懒加载
const Login = () => import('../views/Login.vue')
const UserList = () => import('../views/UserList.vue')

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: { requiresAuth: false }
    },
    {
        path: '/users',
        name: 'UserList',
        component: UserList,
        meta: { requiresAuth: true }
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
    if (to.meta.requiresAuth) {
        try {
            await authApi.getUserInfo()
            next()
        } catch (error) {
            next('/login')
        }
    } else {
        next()
    }
})

export default router