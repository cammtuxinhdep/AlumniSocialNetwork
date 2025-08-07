import { useState } from "react";
import { Button, Form, Modal, Alert } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";

const PostForm = ({ onPostCreated }) => {
  const [show, setShow] = useState(false);
  const [content, setContent] = useState("");
  const [error, setError] = useState("");

  const handleClose = () => {
    setShow(false);
    setContent("");
    setError("");
  };

  const handleShow = () => setShow(true);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!content.trim()) {
      setError("Nội dung bài viết không được để trống!");
      return;
    }

    try {
      const res = await authApis().post(endpoints.posts, {
        content: content.trim()
      });

      onPostCreated(res.data); // Bài viết mới từ backend
      handleClose();
    } catch (err) {
      const errorMsg = err.response?.data || "Lỗi khi đăng bài. Vui lòng thử lại!";
      setError(errorMsg);

      if (err.response?.status === 401) {
        window.location.href = "/login";
      }

      console.error("Lỗi đăng bài:", err);
    }
  };

  return (
    <>
      <Button variant="primary" onClick={handleShow} className="w-100 mb-3">
        Đăng bài viết
      </Button>

      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Tạo bài viết</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {error && <Alert variant="danger">{error}</Alert>}
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Control
                as="textarea"
                rows={3}
                placeholder="Bạn đang nghĩ gì?"
                value={content}
                onChange={(e) => setContent(e.target.value)}
              />
            </Form.Group>
            <Button variant="primary" type="submit">
              Đăng
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </>
  );
};

export default PostForm;
