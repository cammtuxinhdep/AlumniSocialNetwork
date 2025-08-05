import { useContext, useEffect, useState } from "react";
import { Alert, Button, Col, Form, Row } from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";
import MySpinner from "./layout/MySpinner";
import PostItem from "./PostItem";
import { MyUserContext } from "../configs/Context";

const PostList = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [q, setQ] = useState("");
  const [page, setPage] = useState(1);
  const [user] = useContext(MyUserContext); 
  const loadPosts = async () => {
    try {
      setLoading(true);
      const res = await Apis.get(endpoints.posts, {
        params: {
          page,
          kw: q || undefined,
        },
      });

      const newPosts = res.data;

      if (newPosts.length === 0 && page > 1) {
        setPage(0); // Không còn dữ liệu để load
      } else {
        setPosts((prev) => (page === 1 ? newPosts : [...prev, ...newPosts]));
      }
    } catch (err) {
      console.error("Lỗi tải bài viết:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const delayDebounce = setTimeout(() => {
      if (page > 0) loadPosts();
    }, 500);

    return () => clearTimeout(delayDebounce);
  }, [page, q]);

  useEffect(() => {
    setPage(1); 
  }, [q]);

  const loadMore = () => {
    setPage((prev) => prev + 1);
  };

  return (
    <>
      <Form>
        <Form.Group className="mb-3 mt-2">
          <Form.Control
            type="text"
            placeholder="Tìm kiếm bài viết..."
            value={q}
            onChange={(e) => setQ(e.target.value)}
          />
        </Form.Group>
      </Form>

      {posts.length === 0 && !loading && (
        <Alert variant="info">Không có bài viết nào!</Alert>
      )}

      {/* <Row>
        {posts.map((post) => (
          <Col key={post.id} md={6} className="mb-3">
            <PostItem post={post} currentUser={user} />
          </Col>
        ))}
      </Row> */}

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
