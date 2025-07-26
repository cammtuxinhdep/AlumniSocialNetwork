import { useEffect, useState } from "react";
import { Alert, Button, Col, Form, Row } from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";
import MySpinner from "./layout/MySpinner";
import PostItem from "./PostItem";

const PostList = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [q, setQ] = useState("");
  const [page, setPage] = useState(1);

  const loadPosts = async () => {
    try {
      let url = `${endpoints.posts}?page=${page}`;
      if (q) url += `&kw=${q}`;

      const res = await Apis.get(url);

      if (res.data.length === 0 && page > 1) {
        setPage(0); // Dừng phân trang
      } else {
        if (page === 1) setPosts(res.data);
        else setPosts(prev => [...prev, ...res.data]);
      }
    } catch (err) {
      console.error("Lỗi tải bài viết:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    setLoading(true);
    const timer = setTimeout(() => {
      if (page > 0) loadPosts();
    }, 500);

    return () => clearTimeout(timer);
  }, [page, q]);

  useEffect(() => {
    setPage(1);
  }, [q]);

  const loadMore = () => {
    setPage(prev => prev + 1);
  };

  return (
    <>
      <Form>
        <Form.Group className="mb-3 mt-2">
          <Form.Control
            type="text"
            placeholder="Tìm kiếm bài viết..."
            value={q}
            onChange={e => setQ(e.target.value)}
          />
        </Form.Group>
      </Form>

      {posts.length === 0 && !loading && (
        <Alert variant="info">Không có bài viết nào!</Alert>
      )}

      <Row>
        {posts.map(post => (
          <Col key={post.id} md={6} className="mb-3">
            <PostItem post={post} />
          </Col>
        ))}
      </Row>

      {loading && <MySpinner />}

      {!loading && page > 0 && (
        <div className="text-center mb-3">
          <Button onClick={loadMore}>Xem thêm</Button>
        </div>
      )}
    </>
  );
};

export default PostList;
