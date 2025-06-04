import { createRouter, createWebHistory } from 'vue-router'
import Login from "../views/Login.vue";
import MainLayout from "../layouts/MainLayout.vue";

const routes = [
    {
        path: '/',
        component: MainLayout,
        redirect: '/dashboard',
        children: [
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: () => import('../views/Dashboard.vue'),
                meta: { title: '仪表盘' }
            },
            // 系统管理
            {
                path: 'system',
                name: 'System',
                redirect: '/system/user',
                meta: { title: '系统管理' },
                children: [
                    {
                        path: 'user',
                        name: 'User',
                        component: () => import('../views/system/User.vue'),
                        meta: { title: '用户管理' }
                    },
                    // {
                    //     path: 'role',
                    //     name: 'Role',
                    //     component: () => import('../views/system/Role.vue'),
                    //     meta: { title: '角色管理' }
                    // }
                ]
            },
            // // 系统设置
            // {
            //     path: 'settings',
            //     name: 'Settings',
            //     component: () => import('../views/Settings.vue'),
            //     meta: { title: '系统设置' }
            // }
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: { title: '登录' }
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
    // 设置页面标题
    document.title = to.meta.title ? `${to.meta.title} - 后台管理系统` : '后台管理系统'
    
    // 这里可以添加登录验证等逻辑
    // const token = localStorage.getItem('token')
    // if (!token && to.path !== '/login') {
    //     next('/login')
    // } else {
    //     next()
    // }
    
    next()
})

export default router