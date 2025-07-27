package com.vmct.repositories.impl;

import com.vmct.pojo.Reaction;
import com.vmct.repositories.ReactionRepository;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class ReactionRepositoryImpl implements ReactionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return this.factory.getObject().getCurrentSession();
    }

    @Override
    public Reaction getReactionById(Long id) {
        return getCurrentSession().get(Reaction.class, id);
    }

    @Override
    public List<Reaction> getReactionsByPostId(Long postId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Reaction> cq = cb.createQuery(Reaction.class);
        Root<Reaction> root = cq.from(Reaction.class);

        Predicate postPredicate = cb.equal(root.get("postId").get("id"), postId);

        cq.select(root)
          .where(postPredicate);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public boolean saveOrUpdate(Reaction reaction) {
        Session session = getCurrentSession();
        try {
            if (reaction.getId() == null)
                session.persist(reaction);
            else
                session.merge(reaction);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteReaction(Long id) {
        Reaction reaction = getReactionById(id);
        if (reaction != null) {
            getCurrentSession().remove(reaction);
            return true;
        }
        return false;
    }

    @Override
    public int countByPostId(Long postId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Reaction> root = cq.from(Reaction.class);

        Predicate postPredicate = cb.equal(root.get("postId").get("id"), postId);

        cq.select(cb.count(root))
          .where(postPredicate);

        Long result = session.createQuery(cq).getSingleResult();
        return result != null ? result.intValue() : 0;
    }

    @Override
    public int countByPostIdAndType(Long postId, String type) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Reaction> root = cq.from(Reaction.class);

        Predicate postPredicate = cb.equal(root.get("postId").get("id"), postId);
        Predicate typePredicate = cb.equal(root.get("type"), type);

        cq.select(cb.count(root))
          .where(cb.and(postPredicate, typePredicate));

        Long result = session.createQuery(cq).getSingleResult();
        return result != null ? result.intValue() : 0;
    }

    @Override
    public Map<String, Integer> getReactionStats(Long postId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Reaction> root = cq.from(Reaction.class);

        Predicate postPredicate = cb.equal(root.get("postId").get("id"), postId);

        cq.multiselect(root.get("type"), cb.count(root))
          .where(postPredicate)
          .groupBy(root.get("type"));

        List<Object[]> results = session.createQuery(cq).getResultList();

        Map<String, Integer> stats = new HashMap<>();
        for (Object[] row : results) {
            String type = (String) row[0];
            Long count = (Long) row[1];
            stats.put(type, count.intValue());
        }

        return stats;
    }

    @Override
    public Reaction getUserReaction(Long postId, Long userId) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Reaction> cq = cb.createQuery(Reaction.class);
        Root<Reaction> root = cq.from(Reaction.class);

        Predicate postPredicate = cb.equal(root.get("postId").get("id"), postId);
        Predicate userPredicate = cb.equal(root.get("user").get("id"), userId);

        cq.select(root)
          .where(cb.and(postPredicate, userPredicate));

        List<Reaction> results = session.createQuery(cq).getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}
