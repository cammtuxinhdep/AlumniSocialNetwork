import { useEffect, useState } from "react";
import { Button, Card } from "react-bootstrap";
import cookie from 'react-cookies';
import Apis, { endpoints } from "../configs/Apis";

const Profile = () => {
    const defaultAvatar = "https://res.cloudinary.com/dlnru7sj1/image/upload/v1753591841/wu5x3zqqgl7vgt4jgkxm.png";

    const user = cookie.load('user');

    const [posts, setPosts] = useState(null);
    const [loading, setLoading] = useState(false);

    const loadPosts = async () => {
        let url = `${endpoints['userPosts']}?id=${user.data.id}`;

        try {
            setLoading(true);

            let res = await Apis.get(url);
            setPosts(res.data);
        } catch (ex) {
            console.error(ex);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadPosts();
    },[]);

    return (
        <div style={{
            backgroundColor: "#f0f4fb", color: "#0a1c3f", fontFamily: "Arial, sans-serif", minHeight: "100vh",
            border: "1px solid #d0d7e2", borderRadius: "12px", overflow: "hidden"
        }}>

            <div style={{ height: "300px", position: "relative", backgroundColor: user.data.cover ? "transparent" : "#cfe2f3" }}>
                <img src={ user.data.cover || null} alt="Ảnh bìa" style={{ width: "100%", height: "100%", objectFit: "cover" }} />
                <Button variant="outline-dark" style={{ position: "absolute", bottom: "20px", right: "20px", fontWeight: "bold", backgroundColor: "lightblue" }}>
                    📷 Thêm ảnh bìa
                </Button>
            </div>

            <div style={{ display: "flex", alignItems: "flex-end", padding: "0 20px", marginTop: "-80px" }}>
                <div style={{ position: "relative" }}>
                    <img src={user.data.avatar || defaultAvatar} alt="Avatar" style={{ width: "10rem", height: "10rem", borderRadius: "50%", border: "5px solid white", objectFit: "cover" }} />
                    <Button style={{ position: "absolute", bottom: "5px", right: "10px", backgroundColor: "lightblue", borderRadius: "50%", padding: "6px", border: "1px solid white" }}>
                        📷
                    </Button>
                </div>

                <div style={{ marginLeft: "20px" }}>
                    <h2 style={{ marginTop: "100px", fontSize: "24px", color: "#000000" }}>{user.data.lastName} {user.data.firstName}</h2>
                    <p style={{ color: "#000000" }}>536 người bạn</p>
                </div>
            </div>

            <div style={{ display: "flex", padding: "20px", gap: "30px" }}>
                <div style={{ flex: "1", backgroundColor: "#ffffff", padding: "30px", borderRadius: "12px", border: "1px solid #d0d7e2" }}>
                    <h4 style={{ color: "#000000" }}>Thông tin tài khoản</h4>
                    <p>Họ và tên đệm: <strong>{user.data.lastName}</strong></p>
                    <p>Tên: <strong>{user.data.firstName}</strong></p>
                    <p>Mã số sinh viên: <strong>{user.data.studentId}</strong></p>
                    <p>Email: <strong>{user.data.email}</strong></p>
                    <p>Loại tài khoản: <strong>{user.data.userRole}</strong></p>
                    <Button variant="outline-dark" className="mt-3">✏️ Chỉnh sửa thông tin</Button>
                </div>

                <div style={{ flex: "2", display: "flex", flexDirection: "column", gap: "20px" }}>
                    {posts.map(post =>
                        <Card key={post.id} style={{ backgroundColor: "#ffffff", border: "1px solid #d0d7e2", borderRadius: "12px" }}>
                            <Card.Body>
                                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                    <div style={{ fontSize: "14px", color: "#6c757d", marginBottom: "8px" }}>
                                        🕒 {post.date}
                                    </div>
                                    <div style={{ display: "flex", gap: "8px" }}>
                                        <Button variant="outline-dark" size="sm">✏️ Sửa</Button>
                                        <Button variant="outline-dark" size="sm">🗑️ Xoá</Button>
                                    </div>
                                </div>

                                <Card.Title style={{ fontSize: "20px", fontWeight: "bold", marginBottom: "0", color: "#000000" }}>
                                    {post.title}
                                </Card.Title>

                                <Card.Text className="mt-2">{post.content}</Card.Text>

                                <div style={{ display: "flex", alignItems: "center", gap: "20px", marginTop: "12px", fontSize: "14px", color: "#495057" }}>
                                    <span>👍 {post.reactions.like}</span>
                                    <span>😂 {post.reactions.haha}</span>
                                    <span>❤️ {post.reactions.love}</span>
                                    <span>💬 {post.comments} bình luận</span>
                                </div>
                            </Card.Body>
                        </Card>
                    )}
                </div>
            </div>
        </div>
    );
}
export default Profile;