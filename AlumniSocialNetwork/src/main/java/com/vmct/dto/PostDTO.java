package com.vmct.dto;

import com.vmct.pojo.Post;
import org.hibernate.Hibernate;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PostDTO {
    private Long id;
    private String content;
    private Boolean isCommentLocked;
    private Date createdAt;
    private Date updatedAt;
    private UserDTO user;
    private List<CommentDTO> comments;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.isCommentLocked = post.getIsCommentLocked();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.user = (post.getUserId() != null && Hibernate.isInitialized(post.getUserId())) 
                ? new UserDTO(post.getUserId()) 
                : null;
        this.comments = (post.getCommentSet() != null && Hibernate.isInitialized(post.getCommentSet()))
                ? post.getCommentSet().stream()
                      .filter(c -> c.getParentId() == null) // Chỉ lấy bình luận cấp 1
                      .map(CommentDTO::new)
                      .collect(Collectors.toList())
                : Collections.emptyList();
    }

    // Getters và setters (giữ nguyên như mã hiện tại)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Boolean getIsCommentLocked() { return isCommentLocked; }
    public void setIsCommentLocked(Boolean isCommentLocked) { this.isCommentLocked = isCommentLocked; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
    public List<CommentDTO> getComments() { return comments; }
    public void setComments(List<CommentDTO> comments) { this.comments = comments; }
}