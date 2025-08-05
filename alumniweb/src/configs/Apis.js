import axios from "axios";
import cookie from 'react-cookies';

const BASE_URL = 'http://localhost:8080/AlumniSocialNetwork/api/';

export const endpoints = {
    posts: 'post',
    postDetail: (id) => `post/${id}`,
    lockComment: (id, lock) => `post/${id}/lock-comments?lock=${lock}`,

    comments: 'comment',
    commentByPost: (postId) => `comment/post/${postId}`,
    commentDetail: (id) => `comment/${id}`,

    reactions: 'reaction',
    reactionStats: (postId) => `reaction/stats/${postId}`,
    userReaction: (userId, postId) => `reaction/user/${userId}/post/${postId}`,

    login: 'login',
    register: 'register',
    profile: 'secure/profile'
};

export const authApis = () => axios.create({
    baseURL: BASE_URL,
    headers: {
        'Authorization': `Bearer ${cookie.load('token')}`
    }
});

export default axios.create({
    baseURL: BASE_URL
});
