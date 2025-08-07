package com.vmct.dto;

import com.vmct.pojo.Post;
import org.hibernate.Hibernate;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PostDTO {

    private Long id;
    private String content;
    private Boolean isCommentLocked;
    private Date createdAt;
    private Date updatedAt;

    private UserDTO user;
    private List<CommentDTO> comments;

    private Map<String, Integer> reactionStats;
    private String currentUserReaction;
    private int commentCount;
    private boolean isOwner;


    public PostDTO(Post post,
            List<CommentDTO> comments,
            Map<String, Integer> reactionStats,
            String currentUserReaction,
            int commentCount,
            boolean isOwner) {
        this.id = post.getId();
        this.content = post.getContent();
        this.isCommentLocked = post.getIsCommentLocked();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

        this.user = (post.getUserId() != null && Hibernate.isInitialized(post.getUserId()))
                ? new UserDTO(post.getUserId())
                : null;

        this.comments = comments != null ? comments : Collections.emptyList();
        this.reactionStats = reactionStats;
        this.currentUserReaction = currentUserReaction;
        this.commentCount = commentCount;
     this.isOwner=isOwner;
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

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public Map<String, Integer> getReactionStats() {
        return reactionStats;
    }

    public void setReactionStats(Map<String, Integer> reactionStats) {
        this.reactionStats = reactionStats;
    }

    public String getCurrentUserReaction() {
        return currentUserReaction;
    }

    public void setCurrentUserReaction(String currentUserReaction) {
        this.currentUserReaction = currentUserReaction;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
