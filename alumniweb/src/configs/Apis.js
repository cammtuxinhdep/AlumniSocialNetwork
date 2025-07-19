// src/configs/Apis.js
import axios from "axios";

const BASE_URL = "http://localhost:8080/AlumniSocialNetwork/api";

const apiClient = axios.create({
  baseURL: BASE_URL,
  // bạn có thể thêm headers, timeout, interceptors ở đây nếu cần
});

// Các endpoint REST API
const endpoints = {
  posts: "/posts",
  comments: "/comments",
};

// Các hàm gọi API cho Posts và Comments
const Apis = {
  // Posts
  getAllPosts: () => apiClient.get(endpoints.posts),
  getPostById: (id) => apiClient.get(`${endpoints.posts}/${id}`),
  createPost: (data) => apiClient.post(endpoints.posts, data),
  updatePost: (id, data) => apiClient.put(`${endpoints.posts}/${id}`, data),
  deletePost: (id) => apiClient.delete(`${endpoints.posts}/${id}`),

  // Comments
  getCommentsByPostId: (postId) =>
    apiClient.get(`${endpoints.comments}?postId=${postId}`),
  createComment: (data) => apiClient.post(endpoints.comments, data),
  updateComment: (id, data) => apiClient.put(`${endpoints.comments}/${id}`, data),
  deleteComment: (id) => apiClient.delete(`${endpoints.comments}/${id}`),
};

export default Apis;
