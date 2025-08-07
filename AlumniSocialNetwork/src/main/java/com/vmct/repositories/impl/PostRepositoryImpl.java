package com.vmct.repositories.impl;

import com.vmct.pojo.Post;
import com.vmct.repositories.PostRepository;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
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
public class PostRepositoryImpl implements PostRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
  private static final int PAGE_SIZE = 6;
  

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
    cq.select(root);

    List<Predicate> predicates = new ArrayList<>();

    if (params != null) {
        String kw = params.get("kw");
        if (kw != null && !kw.trim().isEmpty()) {
            // Sửa: tìm kiếm không phân biệt hoa thường
            predicates.add(cb.like(cb.lower(root.get("content")), "%" + kw.trim().toLowerCase() + "%"));
        }

        String userId = params.get("userId");
        if (userId != null && !userId.isEmpty()) {
            try {
                predicates.add(cb.equal(root.get("userId").get("id"), Long.parseLong(userId)));
            } catch (NumberFormatException e) {
            }
        }
    }

    cq.where(predicates.toArray(Predicate[]::new));
    cq.orderBy(cb.desc(root.get("createdAt")));

    Query<Post> query = session.createQuery(cq);

    if (params != null && params.containsKey("page")) {
        try {
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;
            query.setMaxResults(PAGE_SIZE);
            query.setFirstResult(start);
        } catch (NumberFormatException e) {
        }
    }

    return query.getResultList();
}
}
