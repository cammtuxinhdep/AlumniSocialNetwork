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
 * @author HP
 */
@Entity
@Table(name = "user_accounts")
@NamedQueries({
    @NamedQuery(name = "UserAccounts.findAll", query = "SELECT u FROM UserAccounts u"),
    @NamedQuery(name = "UserAccounts.findByUserId", query = "SELECT u FROM UserAccounts u WHERE u.userId = :userId"),
    @NamedQuery(name = "UserAccounts.findByEmail", query = "SELECT u FROM UserAccounts u WHERE u.email = :email"),
    @NamedQuery(name = "UserAccounts.findByPassword", query = "SELECT u FROM UserAccounts u WHERE u.password = :password"),
    @NamedQuery(name = "UserAccounts.findByRole", query = "SELECT u FROM UserAccounts u WHERE u.role = :role"),
    @NamedQuery(name = "UserAccounts.findByStudentId", query = "SELECT u FROM UserAccounts u WHERE u.studentId = :studentId"),
    @NamedQuery(name = "UserAccounts.findByAvatar", query = "SELECT u FROM UserAccounts u WHERE u.avatar = :avatar"),
    @NamedQuery(name = "UserAccounts.findByCoverPhoto", query = "SELECT u FROM UserAccounts u WHERE u.coverPhoto = :coverPhoto"),
    @NamedQuery(name = "UserAccounts.findByDisplayName", query = "SELECT u FROM UserAccounts u WHERE u.displayName = :displayName"),
    @NamedQuery(name = "UserAccounts.findByCreatedAt", query = "SELECT u FROM UserAccounts u WHERE u.createdAt = :createdAt"),
    @NamedQuery(name = "UserAccounts.findByIsVerified", query = "SELECT u FROM UserAccounts u WHERE u.isVerified = :isVerified"),
    @NamedQuery(name = "UserAccounts.findByPasswordChangedAt", query = "SELECT u FROM UserAccounts u WHERE u.passwordChangedAt = :passwordChangedAt"),
    @NamedQuery(name = "UserAccounts.findByIsLocked", query = "SELECT u FROM UserAccounts u WHERE u.isLocked = :isLocked"),
    @NamedQuery(name = "UserAccounts.findByLastLogin", query = "SELECT u FROM UserAccounts u WHERE u.lastLogin = :lastLogin")})
public class UserAccounts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "role")
    private String role;
    @Size(max = 50)
    @Column(name = "student_id")
    private String studentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "avatar")
    private String avatar;
    @Size(max = 255)
    @Column(name = "cover_photo")
    private String coverPhoto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "display_name")
    private String displayName;
    @Lob
    @Size(max = 65535)
    @Column(name = "bio")
    private String bio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "is_verified")
    private Boolean isVerified;
    @Column(name = "password_changed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordChangedAt;
    @Column(name = "is_locked")
    private Boolean isLocked;
    @Column(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<PostItems> postItemsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<PostComments> postCommentsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<PostReactions> postReactionsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<SurveyVotes> surveyVotesSet;

    public UserAccounts() {
    }

    public UserAccounts(Integer userId) {
        this.userId = userId;
    }

    public UserAccounts(Integer userId, String email, String password, String role, String avatar, String displayName, Date createdAt) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.avatar = avatar;
        this.displayName = displayName;
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Date getPasswordChangedAt() {
        return passwordChangedAt;
    }

    public void setPasswordChangedAt(Date passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Set<PostItems> getPostItemsSet() {
        return postItemsSet;
    }

    public void setPostItemsSet(Set<PostItems> postItemsSet) {
        this.postItemsSet = postItemsSet;
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

    public Set<SurveyVotes> getSurveyVotesSet() {
        return surveyVotesSet;
    }

    public void setSurveyVotesSet(Set<SurveyVotes> surveyVotesSet) {
        this.surveyVotesSet = surveyVotesSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserAccounts)) {
            return false;
        }
        UserAccounts other = (UserAccounts) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vmct.pojo.UserAccounts[ userId=" + userId + " ]";
    }
    
}
