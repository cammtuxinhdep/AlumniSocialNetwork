import axios from "axios";
import cookie from 'react-cookies';

const BASE_URL = 'http://localhost:8080/AlumniSocialNetwork/api/';

export const endpoints = {
    posts: 'secure/post',
    postDetail: (id) => `secure/post/${id}`,
    lockComment: (id, lock) => `secure/post/${id}/lock-comments?lock=${lock}`,

    comments: 'secure/comment',
    commentByPost: (postId) => `secure/comment/post/${postId}`,
    commentDetail: (id) => `secure/comment/${id}`,
    commentReplies: (parentId) => `secure/comment/replies/${parentId}`,
    deleteComment: (id) => `secure/comment/${id}`,
    updateComment: (id) => `secure/comment/${id}`,

    reactions: 'secure/reaction',
    reactionStats: (postId) => `secure/reaction/stats/${postId}`,
    userReaction: (postId) => `secure/reaction/user/post/${postId}`,

    surveys: "secure/survey", 
    surveyDetail: (id) => `secure/survey/${id}`, 
    surveySearch: (title) => `secure/survey/search?title=${title}`, 
    surveyStats: (id) => `secure/survey/stats/${id}`,
    surveyVote: (id) => `secure/survey/${id}/vote`,

    login: 'login',
    register: 'register',
    profile: (username) => `secure/profile/${username}`,
    changePassword: 'secure/password',
    changeAvatar: 'secure/avatar',
    changeCover: 'secure/cover',
};

export const authApis = () => axios.create({
    baseURL: BASE_URL,
    headers: {
        'Authorization': `Bearer ${cookie.load('token')}`,
    }
});

export default axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});