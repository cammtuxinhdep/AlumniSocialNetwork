package com.vmct.repositories.impl;

import com.vmct.pojo.GroupMember;
import com.vmct.pojo.User;
import com.vmct.pojo.UserGroup;
import com.vmct.repositories.UserGroupRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class UserGroupRepositoryImpl implements UserGroupRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public List<UserGroup> getAllGroups() {
        Session session = getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserGroup> cq = cb.createQuery(UserGroup.class);
        Root<UserGroup> root = cq.from(UserGroup.class);
        cq.select(root);
        return session.createQuery(cq).getResultList();
    }

    @Override
    public UserGroup getGroupById(Long id) {
        return getSession().get(UserGroup.class, id);
    }

    @Override
    public List<User> getUsersByGroupIds(List<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return new ArrayList<>();
        }

        Session session = getSession();
        Query<User> query = session.createQuery(
                "SELECT u FROM GroupMember gm JOIN gm.userId u WHERE gm.groupId.id IN :groupIds",
                User.class
        );

        query.setParameter("groupIds", groupIds);
        return query.getResultList();
    }

    @Override
    public boolean addUserToGroup(Long groupId, Long userId) {
        Session session = getSession();
        UserGroup group = session.get(UserGroup.class, groupId);
        User user = session.get(User.class, userId);

        if (group == null || user == null) {
            return false;
        }

        GroupMember gm = new GroupMember();
        gm.setGroupId(group);
        gm.setUserId(user);

        session.persist(gm);
        return true;
    }

    @Override
    public boolean removeUserFromGroup(Long groupId, Long userId) {
        Session session = getSession();
        Query<GroupMember> query = session.createQuery(
                "FROM GroupMember gm WHERE gm.groupId.id = :groupId AND gm.userId.id = :userId",
                GroupMember.class
        );
        query.setParameter("groupId", groupId);
        query.setParameter("userId", userId);

        GroupMember gm = query.uniqueResult();
        if (gm != null) {
            session.remove(gm);
            return true;
        }
        return false;
    }

    @Override
    public boolean createGroup(UserGroup group) {
        Session session = getSession();
        group.setCreatedAt(new Date());
        session.persist(group);
        return true;
    }

    @Override
    public boolean updateGroup(UserGroup group) {
        getSession().merge(group);
        return true;
    }

    @Override
    public boolean deleteGroup(Long id) {
        Session session = getSession();
        UserGroup group = session.get(UserGroup.class, id);
        if (group != null) {
            session.remove(group);
            return true;
        }
        return false;
    }
}
