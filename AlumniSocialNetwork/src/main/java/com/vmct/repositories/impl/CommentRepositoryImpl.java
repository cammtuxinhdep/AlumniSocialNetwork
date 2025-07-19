package com.vmct.repositories.impl;

import com.vmct.pojo.Comment;
import com.vmct.repositories.CommentRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Thanh Nhat
 */
@Repository
@Transactional
public class CommentRepositoryImpl implements CommentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Comment findById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        Query query = s.createQuery("SELECT c FROM Comments c "
                + "JOIN FETCH c.userId "
                + "LEFT JOIN FETCH c.commentsCollection "
                + "WHERE c.id = :id");
        query.setParameter("id", id);
        return (Comment) query.getSingleResult();
    }

    @Override
    public boolean save(Comment comment) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            if (comment.getId() == null) {
                s.persist(comment);
            } else {
                s.merge(comment);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Thêm log để debug nếu cần
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            Comment comment = findById(id);
            if (comment != null) {
                s.remove(comment);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Thêm log để debug nếu cần
            return false;
        }
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Comment> q = b.createQuery(Comment.class);
        Root<Comment> root = q.from(Comment.class);
        q.select(root).where(b.equal(root.get("postId").get("id"), postId));
        Query query = s.createQuery(q);
        return query.getResultList();
    }
}
