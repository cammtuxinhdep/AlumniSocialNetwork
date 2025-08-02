import { useState, useEffect, useContext } from "react";
import { Alert, Button, Col, Form, Row } from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";
import MySpinner from "./layout/MySpinner";
import PostItem from "./PostItem";
import { MyUserContext } from "../configs/Context";

const PostList = () => {
  const [currentUser] = useContext(MyUserContext);
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [q, setQ] = useState("");
  const [page, setPage] = useState(1);

  const loadPosts = async () => {
    try {
      setLoading(true);
      let res = await Apis.get(endpoints["posts"], {
        params: {
          page,
          kw: q || undefined,
        },
      });
      const newPosts = res.data;
      if (newPosts.length === 0 && page > 1) {
        setPage(0);
      } else {
        setPosts((prev) => (page === 1 ? newPosts : [...prev, ...newPosts]));
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const delayDebounce = setTimeout(() => {
      if (page > 0) loadPosts();
    }, 500);
    return () => clearTimeout(delayDebounce);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, q]);

  useEffect(() => {
    setPage(1);
  }, [q]);

  const loadMore = () => {
    setPage((prev) => prev + 1);
  };

  const handlePostCreated = (newPost) => {
    setPosts([newPost, ...posts]);
  };

  return (
    <>
      {currentUser ? (
        <PostItem onPostCreated={handlePostCreated} />
      ) : (
        <Button
          variant="success"
          className="mb-3"
          onClick={() => (window.location.href = "/login")}
        >
          Đăng nhập để đăng bài
        </Button>
      )}
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

      {posts.length === 0 && !loading && <Alert variant="info">Không có bài viết nào!</Alert>}

      <Row>
        {posts.map((post) => (
          <Col key={post.id} xs={12} className="mb-3">
            <PostItem post={post} onPostUpdated={loadPosts} />
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