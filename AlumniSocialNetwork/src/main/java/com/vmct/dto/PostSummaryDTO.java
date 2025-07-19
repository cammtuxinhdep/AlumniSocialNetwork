package com.vmct.dto;

import com.vmct.pojo.Post;
import java.util.Date;

public class PostSummaryDTO {
    private Long id;
    private String content;
    private Boolean isCommentLocked;
    private Date createdAt;
    private Date updatedAt;

    public PostSummaryDTO(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.isCommentLocked = post.getIsCommentLocked();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    // Getters và setters
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
}
