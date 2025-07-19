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
import java.util.Collection;

/**
 *
 * @author Thanh Nhat
 */
@Entity
@Table(name = "survey_option")
@NamedQueries({
    @NamedQuery(name = "SurveyOption.findAll", query = "SELECT s FROM SurveyOption s"),
    @NamedQuery(name = "SurveyOption.findById", query = "SELECT s FROM SurveyOption s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyOption.findByOptionText", query = "SELECT s FROM SurveyOption s WHERE s.optionText = :optionText")})
public class SurveyOption implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "option_text")
    private String optionText;
    @JoinColumn(name = "survey_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Survey surveyId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "optionId")
    private Collection<SurveyResponses> surveyResponsesCollection;

    public SurveyOption() {
    }

    public SurveyOption(Long id) {
        this.id = id;
    }

    public SurveyOption(Long id, String optionText) {
        this.id = id;
        this.optionText = optionText;
    }

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

    public Survey getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Survey surveyId) {
        this.surveyId = surveyId;
    }

    public Collection<SurveyResponses> getSurveyResponsesCollection() {
        return surveyResponsesCollection;
    }

    public void setSurveyResponsesCollection(Collection<SurveyResponses> surveyResponsesCollection) {
        this.surveyResponsesCollection = surveyResponsesCollection;
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
        if (!(object instanceof SurveyOption)) {
            return false;
        }
        SurveyOption other = (SurveyOption) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vmct.pojo.SurveyOption[ id=" + id + " ]";
    }
    
}
