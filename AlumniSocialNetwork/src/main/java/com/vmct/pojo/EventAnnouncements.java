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
@Table(name = "event_announcements")
@NamedQueries({
    @NamedQuery(name = "EventAnnouncements.findAll", query = "SELECT e FROM EventAnnouncements e"),
    @NamedQuery(name = "EventAnnouncements.findByAnnouncementId", query = "SELECT e FROM EventAnnouncements e WHERE e.announcementId = :announcementId"),
    @NamedQuery(name = "EventAnnouncements.findByEventTitle", query = "SELECT e FROM EventAnnouncements e WHERE e.eventTitle = :eventTitle"),
    @NamedQuery(name = "EventAnnouncements.findByEventDate", query = "SELECT e FROM EventAnnouncements e WHERE e.eventDate = :eventDate"),
    @NamedQuery(name = "EventAnnouncements.findByEventLocation", query = "SELECT e FROM EventAnnouncements e WHERE e.eventLocation = :eventLocation")})
public class EventAnnouncements implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "announcement_id")
    private Integer announcementId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "event_title")
    private String eventTitle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "event_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;
    @Size(max = 255)
    @Column(name = "event_location")
    private String eventLocation;
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    @ManyToOne(optional = false)
    private PostItems postId;

    public EventAnnouncements() {
    }

    public EventAnnouncements(Integer announcementId) {
        this.announcementId = announcementId;
    }

    public EventAnnouncements(Integer announcementId, String eventTitle, Date eventDate) {
        this.announcementId = announcementId;
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
    }

    public Integer getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Integer announcementId) {
        this.announcementId = announcementId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public PostItems getPostId() {
        return postId;
    }

    public void setPostId(PostItems postId) {
        this.postId = postId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (announcementId != null ? announcementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventAnnouncements)) {
            return false;
        }
        EventAnnouncements other = (EventAnnouncements) object;
        if ((this.announcementId == null && other.announcementId != null) || (this.announcementId != null && !this.announcementId.equals(other.announcementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vmct.pojo.EventAnnouncements[ announcementId=" + announcementId + " ]";
    }
    
}
