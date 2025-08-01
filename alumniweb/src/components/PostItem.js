import { useState, useEffect, useCallback, useContext } from "react";
import axios from "../configs/Apis";
import { endpoints } from "../configs/Apis";
import CommentList from "./CommentList";
import ReactionStats from "./ReactionStats";
import { MyUserContext } from "../configs/Context";
import { Card, Button, Image, Spinner } from "react-bootstrap";
import { formatTimeVi } from "../formatters/TimeFormatter";

const reactionEmojis = {
  LIKE: "👍",
  HAHA: "😂",
  LOVE: "❤️",
};

const PostItem = ({ post }) => {
  const [comments, setComments] = useState([]);
  const [reactions, setReactions] = useState([]);
  const [userReaction, setUserReaction] = useState(null);
  const [loading, setLoading] = useState(true);

  const [currentUser] = useContext(MyUserContext);

  const loadComments = useCallback(async () => {
    try {
      const res = await axios.get(endpoints.commentByPost(post.id));
      setComments(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("Lỗi tải bình luận:", err);
      setComments([]);
    }
  }, [post.id]);

  const loadReactions = useCallback(async () => {
    try {
      const res = await axios.get(endpoints.reactionStats(post.id));
      const stats = res.data && typeof res.data === "object"
        ? Object.entries(res.data).map(([reactionType, count]) => ({
            reactionType,
            count,
          }))
        : [];
      setReactions(stats);
    } catch (err) {
      console.error("Lỗi tải reactions:", err);
      setReactions([]);
    }
  }, [post.id]);

  const loadUserReaction = useCallback(async () => {
    try {
      if (!currentUser?.id) return;
      const res = await axios.get(endpoints.userReaction(currentUser.id, post.id));
      setUserReaction(res.data);
    } catch (err) {
      console.error("Lỗi tải reaction người dùng:", err);
      setUserReaction(null);
    }
  }, [currentUser, post.id]);

  const sendReaction = async (reactionType) => {
    if (!currentUser?.id) return;
    try {
      const payload = {
        type: reactionType,
        postId: post.id,
        userId: currentUser.id,
      };
      await axios.post(endpoints.reactions, payload);
      await Promise.all([loadReactions(), loadUserReaction()]);
    } catch (err) {
      console.error("Lỗi gửi cảm xúc:", err);
    }
  };

  useEffect(() => {
    const loadAll = async () => {
      setLoading(true);
      await Promise.all([loadComments(), loadReactions(), loadUserReaction()]);
      setLoading(false);
    };
    loadAll();
  }, [loadComments, loadReactions, loadUserReaction]);

  if (loading) return <Spinner animation="border" variant="primary" />;

  return (
    <Card className="mb-4 shadow-sm">
      <Card.Body>
        {/* Header */}
        <div className="d-flex align-items-center mb-3">
          <Image
            src={post.user?.avatar || "https://via.placeholder.com/40"}
            roundedCircle
            width="48"
            height="48"
            className="me-2"
          />
          <div>
            <h6 className="mb-0">{post.user?.fullName || "Ẩn danh"}</h6>
            <small className="text-muted">
              {post.createdAt ? formatTimeVi(post.createdAt) : "Không rõ thời gian"}
            </small>
          </div>
        </div>

        {/* Nội dung bài viết */}
        <p>{post.content || "Không có nội dung."}</p>
        {post.image && (
          <div className="mb-3 text-center">
            <img
              src={post.image}
              alt="Hình ảnh bài viết"
              className="img-fluid rounded"
            />
          </div>
        )}

        {/* Thống kê reactions */}
        <ReactionStats reactions={reactions} emojis={reactionEmojis} />

        {/* Reaction người dùng */}
        {userReaction && (
          <div className="mt-2 text-primary">
            Bạn đã thả {reactionEmojis[userReaction.reactionType] || userReaction.reactionType}
          </div>
        )}

        {/* Các nút reaction */}
        <div className="d-flex gap-2 mt-2">
          {["LIKE", "HAHA", "LOVE"].map((type) => (
            <Button
              key={type}
              variant="outline-primary"
              onClick={() => sendReaction(type)}
              size="sm"
            >
              {reactionEmojis[type]} {type}
            </Button>
          ))}
        </div>

        {/* Danh sách bình luận */}
        <div className="mt-3">
          <CommentList comments={comments} />
        </div>
      </Card.Body>
    </Card>
  );
};

export default PostItem;
