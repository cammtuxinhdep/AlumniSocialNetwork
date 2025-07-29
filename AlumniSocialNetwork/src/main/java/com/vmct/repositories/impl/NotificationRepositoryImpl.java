package com.vmct.repositories.impl;

import com.vmct.pojo.Notification;
import com.vmct.repositories.NotificationRepository;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class NotificationRepositoryImpl implements NotificationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public boolean addOrUpdateNotification(Notification notification) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            if (notification.getId() == null)
                session.persist(notification);
            else
                session.merge(notification);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Notification getNotificationById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        Root<Notification> root = cq.from(Notification.class);
        cq.select(root).where(cb.equal(root.get("id"), id));
        Query<Notification> query = session.createQuery(cq);
        return query.uniqueResultOptional().orElse(null);
    }

    @Override
    public List<Notification> getAllNotifications(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        Root<Notification> root = cq.from(Notification.class);
        Predicate predicate = cb.conjunction();

        if (params != null) {
            if (params.containsKey("title")) {
                String title = "%" + params.get("title").toLowerCase().trim() + "%";
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("title")), title));
            }
        }

        cq.select(root).where(predicate).orderBy(cb.desc(root.get("createdAt")));
        return session.createQuery(cq).getResultList();
    }

    @Override
    public boolean deleteNotification(Long id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Notification notification = getNotificationById(id);
            if (notification != null) {
                session.remove(session.contains(notification) ? notification : session.merge(notification));
                return true;
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
