import axios from "axios";

const api = axios.create({
    baseURL: 'http://localhost:8080/api'
})

export const userApi = {
    getUsers(pageNum = 1,pageSize = 10) {
        return api.get(`/user/get/page?pageNum=${pageNum}&pageSize=${pageSize}`)
    },

    addUser(data) {
        return api.post(`/user/add`, data)
    },

    deleteUser(id) {
        return api.delete(`/user/delete/${id}`)
    },

    updateUser(data) {
        return api.put(`/user/update`,data)
    },

    getUserById(id) {
        return api.get(`/user/get/${id}`)
    }
}