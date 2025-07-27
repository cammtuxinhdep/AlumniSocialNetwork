package com.vmct.dto;

import com.vmct.pojo.Comment;
import java.util.Date;
import java.util.List;

public class CommentDTO {
    private Long id;
    private String content;
    private Date createdAt;
    private UserDTO user;
    private List<CommentDTO> replies;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();

        try {
            if (comment.getUserId() != null)
                this.user = new UserDTO(comment.getUserId());
        } catch (Exception e) {
            e.printStackTrace(); // log nếu có lỗi lazy
            this.user = null;
        }
    }

    public CommentDTO(Comment comment, List<CommentDTO> replies) {
        this(comment);
        this.replies = replies;
    }

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public List<CommentDTO> getReplies() { return replies; }
    public void setReplies(List<CommentDTO> replies) { this.replies = replies; }
}

