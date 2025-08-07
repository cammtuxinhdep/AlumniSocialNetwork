import { useEffect, useState, useContext, useRef } from "react";
import { Button, Form, Alert } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import PostItem from "./PostItem";
import PostForm from "./PostForm";
import MySpinner from "./layout/MySpinner";
import { MyUserContext } from "../configs/Context";

const PostList = () => {
    const [posts, setPosts] = useState([]);
    const [user] = useContext(MyUserContext);
    const [page, setPage] = useState(1);
    const [q, setQ] = useState("");
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const debounceTimer = useRef(null);

    const loadPosts = async (search = q, pageNumber = page) => {
        try {
            setLoading(true);
            let url = `${endpoints.posts}?page=${pageNumber}`;
            if (search.trim()) url += `&kw=${encodeURIComponent(search.trim())}`;

            const res = await authApis().get(url);
            const data = res.data;

            if (pageNumber === 1) {
                setPosts(data);
            } else {
                setPosts(prev => [...prev, ...data]);
            }

            setHasMore(data.length >= 6); // Giả định page size = 6
        } catch (err) {
            console.error("Lỗi tải bài viết:", err);
        } finally {
            setLoading(false);
        }
    };

    // Xử lý debounce tìm kiếm
    useEffect(() => {
        if (debounceTimer.current) clearTimeout(debounceTimer.current);

        debounceTimer.current = setTimeout(() => {
            setPage(1);
            loadPosts(q, 1);
        }, 500);

        return () => clearTimeout(debounceTimer.current);
    }, [q]);

    // Gọi loadPosts khi page tăng (sau lần đầu)
    useEffect(() => {
        if (page > 1) loadPosts(q, page);
    }, [page]);

    const loadMore = () => {
        if (!hasMore || loading) return;
        setPage(prev => prev + 1);
    };

    return (
        <>
            <PostForm onPostCreated={() => {
                setPage(1);
                loadPosts(q, 1);
            }} />

            <Form>
                <Form.Group className="mb-3 mt-2">
                    <Form.Control
                        value={q}
                        onChange={e => setQ(e.target.value)}
                        type="text"
                        placeholder="Tìm kiếm bài viết..."
                    />
                </Form.Group>
            </Form>

            {(!posts || posts.length === 0) && !loading && (
                <Alert variant="info">Không có bài viết nào!</Alert>
            )}

            {posts.map(p => (
                <PostItem key={p.id} post={p} onPostUpdate={() => {
                    setPage(1);
                    loadPosts(q, 1);
                }} />
            ))}

            {loading && <MySpinner />}

            {!loading && hasMore && posts.length > 0 && (
                <div className="text-center mt-2 mb-2">
                    <Button variant="primary" onClick={loadMore}>Xem thêm...</Button>
                </div>
            )}
        </>
    );
};

export default PostList;
