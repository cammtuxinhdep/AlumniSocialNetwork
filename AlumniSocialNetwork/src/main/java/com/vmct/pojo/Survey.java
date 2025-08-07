/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author Thanh Nhat
 */
@Entity
@Table(name = "survey")
@NamedQueries({
    @NamedQuery(name = "Survey.findAll", query = "SELECT s FROM Survey s"),
    @NamedQuery(name = "Survey.findById", query = "SELECT s FROM Survey s WHERE s.id = :id"),
    @NamedQuery(name = "Survey.findByTitle", query = "SELECT s FROM Survey s WHERE s.title = :title"),
    @NamedQuery(name = "Survey.findByCreatedAt", query = "SELECT s FROM Survey s WHERE s.createdAt = :createdAt")})
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyId")
    @JsonIgnore
    private Set<SurveyResponse> surveyResponseSet;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "surveyId")
    private Set<SurveyOption> surveyOptionSet;

    public Survey() {
    }

    public Survey(Long id) {
        this.id = id;
    }

    public Survey(Long id, String title) {
        this.id = id;
        this.title = title;
    }

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

    public Set<SurveyResponse> getSurveyResponseSet() {
        return surveyResponseSet;
    }

    public void setSurveyResponseSet(Set<SurveyResponse> surveyResponseSet) {
        this.surveyResponseSet = surveyResponseSet;
    }

    public Set<SurveyOption> getSurveyOptionSet() {
        return surveyOptionSet;
    }

    public void setSurveyOptionSet(Set<SurveyOption> surveyOptionSet) {
        this.surveyOptionSet = surveyOptionSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Survey)) {
            return false;
        }
        Survey other = (Survey) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vmct.pojo.Survey[ id=" + id + " ]";
    }
    
}
