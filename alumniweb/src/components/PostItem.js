import { useEffect, useState, useContext } from "react";
import { Card, Button, Image, Row, Col, OverlayTrigger, Popover } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import { formatTimeVi } from "../formatters/TimeFormatter";
import { MyUserContext } from "../configs/Context";
import CommentList from "./CommentList";
import ReactionStats from "./ReactionStats";

const PostItem = ({ post: initialPost }) => {
    const [user] = useContext(MyUserContext);
    const [post, setPost] = useState(initialPost);
    const [reactions, setReactions] = useState({});
    const [showComment, setShowComment] = useState(false);
    const [userReaction, setUserReaction] = useState(null);

    const loadReactions = async () => {
        try {
            const res = await authApis().get(endpoints.reactionStats(post.id));
            setReactions(res.data);
        } catch {}
    };

    const loadUserReaction = async () => {
        try {
            const res = await authApis().get(endpoints.userReaction(post.id));
            setUserReaction(res.data?.type);
        } catch {}
    };

    const toggleLockComment = async () => {
    try {
        const newLockState = !post.isCommentLocked;
        await authApis().post(endpoints.lockComment(post.id, newLockState));
        setPost({ ...post, isCommentLocked: newLockState });
    } catch {}
};


    useEffect(() => {
        setPost(initialPost);
        loadReactions();
        if (user) loadUserReaction();
    }, [initialPost]);

    const popover = (
        <Popover>
            <Popover.Body className="d-flex gap-2">
                <Button variant="outline-primary" onClick={() => react("LIKE")}>👍</Button>
                <Button variant="outline-warning" onClick={() => react("HAHA")}>😂</Button>
                <Button variant="outline-danger" onClick={() => react("HEART")}>❤️</Button>
            </Popover.Body>
        </Popover>
    );

    const react = async (type) => {
        try {
            await authApis().post(endpoints.reactions, {
                type,
                postId: post.id
            });
            await loadReactions();
            await loadUserReaction();
        } catch {}
    };

    const isOwner = user && post?.user?.id === user.id;

    return (
        <Card className="my-3 shadow-sm">
            <Card.Body>
                <Row className="align-items-center mb-2">
                    <Col xs="auto">
                        <Image src={post.user.avatar} roundedCircle width={40} height={40} />
                    </Col>
                    <Col>
                        <strong>{post.user.lastName} {post.user.firstName}</strong><br />
                        <small className="text-muted">{formatTimeVi(post.createdAt)}</small>
                    </Col>
                </Row>

                <Card.Text>{post.content}</Card.Text>
                <ReactionStats stats={reactions} />

                <div className="d-flex gap-2 mt-2">
                    <OverlayTrigger trigger="click" placement="top" overlay={popover} rootClose>
                        <Button variant="light">
                            {userReaction === "LIKE" && "👍"}
                            {userReaction === "HAHA" && "😂"}
                            {userReaction === "HEART" && "❤️"}
                            {!userReaction && "Thích"}
                        </Button>
                    </OverlayTrigger>

                    <Button
                    variant="light"
                    onClick={() => setShowComment(!showComment)}
                    disabled={post.isCommentLocked}
                    title={post.isCommentLocked ? "Bình luận đã bị khóa" : "Bình luận"}
                >
                    Bình luận {post.isCommentLocked && "🔒"}
                </Button>

                {isOwner && (
                    <Button
                        variant={post.isCommentLocked ? "danger" : "outline-secondary"}
                        onClick={toggleLockComment}
                        title={post.isCommentLocked ? "Mở khóa bình luận" : "Khóa bình luận"}
                    >
                        {post.isCommentLocked ? "🔓 Mở khóa bình luận" : "🔒 Khóa bình luận"}
                    </Button>
)}

                </div>

                {showComment && (
                            post.isCommentLocked ? (
                                <p className="text-danger mt-2">🚫 Chủ bài viết đã khóa bình luận.</p>
                            ) : (
                                <CommentList postId={post.id} isLocked={false} isOwner={isOwner} />
                            )
                )}

            </Card.Body>
        </Card>
    );
};

export default PostItem;
