/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.repositories.impl;

import com.vmct.pojo.User;
import com.vmct.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public User addUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(u);
        
        return u;
    }

    @Override
    public User getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        
        return (User) q.getSingleResult();
    }
    
}
