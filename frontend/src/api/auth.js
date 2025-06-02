import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    withCredentials: true
})

export const authApi = {
    login(username, password) {
        const formData = new FormData()
        formData.append('username', username)
        formData.append('password', password)
        return api.post('/auth/login', formData)
    },

    logout() {
        return api.post('/auth/logout')
    },

    getUserInfo() {
        return api.get('/auth/info')
    }
}