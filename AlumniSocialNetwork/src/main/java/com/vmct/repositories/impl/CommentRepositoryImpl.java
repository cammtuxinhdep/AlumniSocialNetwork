package com.vmct.repositories.impl;

import com.vmct.pojo.Comment;
import com.vmct.repositories.CommentRepository;

import jakarta.persistence.criteria.*;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
@Repository
@Transactional
public class CommentRepositoryImpl implements CommentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return this.factory.getObject().getCurrentSession();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(getCurrentSession().get(Comment.class, id));

    }

    @Override
    public boolean save(Comment comment) {
        if (comment == null) return false;
        Session session = getCurrentSession();
        Date now = new Date();

        if (comment.getId() == null) {
            comment.setCreatedAt(now);
            session.persist(comment);
        } else {
            comment.setUpdatedAt(now);
            session.merge(comment);
        }
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        Comment c = getCurrentSession().get(Comment.class, id);
        if (c != null) {
            getCurrentSession().remove(c);
            return true;
        }
        return false;
    }

    @Override
    public int countByPostId(Long postId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Comment> root = cq.from(Comment.class);

        cq.select(cb.count(root))
          .where(cb.equal(root.get("postId").get("id"), postId));

        Long result = session.createQuery(cq).getSingleResult();
        return result != null ? result.intValue() : 0;
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);
        Root<Comment> root = cq.from(Comment.class);

        cq.select(root)
          .where(cb.equal(root.get("postId").get("id"), postId))
          .orderBy(cb.asc(root.get("createdAt")));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<Comment> findByParentId(Long parentId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);
        Root<Comment> root = cq.from(Comment.class);

        cq.select(root)
          .where(cb.equal(root.get("parentId").get("id"), parentId))
          .orderBy(cb.asc(root.get("createdAt")));

        return session.createQuery(cq).getResultList();
    }
}
