import { useState } from "react";
import { Button, Form, Modal, Alert } from "react-bootstrap";
import { authApis } from "../configs/Apis";

const PostForm = ({ onPostCreated }) => {
  const [show, setShow] = useState(false);
  const [content, setContent] = useState("");
  const [image, setImage] = useState(null);
  const [error, setError] = useState("");

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("content", content);
    if (image) formData.append("image", image);

    try {
      const res = await authApis().post("/post", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      onPostCreated(res.data);
      setContent("");
      setImage(null);
      setError("");
      handleClose();
    } catch (err) {
      setError("Lỗi khi đăng bài. Vui lòng thử lại!");
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
            <Form.Group className="mb-3">
              <Form.Control
                type="file"
                accept="image/*"
                onChange={(e) => setImage(e.target.files[0])}
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