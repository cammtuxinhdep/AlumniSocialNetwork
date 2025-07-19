/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.repositories.impl;

import com.vmct.pojo.Posts;
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

/**
 *
 * @author Thanh Nhat
 */
@Repository
@Transactional
public class PostRepositoryImpl implements PostRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Posts getPostById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Posts.class, id);
    }

    @Override
    public boolean addOrUpdatePost(Posts post) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            if (post.getId() == null) {
                s.persist(post);
            } else {
                s.merge(post);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deletePost(Long id) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            Posts post = getPostById(id);
            if (post != null) {
                s.remove(post);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Posts> getPostsByUserId(Long userId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Posts> q = b.createQuery(Posts.class);
        Root<Posts> root = q.from(Posts.class);
        q.select(root).where(b.equal(root.get("userId").get("id"), userId));
        Query query = s.createQuery(q);
        return query.getResultList();
    }

    @Override
    public List<Posts> getAllPosts() {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Posts> q = b.createQuery(Posts.class);
        Root<Posts> root = q.from(Posts.class);
        q.select(root);
        Query query = s.createQuery(q);
        return query.getResultList();
    }

}
