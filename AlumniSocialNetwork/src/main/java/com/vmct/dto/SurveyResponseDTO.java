package com.vmct.dto;

import java.util.Date;

public class SurveyResponseDTO {

    private Long id;
    private Long userId;
    private String userName;
    private Long optionId;
    private String optionText;
    private Date createdAt;

    public SurveyResponseDTO() {
    }

    public SurveyResponseDTO(Long id, Long userId, String userName, Long optionId, String optionText, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.optionId = optionId;
        this.optionText = optionText;
        this.createdAt = createdAt;
    }

    // getters và setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
