/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "survey_votes")
@NamedQueries({
    @NamedQuery(name = "SurveyVotes.findAll", query = "SELECT s FROM SurveyVotes s"),
    @NamedQuery(name = "SurveyVotes.findByVoteId", query = "SELECT s FROM SurveyVotes s WHERE s.voteId = :voteId"),
    @NamedQuery(name = "SurveyVotes.findByCreatedAt", query = "SELECT s FROM SurveyVotes s WHERE s.createdAt = :createdAt")})
public class SurveyVotes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "vote_id")
    private Integer voteId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "option_id", referencedColumnName = "option_id")
    @ManyToOne(optional = false)
    private SurveyOptions optionId;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private UserAccounts userId;

    public SurveyVotes() {
    }

    public SurveyVotes(Integer voteId) {
        this.voteId = voteId;
    }

    public SurveyVotes(Integer voteId, Date createdAt) {
        this.voteId = voteId;
        this.createdAt = createdAt;
    }

    public Integer getVoteId() {
        return voteId;
    }

    public void setVoteId(Integer voteId) {
        this.voteId = voteId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public SurveyOptions getOptionId() {
        return optionId;
    }

    public void setOptionId(SurveyOptions optionId) {
        this.optionId = optionId;
    }

    public UserAccounts getUserId() {
        return userId;
    }

    public void setUserId(UserAccounts userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (voteId != null ? voteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SurveyVotes)) {
            return false;
        }
        SurveyVotes other = (SurveyVotes) object;
        if ((this.voteId == null && other.voteId != null) || (this.voteId != null && !this.voteId.equals(other.voteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vmct.pojo.SurveyVotes[ voteId=" + voteId + " ]";
    }
    
}
