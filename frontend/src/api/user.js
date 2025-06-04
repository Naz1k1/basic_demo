import axios from "axios";

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json'
    }
})

export const userApi = {
    getUsers(pageNum = 1, pageSize = 10) {
        return api.get(`/users/page?pageNum=${pageNum}&pageSize=${pageSize}`)
    },

    createUser(data) {
        return api.post('/users', data)
    },

    deleteUser(id) {
        return api.delete(`/users/${id}`)
    },

    updateUser(data) {
        return api.put('/users', data)
    },

    getUserById(id) {
        return api.get(`/users/${id}`)
    },

    updateUserStatus(id, status) {
        return api.put(`/users/${id}/status`, { status })
    }
}