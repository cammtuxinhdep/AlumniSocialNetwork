package com.vmct.repositories.impl;

import com.vmct.pojo.SurveyOption;
import com.vmct.repositories.SurveyOptionRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
@Repository

public class SurveyOptionRepositoryImpl implements SurveyOptionRepository {

    private static final Logger logger = LoggerFactory.getLogger(SurveyOptionRepositoryImpl.class);
    private final SessionFactory sessionFactory;

    public SurveyOptionRepositoryImpl(LocalSessionFactoryBean factoryBean) {
        this.sessionFactory = factoryBean.getObject();
    }

    private Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public SurveyOption getSurveyOptionById(Long id) {
        Session session = null;
        try {
            session = getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SurveyOption> cq = cb.createQuery(SurveyOption.class);
            Root<SurveyOption> root = cq.from(SurveyOption.class);
            cq.select(root).where(cb.equal(root.get("id"), id));

            return session.createQuery(cq)
                    .uniqueResultOptional()
                    .orElse(null);
        } catch (HibernateException e) {
            logger.error("Error retrieving survey option with ID {}", id, e);
            return null;
        }
    }

    @Override
    public boolean addOrUpdateSurveyOption(SurveyOption option) {
        Session session = null;
        try {
            if (option == null || option.getOptionText() == null || option.getSurveyId() == null) {
                logger.error("Invalid survey option: null option, optionText, or surveyId");
                return false;
            }
            session = getCurrentSession();
            if (option.getId() == null) {
                session.persist(option);
            } else {
                session.merge(option);
            }
            return true;
        } catch (HibernateException e) {
            logger.error("Error saving or updating survey option", e);
            return false;
        }
    }

    @Override
    public boolean deleteSurveyOption(Long id) {
        Session session = null;
        try {
            session = getCurrentSession();
            SurveyOption option = getSurveyOptionById(id);
            if (option != null) {
                session.remove(session.contains(option) ? option : session.merge(option));
                return true;
            }
            return false;
        } catch (HibernateException e) {
            logger.error("Error deleting survey option with ID {}", id, e);
            return false;
        }
    }
}