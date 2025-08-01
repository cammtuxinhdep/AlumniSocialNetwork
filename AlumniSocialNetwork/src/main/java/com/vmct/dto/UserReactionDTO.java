package com.vmct.dto;

public class UserReactionDTO {

    private Long userId;
    private String username;
    private String avatar;
    private String reactionType;

    public UserReactionDTO(Long userId, String username, String avatar, String reactionType) {
        this.userId = userId;
        this.username = username;
        this.avatar = avatar;
        this.reactionType = reactionType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }
}
