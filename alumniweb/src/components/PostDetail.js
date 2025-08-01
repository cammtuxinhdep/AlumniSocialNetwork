// // src/components/post/PostDetail.jsx
// import { useParams } from "react-router-dom";
// import { useEffect, useState } from "react";
// import Apis from "../configs/Apis";
// import ReactionStats from "./ReactionStats";

// const PostDetail = () => {
//   const { postId } = useParams();
//   const [post, setPost] = useState(null);

//   useEffect(() => {
//     Apis.getPostById(postId)
//       .then(res => setPost(res.data))
//       .catch(err => console.error("Lỗi tải chi tiết bài viết:", err));
//   }, [postId]);

//   if (!post) return <p>Đang tải...</p>;

//   return (
//     <div>
//       <h2>{post.title}</h2>
//       <p className="text-muted">Tác giả: {post.author?.fullName}</p>
//       <p>{post.content}</p>

//       <hr />
//       <CommentSection postId={post.id} locked={post.lockComment} />
//     </div>
//   );
// };

// export default PostDetail;
