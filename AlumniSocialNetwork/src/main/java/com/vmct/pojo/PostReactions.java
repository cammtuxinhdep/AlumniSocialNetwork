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
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "post_reactions")
@NamedQueries({
    @NamedQuery(name = "PostReactions.findAll", query = "SELECT p FROM PostReactions p"),
    @NamedQuery(name = "PostReactions.findByReactionId", query = "SELECT p FROM PostReactions p WHERE p.reactionId = :reactionId"),
    @NamedQuery(name = "PostReactions.findByReactionType", query = "SELECT p FROM PostReactions p WHERE p.reactionType = :reactionType"),
    @NamedQuery(name = "PostReactions.findByCreatedAt", query = "SELECT p FROM PostReactions p WHERE p.createdAt = :createdAt")})
public class PostReactions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "reaction_id")
    private Integer reactionId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "reaction_type")
    private String reactionType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    @ManyToOne(optional = false)
    private PostItems postId;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private UserAccounts userId;

    public PostReactions() {
    }

    public PostReactions(Integer reactionId) {
        this.reactionId = reactionId;
    }

    public PostReactions(Integer reactionId, String reactionType, Date createdAt) {
        this.reactionId = reactionId;
        this.reactionType = reactionType;
        this.createdAt = createdAt;
    }

    public Integer getReactionId() {
        return reactionId;
    }

    public void setReactionId(Integer reactionId) {
        this.reactionId = reactionId;
    }

    public String getReactionType() {
        return reactionType;
    }

    public void setReactionType(String reactionType) {
        this.reactionType = reactionType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public PostItems getPostId() {
        return postId;
    }

    public void setPostId(PostItems postId) {
        this.postId = postId;
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
        hash += (reactionId != null ? reactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PostReactions)) {
            return false;
        }
        PostReactions other = (PostReactions) object;
        if ((this.reactionId == null && other.reactionId != null) || (this.reactionId != null && !this.reactionId.equals(other.reactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vmct.pojo.PostReactions[ reactionId=" + reactionId + " ]";
    }
    
}
