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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
 * @author HP
 */
@Entity
@Table(name = "post_items")
@NamedQueries({
    @NamedQuery(name = "PostItems.findAll", query = "SELECT p FROM PostItems p"),
    @NamedQuery(name = "PostItems.findByPostId", query = "SELECT p FROM PostItems p WHERE p.postId = :postId"),
    @NamedQuery(name = "PostItems.findByType", query = "SELECT p FROM PostItems p WHERE p.type = :type"),
    @NamedQuery(name = "PostItems.findByIsCommentLocked", query = "SELECT p FROM PostItems p WHERE p.isCommentLocked = :isCommentLocked"),
    @NamedQuery(name = "PostItems.findByCreatedAt", query = "SELECT p FROM PostItems p WHERE p.createdAt = :createdAt"),
    @NamedQuery(name = "PostItems.findByUpdatedAt", query = "SELECT p FROM PostItems p WHERE p.updatedAt = :updatedAt")})
public class PostItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "post_id")
    private Integer postId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "content")
    private String content;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "type")
    private String type;
    @Column(name = "is_comment_locked")
    private Boolean isCommentLocked;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private UserAccounts userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postId")
    private Set<EventAnnouncements> eventAnnouncementsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postId")
    private Set<PostMedia> postMediaSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postId")
    private Set<SurveyOptions> surveyOptionsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postId")
    private Set<PostComments> postCommentsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postId")
    private Set<PostReactions> postReactionsSet;

    public PostItems() {
    }

    public PostItems(Integer postId) {
        this.postId = postId;
    }

    public PostItems(Integer postId, String content, String type, Date createdAt) {
        this.postId = postId;
        this.content = content;
        this.type = type;
        this.createdAt = createdAt;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsCommentLocked() {
        return isCommentLocked;
    }

    public void setIsCommentLocked(Boolean isCommentLocked) {
        this.isCommentLocked = isCommentLocked;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserAccounts getUserId() {
        return userId;
    }

    public void setUserId(UserAccounts userId) {
        this.userId = userId;
    }

    public Set<EventAnnouncements> getEventAnnouncementsSet() {
        return eventAnnouncementsSet;
    }

    public void setEventAnnouncementsSet(Set<EventAnnouncements> eventAnnouncementsSet) {
        this.eventAnnouncementsSet = eventAnnouncementsSet;
    }

    public Set<PostMedia> getPostMediaSet() {
        return postMediaSet;
    }

    public void setPostMediaSet(Set<PostMedia> postMediaSet) {
        this.postMediaSet = postMediaSet;
    }

    public Set<SurveyOptions> getSurveyOptionsSet() {
        return surveyOptionsSet;
    }

    public void setSurveyOptionsSet(Set<SurveyOptions> surveyOptionsSet) {
        this.surveyOptionsSet = surveyOptionsSet;
    }

    public Set<PostComments> getPostCommentsSet() {
        return postCommentsSet;
    }

    public void setPostCommentsSet(Set<PostComments> postCommentsSet) {
        this.postCommentsSet = postCommentsSet;
    }

    public Set<PostReactions> getPostReactionsSet() {
        return postReactionsSet;
    }

    public void setPostReactionsSet(Set<PostReactions> postReactionsSet) {
        this.postReactionsSet = postReactionsSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (postId != null ? postId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PostItems)) {
            return false;
        }
        PostItems other = (PostItems) object;
        if ((this.postId == null && other.postId != null) || (this.postId != null && !this.postId.equals(other.postId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vmct.pojo.PostItems[ postId=" + postId + " ]";
    }
    
}
