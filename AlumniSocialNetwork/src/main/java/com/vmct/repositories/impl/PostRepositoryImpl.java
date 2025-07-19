package com.vmct.repositories.impl;

import com.vmct.pojo.Post;
import com.vmct.repositories.PostRepository;
import org.springframework.stereotype.Repository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
@Transactional
public class PostRepositoryImpl implements PostRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private static final Logger logger = LoggerFactory.getLogger(PostRepositoryImpl.class);

    @Override
    public Post getPostById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Post> query = s.createQuery(
                "SELECT DISTINCT p FROM Post p "
                + "LEFT JOIN FETCH p.userId "
                + "LEFT JOIN FETCH p.commentsCollection c "
                + "LEFT JOIN FETCH c.commentsCollection "
                + "LEFT JOIN FETCH c.userId "
                + "WHERE p.id = :pid", Post.class);
        query.setParameter("pid", id);

        return query.uniqueResultOptional().orElse(null);
    }

    @Override
    public boolean addOrUpdatePost(Post post) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            if (post.getId() == null) {
                s.persist(post);
            } else {
                s.merge(post);
            }
            return true;
        } catch (Exception e) {
            logger.error("Error saving or updating post", e);
            return false;
        }
    }

    @Override
    public boolean deletePost(Long id) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            Post post = getPostById(id);
            if (post != null) {
                s.remove(s.contains(post) ? post : s.merge(post));
            }
            return true;
        } catch (Exception e) {
            logger.error("Error deleting post with id {}", id, e);
            return false;
        }
    }

    @Override
    public List<Post> getPostByUserId(Long userId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Post> query = s.createQuery(
                "SELECT DISTINCT p FROM Post p "
                + "LEFT JOIN FETCH p.userId "
                + "LEFT JOIN FETCH p.commentsCollection c "
                + "LEFT JOIN FETCH c.commentsCollection "
                + "LEFT JOIN FETCH c.userId "
                + "WHERE p.userId.id = :uid", Post.class);
        query.setParameter("uid", userId);
        return query.getResultList();
    }

    @Override
    public List<Post> getAllPost() {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Post> query = s.createQuery("FROM Post p", Post.class);
        return query.getResultList();
    }

}

