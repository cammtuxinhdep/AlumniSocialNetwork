import { useState, useEffect, useContext } from "react";
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/Context";
import { Form, Button, Row, Col, Image } from "react-bootstrap";
import { formatTimeVi } from "../formatters/TimeFormatter";

const CommentList = ({ postId, isLocked, isOwner }) => {
    const [user] = useContext(MyUserContext);
    const [comments, setComments] = useState([]);
    const [content, setContent] = useState("");
    const [editingCommentId, setEditingCommentId] = useState(null);
    const [editingContent, setEditingContent] = useState("");

    const loadComments = async () => {
        try {
            const res = await authApis().get(endpoints.commentByPost(postId));
            setComments(res.data);
        } catch (err) {
            console.error("Lỗi tải bình luận:", err);
        }
    };

    const addComment = async (e) => {
        e.preventDefault();
        try {
            await authApis().post(endpoints.comments, {
    content,
    postId: { id: postId }   
});

            setContent("");
            loadComments();
        } catch (err) {
            console.error("Lỗi thêm bình luận:", err);
        }
    };

    const deleteComment = async (commentId) => {
        try {
            await authApis().delete(endpoints.deleteComment(commentId));
            loadComments();
        } catch (err) {
            console.error("Lỗi xoá bình luận:", err);
        }
    };

    const startEdit = (comment) => {
        setEditingCommentId(comment.id);
        setEditingContent(comment.content);
    };

    const cancelEdit = () => {
        setEditingCommentId(null);
        setEditingContent("");
    };

    const submitEdit = async (e) => {
        e.preventDefault();
        try {
           await authApis().put(endpoints.updateComment(editingCommentId), {
                content: editingContent,
                postId: { id: postId } 
            });
            setEditingCommentId(null);
            setEditingContent("");
            loadComments();
        } catch (err) {
            console.error("Lỗi cập nhật bình luận:", err);
        }
    };

    useEffect(() => {
        loadComments();
    }, [postId]);

    return (
        <>
            {!isLocked && user &&
                <Form onSubmit={addComment} className="my-2">
                    <Form.Group className="d-flex align-items-center gap-2">
                        <Image src={user.avatar} roundedCircle width={36} height={36} />
                        <Form.Control
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                            placeholder="Viết bình luận..."
                            required
                        />
                        <Button type="submit" variant="primary">Gửi</Button>
                    </Form.Group>
                </Form>
            }

            {isLocked && <p className="text-muted"><i>Bình luận đã bị khóa cho bài viết này.</i></p>}

            {comments.map(c => (
                <Row key={c.id} className="my-2">
                    <Col xs="auto">
                        <Image src={c.user.avatar} roundedCircle width={36} height={36} />
                    </Col>
                    <Col>
                        <div className="bg-light p-2 rounded">
                            <strong>{c.user.lastName} {c.user.firstName}</strong>
                            
                            {editingCommentId === c.id ? (
                                <Form onSubmit={submitEdit} className="my-1">
                                    <Form.Control
                                        value={editingContent}
                                        onChange={(e) => setEditingContent(e.target.value)}
                                        required
                                    />
                                    <div className="mt-1 d-flex gap-2">
                                        <Button size="sm" type="submit" variant="success">Lưu</Button>
                                        <Button size="sm" variant="secondary" onClick={cancelEdit}>Huỷ</Button>
                                    </div>
                                </Form>
                            ) : (
                                <p className="mb-1">{c.content}</p>
                            )}

                            <small className="text-muted">{formatTimeVi(c.createdAt)}</small>

                            {user && (user.id === c.user.id || isOwner) && (
                                  <div className="mt-1 d-flex gap-2">
                                      {user.id === c.user.id && (
                                          <Button size="sm" variant="outline-primary" onClick={() => startEdit(c)}>
                                              Sửa
                                          </Button>
                                      )}
                                      <Button size="sm" variant="outline-danger" onClick={() => deleteComment(c.id)}>
                                          Xoá
                                      </Button>
                                  </div>
)}

                        </div>
                    </Col>
                </Row>
            ))}
        </>
    );
};

export default CommentList;
