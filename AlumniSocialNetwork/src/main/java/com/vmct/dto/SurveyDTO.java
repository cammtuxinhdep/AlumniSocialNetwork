package com.vmct.dto;

import java.time.LocalDate;

public class SurveyDTO {

    private Long id;
    private String title;
    private LocalDate createdAt;
    private int optionCount;

    public SurveyDTO(Long id, String title, LocalDate createdAt, int optionCount) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.optionCount = optionCount;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public int getOptionCount() {
        return optionCount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setOptionCount(int optionCount) {
        this.optionCount = optionCount;
    }
}
