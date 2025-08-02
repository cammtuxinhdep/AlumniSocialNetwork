package com.vmct.repositories.impl;

import com.vmct.pojo.User;
import com.vmct.pojo.Post;
import com.vmct.repositories.StatsRepository;
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

@Repository
@Transactional
public class StatsRepositoryImpl implements StatsRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> countUsersByTime(String time, int year) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<User> root = q.from(User.class);

        q.multiselect(
            b.function(time, Integer.class, root.get("createdAt")),
            b.count(root.get("id"))
        );
        q.where(
            b.equal(b.function("YEAR", Integer.class, root.get("createdAt")), year)
        );
        q.groupBy(b.function(time, Integer.class, root.get("createdAt")));

        Query query = s.createQuery(q);
        return query.getResultList();
    }

    @Override
    public List<Object[]> countPostsByTime(String time, int year) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
        Root<Post> root = q.from(Post.class);

        q.multiselect(
            b.function(time, Integer.class, root.get("createdAt")),
            b.count(root.get("id"))
        );
        q.where(
            b.equal(b.function("YEAR", Integer.class, root.get("createdAt")), year)
        );
        q.groupBy(b.function(time, Integer.class, root.get("createdAt")));

        Query query = s.createQuery(q);
        return query.getResultList();
    }
}
