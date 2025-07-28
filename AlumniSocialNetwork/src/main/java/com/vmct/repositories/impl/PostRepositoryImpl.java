package com.vmct.repositories.impl;

import com.vmct.pojo.Post;
import com.vmct.repositories.PostRepository;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class PostRepositoryImpl implements PostRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private static final Logger logger = LoggerFactory.getLogger(PostRepositoryImpl.class);

    @Override
    public Post getPostById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);
        cq.select(root).where(cb.equal(root.get("id"), id));
        Query<Post> query = session.createQuery(cq);
        return query.uniqueResultOptional().orElse(null);
    }

    @Override
    public boolean addOrUpdatePost(Post post) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            if (post.getId() == null) {
                session.persist(post);
            } else {
                session.merge(post);
            }
            return true;
        } catch (Exception ex) {
            logger.error("Failed to save or update Post", ex);
            return false;
        }
    }

    @Override
    public boolean deletePost(Long id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Post post = getPostById(id);
            if (post != null) {
                session.remove(session.contains(post) ? post : session.merge(post));
            }
            return true;
        } catch (Exception ex) {
            logger.error("Failed to delete Post with ID {}", id, ex);
            return false;
        }
    }

    @Override
    public List<Post> getPostByUserId(Long userId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);
        cq.select(root).where(cb.equal(root.get("userId").get("id"), userId));
        cq.orderBy(cb.desc(root.get("createdAt")));
        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<Post> getAllPosts(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> root = cq.from(Post.class);
        Predicate predicate = cb.conjunction();

        // Example: lọc theo từ khoá tiêu đề
        if (params != null) {
            if (params.containsKey("kw")) {
                String kw = "%" + params.get("kw").trim().toLowerCase() + "%";
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("title")), kw));
            }
        }

        cq.select(root).where(predicate).orderBy(cb.desc(root.get("createdAt")));
        return session.createQuery(cq).getResultList();
    }
}
