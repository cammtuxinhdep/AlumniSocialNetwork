package com.vmct.dto;

import com.vmct.pojo.Post;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Map;

public class PostSummaryDTO {

    private Long id;
    private String content;
    private Boolean isCommentLocked;
    private Date createdAt;
    private Date updatedAt;

    private UserDTO user;
    private int commentCount;
    private Map<String, Integer> reactionStats;

    public PostSummaryDTO(Post post, int commentCount, Map<String, Integer> reactionStats) {
        this.id = post.getId();
        this.content = post.getContent();
        this.isCommentLocked = post.getIsCommentLocked();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

        if (post.getUserId() != null && Hibernate.isInitialized(post.getUserId())) {
            this.user = new UserDTO(post.getUserId());
        }

        this.commentCount = commentCount;
        this.reactionStats = reactionStats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsCommentLocked() {
        return isCommentLocked;
    }

    public void setIsCommentLocked(Boolean isCommentLocked) {
        this.isCommentLocked = isCommentLocked;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public Map<String, Integer> getReactionStats() {
        return reactionStats;
    }

    public void setReactionStats(Map<String, Integer> reactionStats) {
        this.reactionStats = reactionStats;
    }


}
