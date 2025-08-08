import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../configs/Apis";  
import { formatTimeVi } from "../formatters/TimeFormatter";
import { Card, Button, Form, Alert } from "react-bootstrap";

export default function PostDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [content, setContent] = useState("");
  const [error, setError] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    authApis().get(endpoints.postDetail(id))
      .then(res => {
        setPost(res.data);
        setContent(res.data.content);
      })
      .catch(err => {
        console.error(err);
        navigate("/profile");
      });
  }, [id, navigate]);

  const handleDelete = async () => {
    if (window.confirm("Bạn có chắc chắn muốn xóa bài viết này?")) {
      try {
        await authApis().delete(endpoints.postDetail(id));
        alert("Xóa thành công");
        navigate("/profile");
      } catch (err) {
        console.error(err);
        alert("Xóa thất bại");
      }
    }
  };

  const handleEdit = async () => {
    setError("");
    setSuccessMsg("");

    try {
      await authApis().put(endpoints.postDetail(id), { ...post, content });
      setSuccessMsg("Cập nhật thành công");
      setIsEditing(false);
      setPost(prev => ({ ...prev, content }));
    } catch (err) {
      console.error(err);
      setError("Cập nhật thất bại");
    }
  };

  if (!post) return <p>Đang tải...</p>;

  return (
    <Card className="my-4 shadow-sm">
      <Card.Body>
        <Card.Title className="h4">Chi tiết bài viết</Card.Title>
        <Card.Subtitle className="mb-2 text-muted">
          Ngày đăng: {formatTimeVi(post.createdDate)}
        </Card.Subtitle>

        <Form.Group className="mt-3">
          <Form.Label>Nội dung:</Form.Label>
          {isEditing ? (
            <Form.Control
              as="textarea"
              rows={5}
              value={content}
              onChange={e => setContent(e.target.value)}
            />
          ) : (
            <Card.Text className="mt-2">{post.content}</Card.Text>
          )}
        </Form.Group>

        {error && <Alert variant="danger" className="mt-3">{error}</Alert>}
        {successMsg && <Alert variant="success" className="mt-3">{successMsg}</Alert>}

        <div className="mt-4">
          {isEditing ? (
            <>
              <Button variant="success" onClick={handleEdit} className="me-2">Lưu</Button>
              <Button variant="secondary" onClick={() => { setIsEditing(false); setContent(post.content); }}>Hủy</Button>
            </>
          ) : (
            <>
              <Button variant="primary" onClick={() => setIsEditing(true)} className="me-2">Sửa</Button>
              <Button variant="danger" onClick={handleDelete}>Xóa</Button>
            </>
          )}
        </div>
      </Card.Body>
    </Card>
  );
}
