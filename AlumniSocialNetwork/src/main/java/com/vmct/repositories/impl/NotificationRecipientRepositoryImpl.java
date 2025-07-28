package com.vmct.repositories.impl;

import com.vmct.pojo.NotificationRecipient;
import com.vmct.repositories.NotificationRecipientRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class NotificationRecipientRepositoryImpl implements NotificationRecipientRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public boolean addNotificationRecipient(NotificationRecipient recipient) {
        try {
            Session session = factory.getObject().getCurrentSession();
            session.persist(recipient);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<NotificationRecipient> getRecipientsByNotificationId(Long notificationId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<NotificationRecipient> cq = cb.createQuery(NotificationRecipient.class);
        Root<NotificationRecipient> root = cq.from(NotificationRecipient.class);
        cq.select(root).where(cb.equal(root.get("notificationId").get("id"), notificationId));
        Query<NotificationRecipient> query = session.createQuery(cq);
        return query.getResultList();
    }
}
