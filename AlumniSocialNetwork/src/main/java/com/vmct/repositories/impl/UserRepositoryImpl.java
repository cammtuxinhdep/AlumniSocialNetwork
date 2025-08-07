/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.repositories.impl;

import com.vmct.pojo.User;
import com.vmct.repositories.UserRepository;
import com.vmct.services.EmailService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private static final int PAGE_SIZE = 10;

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

    @Override
    public User getUserById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findById", User.class);
        q.setParameter("id", id);

        return (User) q.getSingleResult();
    }

    @Override
    public boolean authenticate(String username, String password) {
        User u = this.getUserByUsername(username);

        if (u == null) {
            return false;
        }
        Date now = new Date();
        if ("ROLE_LECTURER".equals(u.getUserRole()) && !u.getIsChecked() && now.after(u.getPasswordChangeDeadline()) && !u.getIsLocked()) {
            u.setIsLocked(Boolean.TRUE);
        }

        return this.passwordEncoder.matches(password, u.getPassword());
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();

        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root root = query.from(User.class);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                String p = "%" + kw.toLowerCase() + "%";
                Predicate p1 = builder.like(builder.lower(root.get("firstName").as(String.class)), p);
                Predicate p2 = builder.like(builder.lower(root.get("lastName").as(String.class)), p);
                predicates.add(builder.or(p1, p2));
            }

            String role = params.get("role");
            if (role != null && !role.isEmpty()) {
                predicates.add(builder.equal(root.get("userRole"), role));
            }

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(Predicate[]::new));
            }
        }

        Query q = s.createQuery(query);

        // Phan trang du lieu
        if (params != null) {
            String page = params.get("page");
            if (page != null) {
                int p = Integer.parseInt(page);
                int start = (p - 1) * PAGE_SIZE;

                q.setFirstResult(start);
                q.setMaxResults(PAGE_SIZE);
            }
        }

        return q.getResultList();
    }

    @Override
    public int getTotalAccountPages(String userRole) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = s.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        Predicate role = builder.equal(root.get("userRole"), userRole);
        query.where(role);
        query.select(builder.count(root));

        Long totalAccounts = s.createQuery(query).getSingleResult();

        return (int) Math.ceil(totalAccounts * 1.0 / PAGE_SIZE) + 1;
    }

    @Override
    public void deleteUser(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findById", User.class);
        q.setParameter("id", id);
        User u = (User) q.getSingleResult();

        s.remove(u);
    }

    @Override
    public List<User> getAllUsers() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findAll", User.class);
        return q.getResultList();
    }

    public void setLockedAlumni(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        User u = this.getUserById(id);

        if (!u.getIsChecked()) {
            u.setIsChecked(true);

            String subject = "Thông báo xác nhận mã số sinh viên thành công!";
            String htmlContent = String.format(
                    "<p>Mã số sinh viên của bạn đã được xác nhận thành công!</p>"
                    + "<p>Hãy đăng nhập và trải nghiệm dịch vụ của chúng tôi :)</p>"
                    + "<p>Trân trọng,</p>"
                    + "<p><strong>Alumni Social Network</strong></p>"
            );

            this.emailService.sendEmail(u.getEmail(), subject, htmlContent);
        }

        u.setIsLocked(!u.getIsLocked());

        s.merge(u);
    }

    @Override
    public User updateUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.merge(u);

        return u;
    }

    @Override
    public User getUserById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findById", User.class
        );
        q.setParameter("id", id);

        return (User) q.getSingleResult();
    }

//    @Override
//    public List<User> getAllUsers() {
//        Session session = this.factory.getObject().getCurrentSession();
//        CriteriaBuilder cb = session.getCriteriaBuilder();
//        CriteriaQuery<User> cq = cb.createQuery(User.class);
//        Root<User> root = cq.from(User.class);
//        cq.select(root);
//        Query<User> query = session.createQuery(cq);
//        return query.getResultList();
//    }
    @Override
    public void setLockedLecturer(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        User u = this.getUserById(id);

        if (!u.getIsChecked() && u.getIsLocked()) {
            u.setPasswordChangeDeadline(new Date(System.currentTimeMillis() + 86400000));

            String subject = "Gia hạn thời gian đổi mật khẩu!";
            String htmlContent = String.format(
                    "<p>Tài khoản của bạn đã được mở khóa.</p>"
                    + "<p>Vui lòng đăng nhập và đổi mật khẩu trong vòng 24h, nếu sau %s bạn không đổi mật khẩu chúng tôi sẽ phải khóa tài khoản của bạn lần nữa!</p>"
                    + "<p>Trân trọng,</p>"
                    + "<p><strong>Alumni Social Network</strong></p>",
                    "<strong>" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(u.getPasswordChangeDeadline()) + "</strong>"
            );

            this.emailService.sendEmail(u.getEmail(), subject, htmlContent);
        }

        u.setIsLocked(!u.getIsLocked());

        s.merge(u);
    }
}
