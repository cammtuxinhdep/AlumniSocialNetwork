import { useEffect, useState, useContext } from "react";
import Apis, { authApis, endpoints } from "../configs/Apis";
import PostItem from "./PostItem";
import PostForm from "./PostForm";
import MySpinner from "./layout/MySpinner";
import { MyUserContext } from "../configs/Context";
import cookie from 'react-cookies';

const PostList = () => {
    const [posts, setPosts] = useState(null);
    const [user] = useContext(MyUserContext);

    const loadPosts = () => {
        const token = cookie.load('token');
        console.log("Token in cookie:", token);
        authApis().get(endpoints.posts)
            .then(res => setPosts(res.data))
            .catch(err => console.error("Lỗi tải bài viết:", err.response ? err.response.data : err.message));
    };

    useEffect(() => {
        loadPosts();
    }, []);

    if (posts === null) return <MySpinner />;

    return (
        <>
            <PostForm onPostCreated={loadPosts} />
            {posts.map(p => <PostItem key={p.id} post={p} onPostUpdate={loadPosts} />)}
        </>
    );
};

export default PostList;