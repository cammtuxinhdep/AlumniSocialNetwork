import React, { useState, useEffect } from "react";
import { Card, Button, Form } from "react-bootstrap";
import api from "../configs/Apis";

const PostItem = ({ post, currentUser }) => {
  const [comments, setComments] = useState(post.comments || []);
  const [newComment, setNewComment] = useState("");
  const [loading, setLoading] = useState(false);

  // Đồng bộ comments khi prop post.comments thay đổi
  useEffect(() => {
    setComments(post.comments || []);
  }, [post.comments]);

  const handleAddComment = async () => {
    if (!newComment.trim()) return;

    setLoading(true);
    try {
      const res = await api.createComment({
        postId: post.id,
        content: newComment,
        userId: currentUser.id,
      });
      // Thêm comment mới vào list
      setComments([...comments, res.data]);
      setNewComment("");
    } catch (error) {
      console.error("Lỗi khi thêm bình luận:", error);
      alert("Không thể thêm bình luận: " + error.message);
    }
    setLoading(false);
  };

  const handleDeleteComment = async (commentId) => {
    if (!window.confirm("Bạn có chắc muốn xóa bình luận này?")) return;

    try {
      await api.deleteComment(commentId);
      // Xóa comment khỏi danh sách local
      setComments(comments.filter((cmt) => cmt.id !== commentId));
    } catch (error) {
      console.error("Lỗi khi xóa bình luận:", error);
      alert("Không thể xóa bình luận: " + error.message);
    }
  };

  const canDeleteComment = (comment) =>
    comment.user.id === currentUser.id || post.user.id === currentUser.id;

  const canEditComment = (comment) => comment.user.id === currentUser.id;

  return (
    <Card className="mb-3 shadow-sm">
      <Card.Body>
        <Card.Title>{post.user.fullName}</Card.Title>
        <Card.Text>{post.content}</Card.Text>

        {/* Reactions */}
        <div className="d-flex gap-2 mb-2">
          <Button size="sm" variant="outline-primary">👍 Like</Button>
          <Button size="sm" variant="outline-warning">😆 Haha</Button>
          <Button size="sm" variant="outline-danger">❤️ Love</Button>
        </div>

        {/* Comment input */}
        {!post.commentsLocked && (
          <div className="mb-2">
            <Form.Control
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              placeholder="Viết bình luận..."
              disabled={loading}
            />
            <Button
              size="sm"
              className="mt-2"
              onClick={handleAddComment}
              disabled={loading}
            >
              {loading ? "Đang gửi..." : "Gửi"}
            </Button>
          </div>
        )}

        {/* Comment list */}
        <div className="mt-3">
          {comments.length === 0 && <p>Chưa có bình luận nào.</p>}
          {comments.map((cmt) => (
            <div key={cmt.id} className="border rounded p-2 mb-2 bg-light">
              <strong>{cmt.user.fullName}</strong>: {cmt.content}
              <div className="text-end">
                {canEditComment(cmt) && (
                  <Button
                    size="sm"
                    variant="outline-info"
                    className="me-1"
                    disabled
                  >
                    Sửa (đang phát triển)
                  </Button>
                )}
                {canDeleteComment(cmt) && (
                  <Button
                    size="sm"
                    variant="outline-danger"
                    onClick={() => handleDeleteComment(cmt.id)}
                  >
                    Xóa
                  </Button>
                )}
              </div>
            </div>
          ))}
        </div>
      </Card.Body>
    </Card>
  );
};

export default PostItem;
