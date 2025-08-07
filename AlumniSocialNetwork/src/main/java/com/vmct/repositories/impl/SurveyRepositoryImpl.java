package com.vmct.repositories.impl;

import com.vmct.pojo.Survey;
import com.vmct.repositories.SurveyRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Hibernate;

@Repository
@Transactional
public class SurveyRepositoryImpl implements SurveyRepository {

    private static final Logger logger = LoggerFactory.getLogger(SurveyRepositoryImpl.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return this.factory.getObject().getCurrentSession();
    }

    @Override
public Survey getSurveyById(Long id) {
    try {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
        Root<Survey> root = cq.from(Survey.class);
        cq.select(root).where(cb.equal(root.get("id"), id));

        Survey survey = session.createQuery(cq).uniqueResultOptional().orElse(null);

        if (survey != null) {
            Hibernate.initialize(survey.getSurveyOptionSet());
            Hibernate.initialize(survey.getSurveyResponseSet());

            for (var option : survey.getSurveyOptionSet()) {
                Hibernate.initialize(option.getSurveyResponseSet()); 
            }
        }

        return survey;
    } catch (Exception e) {
        logger.error("Error getting survey by ID {}", id, e);
        return null;
    }
}


    @Override
    public boolean addOrUpdateSurvey(Survey survey) {
        try {
            Session session = getCurrentSession();
            if (survey.getId() == null) {
                session.persist(survey);
            } else {
                session.merge(survey);
            }
            return true;
        } catch (Exception e) {
            logger.error("Error saving or updating survey", e);
            return false;
        }
    }

    @Override
    public boolean deleteSurvey(Long id) {
        try {
            Session session = getCurrentSession();
            Survey survey = getSurveyById(id);
            if (survey != null) {
                session.remove(session.contains(survey) ? survey : session.merge(survey));
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error deleting survey with ID {}", id, e);
            return false;
        }
    }

    @Override
    public List<Survey> getSurveysByTitle(String title) {
        try {
            Session session = getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
            Root<Survey> root = cq.from(Survey.class);
            cq.select(root).where(cb.like(root.get("title"), "%" + title + "%"));

            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            logger.error("Error getting surveys by title", e);
            return List.of();
        }
    }

    @Override
    public List<Survey> getAllSurveys(Map<String, String> params) {
        try {
            Session session = getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
            Root<Survey> root = cq.from(Survey.class);
            cq.select(root);

            List<Predicate> predicates = new ArrayList<>();

            if (params != null) {
                String kw = params.get("kw");
                if (kw != null && !kw.isEmpty()) {
                    predicates.add(cb.like(root.get("title"), "%" + kw + "%"));
                }

                if (!predicates.isEmpty()) {
                    cq.where(predicates.toArray(new Predicate[0]));
                }

                String orderBy = params.get("orderBy");
                if (orderBy != null && !orderBy.isEmpty()) {
                    cq.orderBy(cb.desc(root.get(orderBy)));
                } else {
                    cq.orderBy(cb.desc(root.get("createdAt")));
                }
            }

            Query<Survey> query = session.createQuery(cq);

            if (params != null && params.containsKey("page")) {
                int page = Integer.parseInt(params.get("page"));
                int start = (page - 1) * PAGE_SIZE;
                query.setFirstResult(start);
                query.setMaxResults(PAGE_SIZE);
            }

            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting all surveys", e);
            return List.of();
        }
    }
}