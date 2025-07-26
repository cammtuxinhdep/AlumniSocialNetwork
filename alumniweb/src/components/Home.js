import React, { useEffect, useState } from "react";
import Apis from "../configs/Apis";

const dummyUser = { id: 1, fullName: "Nguyễn Văn A" }; // currentUser giả lập

const Home = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPosts = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await Apis.getAllPosts(); // gọi đúng hàm trong Apis.js
        setPosts(res.data);
      } catch (err) {
        setError("Lỗi khi tải bài viết");
      }
      setLoading(false);
    };
    fetchPosts();
  }, []);

  if (loading) return <p>Đang tải bài viết...</p>;
  if (error) return <p className="text-danger">{error}</p>;

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Bảng tin</h2>
      {posts.length === 0 && <p>Chưa có bài viết nào.</p>}

    </div>
  );
};

export default Home;
