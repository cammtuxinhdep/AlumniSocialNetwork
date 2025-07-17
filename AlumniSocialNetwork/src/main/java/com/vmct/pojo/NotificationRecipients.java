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
import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Thanh Nhat
 */
@Entity
@Table(name = "notification_recipients")
@NamedQueries({
    @NamedQuery(name = "NotificationRecipients.findAll", query = "SELECT n FROM NotificationRecipients n"),
    @NamedQuery(name = "NotificationRecipients.findById", query = "SELECT n FROM NotificationRecipients n WHERE n.id = :id"),
    @NamedQuery(name = "NotificationRecipients.findByGroupId", query = "SELECT n FROM NotificationRecipients n WHERE n.groupId = :groupId"),
    @NamedQuery(name = "NotificationRecipients.findByIsAll", query = "SELECT n FROM NotificationRecipients n WHERE n.isAll = :isAll")})
public class NotificationRecipients implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "group_id")
    private BigInteger groupId;
    @Column(name = "is_all")
    private Boolean isAll;
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Notifications notificationId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private Users userId;

    public NotificationRecipients() {
    }

    public NotificationRecipients(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getGroupId() {
        return groupId;
    }

    public void setGroupId(BigInteger groupId) {
        this.groupId = groupId;
    }

    public Boolean getIsAll() {
        return isAll;
    }

    public void setIsAll(Boolean isAll) {
        this.isAll = isAll;
    }

    public Notifications getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Notifications notificationId) {
        this.notificationId = notificationId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
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
        if (!(object instanceof NotificationRecipients)) {
            return false;
        }
        NotificationRecipients other = (NotificationRecipients) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vmct.pojo.NotificationRecipients[ id=" + id + " ]";
    }
    
}
