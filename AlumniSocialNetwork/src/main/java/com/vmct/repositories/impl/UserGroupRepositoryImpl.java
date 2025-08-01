package com.vmct.repositories.impl;

import com.vmct.pojo.User;
import com.vmct.pojo.UserGroup;
import com.vmct.repositories.UserGroupRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class UserGroupRepositoryImpl implements UserGroupRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<UserGroup> getAllGroups() {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserGroup> cq = cb.createQuery(UserGroup.class);
        Root<UserGroup> root = cq.from(UserGroup.class);
        cq.select(root);
        return session.createQuery(cq).getResultList();
    }

    @Override
    public UserGroup getGroupById(Long id) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(UserGroup.class, id);
    }
    @Override
    public List<User> getUsersByGroupIds(List<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return new ArrayList<>();
        }
        Session session = factory.getObject().getCurrentSession();
        Query<User> query = session.createQuery(
            "SELECT u FROM User u JOIN group_members gm ON u.id = gm.user_id WHERE gm.group_id IN :groupIds",
            User.class
        );
        query.setParameter("groupIds", groupIds);
        return query.getResultList();
}
}
