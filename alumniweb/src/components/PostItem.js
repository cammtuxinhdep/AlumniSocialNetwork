import { useState, useEffect, useContext } from "react";
import { authApis, endpoints } from "../configs/Apis";
import CommentList from "./CommentList";
import ReactionStats from "./ReactionStats";
import { MyUserContext } from "../configs/Context";
import { Card, Button, Image, Row, Col, OverlayTrigger, Popover } from "react-bootstrap";
import { formatTimeVi } from "../formatters/TimeFormatter";
import MySpinner from "./layout/MySpinner";
import cookie from "react-cookies";

const reactionEmojis = {
  LIKE: "👍",
  HAHA: "😂",
  HEART: "❤️",
};

const PostItem = ({ post, onPostUpdated }) => {
  const [currentUser, dispatch] = useContext(MyUserContext);
  const [comments, setComments] = useState([]);
  const [reactions, setReactions] = useState([]);
  const [userReaction, setUserReaction] = useState(null);
  const [loading, setLoading] = useState(false);
  const [isCommentLocked, setIsCommentLocked] = useState(post.isCommentLocked || false);
  const [showComments, setShowComments] = useState(false);
  const [showReactionMenu, setShowReactionMenu] = useState(false);

  const loadComments = async () => {
    try {
      setLoading(true);
      let res = await authApis().get(endpoints["commentByPost"](post.id));
      setComments(res.data && Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const loadReactions = async () => {
    try {
      setLoading(true);
      let res = await authApis().get(endpoints["reactionStats"](post.id));
      console.info(res.data);
      const stats = res.data && typeof res.data === "object"
        ? Object.entries(res.data).map(([reactionType, count]) => ({
            reactionType,
            count: Number(count) || 0,
          }))
        : [];
      setReactions(stats);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const loadUserReaction = async () => {
    try {
      if (!currentUser?.id) return;
      setLoading(true);
      let res = await authApis().get(endpoints["userReaction"](currentUser.id, post.id));
      console.info(res.data);
      setUserReaction(res.data || null);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const sendReaction = async (reactionType) => {
    if (!currentUser?.id) return;
    try {
      setLoading(true);
      let payload = { type: reactionType, postId: post.id, userId: currentUser.id };
      await authApis().post(endpoints["reactions"], payload);
      await loadReactions();
      await loadUserReaction();
      setShowReactionMenu(false);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const toggleCommentLock = async () => {
    if (!currentUser?.id || currentUser.id !== post.userId) return;
    try {
      setLoading(true);
      await authApis().post(endpoints["lockComment"](post.id, !isCommentLocked));
      setIsCommentLocked(!isCommentLocked);
      if (onPostUpdated) onPostUpdated();
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const deleteComment = async (commentId) => {
    try {
      setLoading(true);
      if (currentUser?.id === post.userId || comments.find((c) => c.id === commentId)?.userId === currentUser?.id) {
        await authApis().delete(endpoints["commentDetail"](commentId));
        setComments(comments.filter((c) => c.id !== commentId));
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const updateComment = async (commentId, content) => {
    try {
      setLoading(true);
      if (comments.find((c) => c.id === commentId)?.userId === currentUser?.id) {
        await authApis().put(endpoints["commentDetail"](commentId), { content });
        setComments(comments.map((c) => (c.id === commentId ? { ...c, content } : c)));
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCommentAdded = (newComment) => {
    setComments([...comments, newComment]);
  };

  const toggleComments = () => {
    if (!isCommentLocked) {
      setShowComments(!showComments);
      if (!showComments && comments.length === 0) {
        loadComments();
      }
    }
  };

  const handleLogout = () => {
    dispatch({ type: "logout" });
    cookie.remove("token");
    window.location.href = "/login";
  };

  useEffect(() => {
    console.info("currentUser:", currentUser);
    setLoading(true);
    const loadAll = async () => {
      await loadReactions();
      await loadUserReaction();
    };
    loadAll();
    setLoading(false);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (loading) return <MySpinner />;

  const reactionPopover = (
    <Popover id="reaction-popover" style={{ minWidth: "120px" }}>
      <div className="p-2">
        <Button
          variant="light"
          className="w-100 text-start mb-1 rounded-pill"
          onClick={() => sendReaction("LIKE")}
        >
          <span>{reactionEmojis.LIKE}</span> Like
        </Button>
        <Button
          variant="light"
          className="w-100 text-start mb-1 rounded-pill"
          onClick={() => sendReaction("HAHA")}
        >
          <span>{reactionEmojis.HAHA}</span> Haha
        </Button>
        <Button
          variant="light"
          className="w-100 text-start rounded-pill"
          onClick={() => sendReaction("HEART")}
        >
          <span>{reactionEmojis.HEART}</span> Heart
        </Button>
      </div>
    </Popover>
  );

  return (
    <Card className="mb-4 shadow-sm rounded-lg overflow-hidden" style={{ backgroundColor: "#fff", border: "none" }}>
      <Card.Body>
        <div className="d-flex align-items-center p-3 border-bottom" style={{ backgroundColor: "#f9f9f9" }}>
          <Image src={post.user?.avatar || "https://via.placeholder.com/40"} roundedCircle width="40" height="40" className="me-3" />
          <div>
            <h6 className="mb-0 fw-bold text-dark">{post.user?.fullName || "Ẩn danh"}</h6>
            <small className="text-muted">{post.createdAt ? formatTimeVi(post.createdAt) : "Không rõ thời gian"}</small>
          </div>
        </div>

        <div className="p-4">
          <p className="text-dark mb-3" style={{ lineHeight: "1.6", fontSize: "1.1rem" }}>
            {post.content || "Không có nội dung."}
          </p>
          {post.image && (
            <div className="mb-3">
              <img
                src={post.image}
                alt="Hình ảnh bài viết"
                className="img-fluid rounded-lg"
                style={{ maxHeight: "500px", objectFit: "cover", width: "100%" }}
              />
            </div>
          )}
        </div>

        <div className="p-3 border-top">
          <ReactionStats reactions={reactions} emojis={reactionEmojis} />
        </div>

        {userReaction && (
          <div className="px-3 text-primary fw-bold" style={{ fontSize: "0.9rem" }}>
            Bạn đã thả {reactionEmojis[userReaction.type] || userReaction.type}
          </div>
        )}

        {currentUser?.id === post.userId && (
          <div className="px-3 pb-3 d-flex gap-2">
            <Button
              variant={isCommentLocked ? "danger" : "outline-secondary"}
              onClick={toggleCommentLock}
              size="sm"
              className="rounded-pill"
            >
              {isCommentLocked ? "Mở khóa bình luận" : "Khóa bình luận"}
            </Button>
            <Button variant="outline-danger" size="sm" className="rounded-pill" onClick={handleLogout}>
              Đăng xuất
            </Button>
          </div>
        )}

        <div className="p-3 border-top">
          <Row className="g-2">
            <Col>
              <OverlayTrigger
                trigger="hover"
                placement="top"
                overlay={reactionPopover}
                show={showReactionMenu}
                onToggle={setShowReactionMenu}
              >
                <Button
                  variant={userReaction?.type === "LIKE" ? "primary" : "outline-secondary"}
                  size="sm"
                  className="w-100 rounded-pill d-flex align-items-center justify-content-center gap-2"
                  onMouseEnter={() => setShowReactionMenu(true)}
                  onMouseLeave={() => setShowReactionMenu(false)}
                  disabled={!currentUser}
                >
                  <span>{reactionEmojis.LIKE}</span>
                  <span>Like</span>
                  {reactions.find((r) => r.reactionType === "LIKE")?.count > 0 && (
                    <span className="ms-auto text-muted">
                      {reactions.find((r) => r.reactionType === "LIKE").count}
                    </span>
                  )}
                </Button>
              </OverlayTrigger>
            </Col>
            <Col>
              <Button
                variant="outline-secondary"
                onClick={toggleComments}
                size="sm"
                className="w-100 rounded-pill d-flex align-items-center justify-content-center gap-2"
                disabled={isCommentLocked || !currentUser}
              >
                <span>💬</span>
                <span>Comment</span>
                {comments.length > 0 && <span className="ms-auto text-muted">{comments.length}</span>}
              </Button>
            </Col>
            <Col>
              <Button variant="outline-secondary" size="sm" className="w-100 rounded-pill d-flex align-items-center justify-content-center gap-2" disabled>
                <span>↪</span>
                <span>Share</span>
              </Button>
            </Col>
          </Row>
        </div>

        {showComments && (
          <div className="p-3 border-top" style={{ backgroundColor: "#f9f9f9" }}>
            <CommentList
              comments={comments}
              currentUser={currentUser}
              postUserId={post.userId}
              isCommentLocked={isCommentLocked}
              onDeleteComment={deleteComment}
              onEditComment={updateComment}
              onCommentAdded={handleCommentAdded}
            />
          </div>
        )}
      </Card.Body>
    </Card>
  );
};

export default PostItem;