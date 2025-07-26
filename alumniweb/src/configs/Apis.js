import axios from "axios";
import cookie from "react-cookies";

// Base URL
export const BASE_URL = "http://localhost:8080/AlumniSocialNetwork/api";

// Các endpoint
export const endpoints = {
  posts: "/post",
  postDetail: (id) => `/post/${id}`,
  lockComment: (id, lock) => `/post/${id}/lock-comments?lock=${lock}`,
  comments: "/comment",
  commentByPost: (postId) => `/comment/post/${postId}`,
  commentDetail: (id) => `/comment/${id}`,
  reactions: "/reaction",
  reactionStats: (postId) => `/reaction/stats/${postId}`,
  userReaction: (userId, postId) => `/reaction/user/${userId}/post/${postId}`,
};

// Axios không auth (dùng mặc định)
export default axios.create({
  baseURL: BASE_URL,
});

// Axios có auth (sẽ dùng sau khi có đăng nhập)
export const authApi = () => {
  const token = cookie.load("token");
  return axios.create({
    baseURL: BASE_URL,
    headers: {
      Authorization: token ? `Bearer ${token}` : "",
    },
  });
};