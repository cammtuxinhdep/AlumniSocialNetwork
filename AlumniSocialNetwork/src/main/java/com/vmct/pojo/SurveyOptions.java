/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "survey_options")
@NamedQueries({
    @NamedQuery(name = "SurveyOptions.findAll", query = "SELECT s FROM SurveyOptions s"),
    @NamedQuery(name = "SurveyOptions.findByOptionId", query = "SELECT s FROM SurveyOptions s WHERE s.optionId = :optionId"),
    @NamedQuery(name = "SurveyOptions.findByOptionText", query = "SELECT s FROM SurveyOptions s WHERE s.optionText = :optionText"),
    @NamedQuery(name = "SurveyOptions.findByVoteCount", query = "SELECT s FROM SurveyOptions s WHERE s.voteCount = :voteCount")})
public class SurveyOptions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "option_id")
    private Integer optionId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "option_text")
    private String optionText;
    @Column(name = "vote_count")
    private Integer voteCount;
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    @ManyToOne(optional = false)
    private PostItems postId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "optionId")
    private Set<SurveyVotes> surveyVotesSet;

    public SurveyOptions() {
    }

    public SurveyOptions(Integer optionId) {
        this.optionId = optionId;
    }

    public SurveyOptions(Integer optionId, String optionText) {
        this.optionId = optionId;
        this.optionText = optionText;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public PostItems getPostId() {
        return postId;
    }

    public void setPostId(PostItems postId) {
        this.postId = postId;
    }

    public Set<SurveyVotes> getSurveyVotesSet() {
        return surveyVotesSet;
    }

    public void setSurveyVotesSet(Set<SurveyVotes> surveyVotesSet) {
        this.surveyVotesSet = surveyVotesSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (optionId != null ? optionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SurveyOptions)) {
            return false;
        }
        SurveyOptions other = (SurveyOptions) object;
        if ((this.optionId == null && other.optionId != null) || (this.optionId != null && !this.optionId.equals(other.optionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vmct.pojo.SurveyOptions[ optionId=" + optionId + " ]";
    }
    
}
