package com.vmct.dto;
import java.util.Date;
import java.util.List;

public class SurveyDetailDTO {

    private Long id;
    private String title;
    private String description;
    private Date createdAt;
    private List<SurveyOptionDTO> options;
    private List<SurveyResponseDTO> responses;

    public SurveyDetailDTO() {
    }

    // getters và setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<SurveyOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<SurveyOptionDTO> options) {
        this.options = options;
    }

    public List<SurveyResponseDTO> getResponses() {
        return responses;
    }

    public void setResponses(List<SurveyResponseDTO> responses) {
        this.responses = responses;
    }
}
