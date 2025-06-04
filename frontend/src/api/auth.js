import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json'
    }
})

export const authApi = {
    login(username, password, rememberMe = false) {
        return api.post('/auth/login', {
            username,
            password,
            rememberMe
        })
    },

    logout() {
        return api.post('/auth/logout')
    },

    getCurrentUser() {
        return api.get('/auth/current')
    }
}