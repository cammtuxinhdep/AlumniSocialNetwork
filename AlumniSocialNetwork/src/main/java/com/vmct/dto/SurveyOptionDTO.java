package com.vmct.dto;

public class SurveyOptionDTO {

    private Long id;
    private String optionText;
    private int voteCount;

    public SurveyOptionDTO() {
    }

    public SurveyOptionDTO(Long id, String optionText, int voteCount) {
        this.id = id;
        this.optionText = optionText;
        this.voteCount = voteCount;
    }

    // getters và setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
