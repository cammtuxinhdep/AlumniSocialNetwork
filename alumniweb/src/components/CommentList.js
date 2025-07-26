import { useState } from "react";
import { Button, Image, Card } from "react-bootstrap";

const CommentList = ({ postId, comments }) => {
  const [expandedComments, setExpandedComments] = useState({});

  const toggleReplies = (commentId) => {
    setExpandedComments((prev) => ({
      ...prev,
      [commentId]: !prev[commentId],
    }));
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
          src={reply.user.avatar}
          roundedCircle
          width={28}
          height={28}
          className="me-2 mt-1"
        />
        <div className="bg-light px-3 py-2 rounded-3">
          <div style={{ fontWeight: 600 }}>{reply.user.name}</div>
          <div style={{ fontSize: "0.95rem" }}>{reply.content}</div>
          <div className="text-muted mt-1" style={{ fontSize: "0.75rem" }}>
            {new Date(reply.createdDate).toLocaleString()}
          </div>
        </div>
      </div>
    ));
  };

  return (
    <div className="pt-3">
      {comments.map((comment) => (
        <div key={comment.id} className="d-flex pt-3">
          <Image
            src={comment.user.avatar}
            roundedCircle
            width={35}
            height={35}
            className="me-2 mt-1"
          />
          <div>
            <div className="bg-light px-3 py-2 rounded-3">
              <strong>{comment.user.name}</strong>
              <div>{comment.content}</div>
              <div className="text-muted mt-1" style={{ fontSize: "0.75rem" }}>
                {new Date(comment.createdDate).toLocaleString()}
              </div>
            </div>

            {/* Replies */}
            {renderReplies(comment)}
          </div>
        </div>
      ))}
    </div>
  );
};

export default CommentList;
