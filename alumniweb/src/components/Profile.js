import { useEffect, useRef, useState } from "react";
import { Button, Card } from "react-bootstrap";
import cookie from 'react-cookies';
import { authApis, endpoints } from "../configs/Apis";
import MySpinner from "./layout/MySpinner";
import { useNavigate, Link } from "react-router-dom";
import { formatTimeVi } from "../formatters/TimeFormatter";

const Profile = () => {
    const defaultAvatar = "https://res.cloudinary.com/dlnru7sj1/image/upload/v1753591841/wu5x3zqqgl7vgt4jgkxm.png";
    const [loading, setLoading] = useState(false);
    const user = cookie.load('user');
    const nav = useNavigate();
    const [posts, setPosts] = useState([]);


    const avatar = useRef();
    const cover = useRef();

    const changeAvatar = async (event) => {
        event.preventDefault();
        if (avatar.current.files.length > 0) {
            try {
                setLoading(true);

                const formData = new FormData();
                formData.append("avatar", avatar.current.files[0]);

                let res = await authApis().post(endpoints['changeAvatar'], formData);
                if (res.status === 200) {
                    alert("Cập nhật avatar thành công!");
                    cookie.save('user', JSON.stringify(res.data));
                    nav("/profile");
                }
            } catch (err) {
                console.error(err);
                alert("Lỗi khi cập nhật avatar!");
            } finally {
                setLoading(false);
            }
        }
    };

    const changeCover = async (event) => {
        event.preventDefault();

        if (cover.current.files.length > 0) {
            try {
                setLoading(true);

                const formData = new FormData();
                formData.append("cover", cover.current.files[0]);

                let res = await authApis().post(endpoints['changeCover'], formData);
                if (res.status === 200) {
                    alert("Cập nhật ảnh bìa thành công!");
                    cookie.save('user', JSON.stringify(res.data));
                    nav("/profile");
                }
            } catch (err) {
                console.error(err);
                alert("Lỗi khi cập nhật ảnh bìa!");
            } finally {
                setLoading(false);
            }
        }
    };

    const loadPosts = async () => {
        try {
            setLoading(true);
            let res = await authApis().get(`${endpoints['posts']}?userId=${user.id}`);
            setPosts(res.data);
        } catch (ex) {
            console.error(ex);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadPosts();
    }, [user.id]);

    return (
        <div style={{ backgroundColor: "#f0f4fb", color: "#0a1c3f", fontFamily: "Arial, sans-serif", minHeight: "100vh", border: "1px solid #d0d7e2", borderRadius: "12px", overflow: "hidden" }}>
            <div style={{ height: "300px", position: "relative", backgroundColor: user.cover ? "transparent" : "#cfe2f3" }}>
                {user.cover && <img src={user.cover} alt="Ảnh bìa" style={{ width: "100%", height: "100%", objectFit: "cover" }} />}
                <label style={{ position: "absolute", bottom: "20px", right: "20px", fontWeight: "bold", backgroundColor: "lightblue", padding: "6px 12px", borderRadius: "8px", cursor: "pointer", border: "1px solid #000000" }}>
                    📷 {user.cover ? "Thay đổi ảnh bìa" : "Thêm ảnh bìa"}
                    <input type="file" accept="image/*" ref={cover} style={{ display: "none" }} onChange={changeCover} />
                </label>
            </div>

            <div style={{ display: "flex", alignItems: "center", padding: "0 20px", marginTop: "-60px" }}>
                <div style={{ position: "relative", width: "10rem", height: "10rem" }}>
                    <img src={user.avatar || defaultAvatar} alt="Avatar" style={{ width: "100%", height: "100%", borderRadius: "50%", border: "5px solid white", objectFit: "cover" }} />
                    <label style={{ position: "absolute", bottom: "5px", right: "5px", backgroundColor: "lightblue", borderRadius: "50%", padding: "6px", border: "1px solid white", cursor: "pointer" }}>
                        📷
                        <input type="file" accept="image/*" ref={avatar} style={{ display: "none" }} onChange={changeAvatar} />
                    </label>
                </div>

                <div style={{ marginLeft: "20px" }}>
                    <h2 style={{ marginTop: "70px", fontSize: "24px", color: "#000000" }}>{user.lastName} {user.firstName}</h2>
                    <p style={{ color: "#000000" }}>{user.userRole === 'ROLE_ALUMNI' ? 'Cựu sinh viên' : user.userRole === 'ROLE_LECTURER' ? 'Giảng viên' : user.userRole}</p>
                </div>
            </div>

            <div style={{ display: "flex", padding: "20px", gap: "30px" }}>
                <div style={{ flex: "1", backgroundColor: "#ffffff", padding: "30px", borderRadius: "12px", border: "1px solid #d0d7e2" }}>
                    <h4 style={{ color: "#000000" }}>Thông tin tài khoản</h4>
                    <p>Họ và tên đệm: <strong>{user.lastName}</strong></p>
                    <p>Tên: <strong>{user.firstName}</strong></p>
                    <p>Mã số sinh viên: <strong>{user.studentId || "Không có mã số sinh viên"}</strong></p>
                    <p>Email: <strong>{user.email}</strong></p>
                    <Button variant="outline-dark" className="mt-3">✏️ Chỉnh sửa thông tin</Button>
                </div>

                {loading ? <MySpinner /> : (
                    <div style={{ flex: "2", display: "flex", flexDirection: "column", gap: "20px" }}>
                        {posts.length > 0 ? posts.map(post => (
                            <Card key={post.id} style={{ backgroundColor: "#ffffff", border: "1px solid #d0d7e2", borderRadius: "12px" }}>
                                <Card.Body>
                                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                        <div style={{ fontSize: "14px", color: "#6c757d", marginBottom: "8px" }}>
                                            🕒 {formatTimeVi(post.createdAt)}
                                        </div>
                                        <div style={{ display: "flex", gap: "8px" }}>
                                            <Link to={`/posts/${post.id}`} className="btn btn-outline-primary btn-sm">Xem chi tiết</Link>
                                        </div>
                                    </div>

                                    <Card.Text className="mt-2">{post.content}</Card.Text>

                                    <div style={{ display: "flex", alignItems: "center", gap: "20px", marginTop: "12px", fontSize: "14px", color: "#495057" }}>
                                        <span>👍 {post.reactionStats?.LIKE || 0}</span>
                                        <span>😂 {post.reactionStats?.HAHA || 0}</span>
                                        <span>❤️ {post.reactionStats?.HEART || 0}</span>
                                        <span>💬 {post.commentCount || 0} bình luận - {post.isCommentLocked ? "Bị khóa" : "Mở"}</span>
                                    </div>
                                </Card.Body>
                            </Card>
                        )) : <p style={{ color: "#6c757d" }}>Chưa có bài viết nào.</p>}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Profile;
