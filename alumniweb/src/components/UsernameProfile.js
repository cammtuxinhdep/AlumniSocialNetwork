import { useEffect, useRef, useState } from "react";
import { Button, Card } from "react-bootstrap";
import cookie from 'react-cookies';
import { authApis, endpoints } from "../configs/Apis";
import MySpinner from "./layout/MySpinner";
import { useNavigate, useParams } from "react-router-dom";
import { formatTimeVi } from "../formatters/TimeFormatter";

const UsernameProfile = () => {
    const defaultAvatar = "https://res.cloudinary.com/dlnru7sj1/image/upload/v1753591841/wu5x3zqqgl7vgt4jgkxm.png";

    const { username } = useParams();
    const [loading, setLoading] = useState(false);
    const [profileUser, setProfileUser] = useState(null);
    const [posts, setPosts] = useState([]);
    const nav = useNavigate();

    const currentUser = cookie.load('user');

    useEffect(() => {
        if (username === currentUser.username) {
            nav("/profile");
        }
    }, []);

    const loadProfileUser = async () => {
        try {
            setLoading(true);
            const res = await authApis().get(endpoints.profile(username));
            setProfileUser(res.data);
        } catch (err) {
            console.error("Lỗi khi tải user theo username:", err);
        } finally {
            setLoading(false);
        }
    };

    const loadPosts = async (userId) => {
        try {
            const res = await authApis().get(`${endpoints.posts}?userId=${userId}`);
            setPosts(res.data);
        } catch (ex) {
            console.error("Lỗi khi tải bài viết:", ex);
        }
    };

    useEffect(() => {
        loadProfileUser();
    }, [username]);

    useEffect(() => {
        if (profileUser) {
            loadPosts(profileUser.id);
        }
    }, [profileUser]);

    if (loading || !profileUser) return <MySpinner />;

    return (
        <div style={{
            backgroundColor: "#f0f4fb", color: "#0a1c3f", fontFamily: "Arial, sans-serif", minHeight: "100vh",
            border: "1px solid #d0d7e2", borderRadius: "12px", overflow: "hidden"
        }}>

            <div style={{ height: "300px", position: "relative", backgroundColor: profileUser.cover ? "transparent" : "#cfe2f3" }}>
                {profileUser.cover && <img src={profileUser.cover} alt="Ảnh bìa" style={{ width: "100%", height: "100%", objectFit: "cover" }} />}
                {profileUser.username !== currentUser?.username && null}
            </div>

            <div style={{ display: "flex", alignItems: "center", padding: "0 20px", marginTop: "-60px" }}>
                <div style={{ position: "relative", width: "10rem", height: "10rem" }}>
                    <img src={profileUser.avatar || defaultAvatar} alt="Avatar"
                        style={{ width: "100%", height: "100%", borderRadius: "50%", border: "5px solid white", objectFit: "cover" }} />
                    {profileUser.username !== currentUser?.username && null}
                </div>

                <div style={{ marginLeft: "20px", flex: 1 }}>
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                        <h2 style={{ marginTop: "70px", fontSize: "24px", color: "#000000" }}>
                            {profileUser.lastName} {profileUser.firstName}
                        </h2>

                        {profileUser.id !== currentUser?.id && (
                            <Button variant="primary" style={{ marginTop: "70px" }}
                                onClick={() => nav(`/chat/${profileUser.id}`)}>💬 Nhắn tin
                            </Button>
                        )}
                    </div>

                    <p style={{ color: "#000000" }}>
                        {profileUser.userRole === 'ROLE_ALUMNI' ? 'Cựu sinh viên' :
                            profileUser.userRole === 'ROLE_LECTURER' ? 'Giảng viên' :
                                profileUser.userRole}
                    </p>
                </div>
            </div>

            <div style={{ display: "flex", padding: "20px", gap: "30px" }}>
                <div style={{ flex: "1", backgroundColor: "#ffffff", padding: "30px", borderRadius: "12px", border: "1px solid #d0d7e2" }}>
                    <h4 style={{ color: "#000000" }}>Thông tin tài khoản</h4>
                    <p>Họ và tên đệm: <strong>{profileUser.lastName}</strong></p>
                    <p>Tên: <strong>{profileUser.firstName}</strong></p>
                    <p>Mã số sinh viên: <strong>{profileUser.studentId || "Không có mã số sinh viên"}</strong></p>
                    <p>Email: <strong>{profileUser.email}</strong></p>
                </div>

                <div style={{ flex: "2", display: "flex", flexDirection: "column", gap: "20px" }}>
                    {posts.length > 0 ? posts.map(post => (
                        <Card key={post.id} style={{ backgroundColor: "#ffffff", border: "1px solid #d0d7e2", borderRadius: "12px" }}>
                            <Card.Body>
                                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                    <div style={{ fontSize: "14px", color: "#6c757d", marginBottom: "8px" }}>
                                        🕒 {formatTimeVi(post.createdAt)}
                                    </div>
                                    <div style={{ display: "flex", gap: "8px" }}>
                                        <Button variant="outline-dark" size="sm">Xem chi tiết</Button>
                                    </div>
                                </div>

                                <Card.Text className="mt-2">{post.content}</Card.Text>

                                <div style={{
                                    display: "flex", alignItems: "center", gap: "20px",
                                    marginTop: "12px", fontSize: "14px", color: "#495057"
                                }}>
                                    <span>👍 {post.reactionStats?.LIKE || 0}</span>
                                    <span>😂 {post.reactionStats?.HAHA || 0}</span>
                                    <span>❤️ {post.reactionStats?.HEART || 0}</span>
                                    <span>💬 {post.commentCount || 0} bình luận{" - "}
                                        {post.isCommentLocked ? "Bài viết bị khóa bình luận" : "Bình luận mở"}
                                    </span>
                                </div>
                            </Card.Body>
                        </Card>
                    )) : <p style={{ color: "#6c757d" }}>Chưa có bài viết nào.</p>}
                </div>
            </div>
        </div>
    );
};

export default UsernameProfile;
