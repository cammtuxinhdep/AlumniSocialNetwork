import { useState, useEffect, useCallback } from "react";
import axios from "../configs/Apis";
import { endpoints } from "../configs/Apis";
import CommentList from "./CommentList";
import ReactionStats from "./ReactionStats";
import "../App.css";

const TEMP_USER_ID = 1;

// Map reaction types to emojis
const reactionEmojis = {
  LIKE: "👍",
  HAHA: "😂",
  LOVE: "❤️",
};

const PostItem = ({ post }) => {
  const [comments, setComments] = useState([]);
  const [reactions, setReactions] = useState([]);
  const [userReaction, setUserReaction] = useState(null);

  const loadComments = useCallback(async () => {
    try {
      const url = `${endpoints.commentByPost(post.id)}`;
      console.log("Fetching comments:", url);
      const res = await axios.get(url);
      console.log("Comments response:", res.data);
      setComments(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("Load comments failed:", {
        message: err.message,
        status: err.response?.status,
        data: err.response?.data,
      });
      setComments([]);
    }
  }, [post.id]);

  const loadReactions = useCallback(async () => {
    try {
      const url = `${endpoints.reactionStats(post.id)}`;
      console.log("Fetching reactions:", url);
      const res = await axios.get(url);
      console.log("Reactions response:", res.data);
      const stats = res.data && typeof res.data === "object"
        ? Object.entries(res.data).map(([reactionType, count]) => ({
            reactionType,
            count,
          }))
        : [];
      setReactions(stats);
    } catch (err) {
      console.error("Load reactions failed:", {
        message: err.message,
        status: err.response?.status,
        data: err.response?.data,
      });
      setReactions([]);
    }
  }, [post.id]);

  const loadUserReaction = useCallback(async () => {
    try {
      const url = `${endpoints.userReaction(TEMP_USER_ID, post.id)}`;
      console.log("Fetching user reaction:", url);
      const res = await axios.get(url);
      console.log("User reaction response:", res.data);
      setUserReaction(res.data);
    } catch (err) {
      console.error("Load user reaction failed:", {
        message: err.message,
        status: err.response?.status,
        data: err.response?.data,
      });
      setUserReaction(null);
    }
  }, [post.id]);

  const sendReaction = async (reactionType) => {
    try {
      const payload = {
        type: reactionType,
        postId: post.id,
        userId: TEMP_USER_ID,
      };
      console.log("Sending reaction payload:", payload);
      const response = await axios.post(endpoints.reactions, payload);
      console.log("Reaction response:", response.data);
      await Promise.all([loadReactions(), loadUserReaction()]);
    } catch (err) {
      console.error("Send reaction failed:", {
        message: err.message,
        status: err.response?.status,
        data: err.response?.data,
      });
    }
  };

  useEffect(() => {
    loadComments();
    loadReactions();
    loadUserReaction();
  }, [loadComments, loadReactions, loadUserReaction]);

  return (
    <div className="post-card">
      {/* Post Header */}
      <div className="post-header">
        <img
          src={post.user?.avatar || "https://via.placeholder.com/40"}
          alt="User avatar"
        />
        <div className="user-info">
          <h3>{post.user?.username || "Anonymous"}</h3>
          <p>{new Date(post.created_date || Date.now()).toLocaleString()}</p>
        </div>
      </div>

      {/* Post Content */}
      <div className="post-content">
        <h2>{post.title || "Untitled"}</h2>
        <p>{post.content || "No content available."}</p>
        {post.image && (
          <img src={post.image} alt="Post image" />
        )}
      </div>

      {/* Reaction Stats */}
      <div className="reaction-stats">
        <ReactionStats reactions={reactions} emojis={reactionEmojis} />
      </div>

      {/* User Reaction */}
      {userReaction && (
        <div className="user-reaction">
          Bạn đã thả {reactionEmojis[userReaction.reactionType] || userReaction.reactionType}
        </div>
      )}

      {/* Reaction Buttons */}
      <div className="reaction-buttons">
        {["LIKE", "HAHA", "LOVE"].map((type) => (
          <button
            key={type}
            onClick={() => sendReaction(type)}
            className="reaction-button"
          >
            <span className="emoji">{reactionEmojis[type]}</span>
            <span>{type}</span>
          </button>
        ))}
      </div>

      {/* Comments */}
      <div className="comment-section">
        <CommentList comments={comments} />
      </div>
    </div>
  );
};

export default PostItem;