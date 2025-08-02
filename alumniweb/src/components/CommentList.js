import { useState } from "react";
import { Button, Image, Form, Modal, Dropdown } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import { formatTimeVi } from "../formatters/TimeFormatter";

const CommentList = ({ comments, currentUser, postUserId, isCommentLocked, onDeleteComment, onEditComment, onCommentAdded }) => {
  const [expandedComments, setExpandedComments] = useState({});
  const [newComment, setNewComment] = useState("");
  const [showEdit, setShowEdit] = useState(false);
  const [editCommentId, setEditCommentId] = useState(null);
  const [editContent, setEditContent] = useState("");
  const [showCommentBox, setShowCommentBox] = useState(false);

  const toggleReplies = (commentId) => {
    setExpandedComments((prev) => ({
      ...prev,
      [commentId]: !prev[commentId],
    }));
  };

  const handleAddComment = async () => {
    if (isCommentLocked || !currentUser?.id || !newComment.trim()) return;
    try {
      const res = await authApis().post(endpoints.comments, {
        content: newComment,
        postId: comments[0]?.postId || postUserId,
        userId: currentUser.id,
      });
      if (onCommentAdded) onCommentAdded(res.data);
      setNewComment("");
      setShowCommentBox(false);
    } catch (err) {
      console.error("Lỗi gửi bình luận:", err);
    }
  };

  const renderReplies = (comment) => {
    const isExpanded = expandedComments[comment.id];
    if (!comment.replies || comment.replies.length === 0) return null;

    if (!isExpanded) {
      return (
        <Button
          variant="link"
          size="sm"
          className="ps-5 text-primary"
          onClick={() => toggleReplies(comment.id)}
        >
          Xem tất cả {comment.replies.length} phản hồi
        </Button>
      );
    }

    return comment.replies.map((reply) => (
      <div key={reply.id} className="d-flex ps-5 pt-2">
        <Image
          src={reply.user?.avatar || "https://via.placeholder.com/28"}
          roundedCircle
          width={28}
          height={28}
          className="me-2 mt-1"
        />
        <div className="bg-light px-3 py-2 rounded-lg w-100">
          <div style={{ fontWeight: 600 }}>{reply.user?.name || "Ẩn danh"}</div>
          <div style={{ fontSize: "0.95rem" }}>{reply.content}</div>
          <div className="text-muted mt-1" style={{ fontSize: "0.75rem" }}>
            {reply.createdAt ? formatTimeVi(reply.createdAt) : "Không rõ thời gian"}
          </div>
        </div>
      </div>
    ));
  };

  return (
    <div className="pt-3">
      {!isCommentLocked && currentUser?.id && (
        <div>
          <Button
            variant="outline-secondary"
            size="sm"
            className="rounded-pill mb-2"
            onClick={() => setShowCommentBox(!showCommentBox)}
          >
            Bình luận
          </Button>
          {showCommentBox && (
            <Form.Group className="mb-2">
              <Form.Control
                type="text"
                placeholder="Viết bình luận..."
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                onKeyPress={(e) => e.key === "Enter" && handleAddComment()}
                className="rounded-pill p-2"
              />
            </Form.Group>
          )}
        </div>
      )}

      {comments.map((comment) => (
        <div key={comment.id} className="d-flex pt-3 align-items-start">
          <Image
            src={comment.user?.avatar || "https://via.placeholder.com/35"}
            roundedCircle
            width={35}
            height={35}
            className="me-3"
          />
          <div className="flex-grow-1">
            <div className="bg-light p-3 rounded-lg position-relative">
              <strong>{comment.user?.name || "Ẩn danh"}</strong>
              <div className="mt-1" style={{ fontSize: "1rem", lineHeight: "1.4" }}>{comment.content}</div>
              <div className="text-muted mt-1" style={{ fontSize: "0.75rem" }}>
                {comment.createdAt ? formatTimeVi(comment.createdAt) : "Không rõ thời gian"}
              </div>
              {(currentUser?.id === comment.userId || currentUser?.id === postUserId) && (
                <Dropdown className="position-absolute top-0 end-0">
                  <Dropdown.Toggle
                    variant="light"
                    size="sm"
                    id={`dropdown-${comment.id}`}
                    className="rounded-circle p-1"
                    style={{ minWidth: "30px", lineHeight: "1" }}
                  >
                    <span className="text-muted">⋮</span>
                  </Dropdown.Toggle>
                  <Dropdown.Menu>
                    {currentUser?.id === comment.userId && (
                      <Dropdown.Item onClick={() => {
                        setEditCommentId(comment.id);
                        setEditContent(comment.content);
                        setShowEdit(true);
                      }}>
                        Sửa
                      </Dropdown.Item>
                    )}
                    {(currentUser?.id === postUserId || currentUser?.id === comment.userId) && (
                      <Dropdown.Item onClick={() => onDeleteComment(comment.id)} className="text-danger">
                        Xóa
                      </Dropdown.Item>
                    )}
                  </Dropdown.Menu>
                </Dropdown>
              )}
            </div>

            {renderReplies(comment)}
          </div>
        </div>
      ))}

      <Modal show={showEdit} onHide={() => setShowEdit(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Sửa bình luận</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Control
            as="textarea"
            rows={2}
            value={editContent}
            onChange={(e) => setEditContent(e.target.value)}
            className="rounded"
          />
          <Button
            variant="primary"
            className="mt-2 rounded-pill"
            onClick={() => {
              onEditComment(editCommentId, editContent);
              setShowEdit(false);
            }}
          >
            Lưu
          </Button>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default CommentList;