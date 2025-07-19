package com.vmct.dto;

import com.vmct.pojo.Comments;
import org.hibernate.Hibernate;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDTO {
    private Long id;
    private String content;
    private Date createdAt;
    private UserDTO user;
    private List<CommentDTO> replies;

    public CommentDTO(Comments comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.user = (comment.getUserId() != null && Hibernate.isInitialized(comment.getUserId())) 
                ? new UserDTO(comment.getUserId()) 
                : null;
        this.replies = (comment.getCommentsCollection() != null && Hibernate.isInitialized(comment.getCommentsCollection()))
                ? comment.getCommentsCollection().stream()
                         .map(CommentDTO::new)
                         .collect(Collectors.toList())
                : Collections.emptyList();
    }

    // Getters và setters (giữ nguyên như mã hiện tại)
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