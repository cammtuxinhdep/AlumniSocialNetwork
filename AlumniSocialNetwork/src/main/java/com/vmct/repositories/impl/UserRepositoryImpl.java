/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.repositories.impl;

import com.vmct.pojo.Users;
import com.vmct.repositories.UserRepository;
import jakarta.data.repository.Repository;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Thanh Nhat
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Users> getAllUsers() {
        return getCurrentSession().createQuery("FROM Users", Users.class).getResultList();
    }

    @Override
    public Users getUserById(Long id) {
        return getCurrentSession().get(Users.class, id);
    }

    @Override
    public void addUser(Users user) {
        getCurrentSession().persist(user);
    }

    @Override
    public void updateUser(Users user) {
        getCurrentSession().merge(user);
    }

    @Override
    public void deleteUser(Long id) {
        Users user = getUserById(id);
        if (user != null) {
            getCurrentSession().remove(user);
        }
    }

    @Override
    public Users getUserByEmail(String email) {
        List<Users> result = getCurrentSession()
            .createNamedQuery("Users.findByEmail", Users.class)
            .setParameter("email", email)
            .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public boolean updatePassword(Long userId, String newPassword) {
        Users user = getUserById(userId);
        if (user != null) {
            user.setPassword(newPassword);
            user.setPasswordChangeDeadline(null);
            updateUser(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPasswordExpired(Long userId) {
        Users user = getUserById(userId);
        if (user != null && user.getPasswordChangeDeadline() != null) {
            return new Date().after(user.getPasswordChangeDeadline());
        }
        return false;
    }

    @Override
    public void lockUserIfPasswordExpired(Long userId) {
        Users user = getUserById(userId);
        if (user != null && isPasswordExpired(userId)) {
            user.setIsLocked(true);
            updateUser(user);
        }
    }
}
