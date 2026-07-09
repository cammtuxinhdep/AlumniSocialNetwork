package com.vmct.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReactionDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 5)
    private String type;

    @NotNull
    private Long postId;

    @NotNull
    private Long userId;

    public ReactionDTO() {
    }

    public ReactionDTO(com.vmct.pojo.Reaction reaction) {
        this.id = reaction.getId();
        this.type = reaction.getType();
        this.postId = reaction.getPostId() != null ? reaction.getPostId().getId() : null;
        this.userId = reaction.getUserId() != null ? reaction.getUserId().getId() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
