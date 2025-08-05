/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vmct.dto.UserDTO;
import com.vmct.pojo.User;
import com.vmct.repositories.UserRepository;
import com.vmct.services.EmailService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.vmct.services.UserService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HP
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private EmailService emailService;

    @Override
    public User getUserByUsername(String username) {
        return this.userRepo.getUserByUsername(username);
    }

    @Override
    public UserDTO getUserByUsernameDTO(String username) {
        User u = this.userRepo.getUserByUsername(username);
        UserDTO userDTO = new UserDTO(u);

        return userDTO;
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepo.getUserById(id);
    }

    @Override
    public User register(Map<String, String> params, MultipartFile avatar) {
        User u = new User();
        u.setFirstName(params.get("firstName"));
        u.setLastName(params.get("lastName"));
        u.setStudentId(params.get("studentId"));
        u.setEmail(params.get("email"));
        u.setUsername(params.get("username"));
        u.setPassword(this.passwordEncoder.encode(params.get("password")));
        u.setCreatedAt(new Date());
        u.setIsLocked(Boolean.TRUE);
        u.setUserRole("ROLE_ALUMNI");

        if (!avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

        return this.userRepo.addUser(u);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid email!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getUserRole()));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return this.userRepo.authenticate(username, password);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepo.getAllUsers();
    }

    @Override
    public User addLecturer(User u) {
        u.setPassword(this.passwordEncoder.encode("ou@123"));
        u.setCreatedAt(new Date());
        u.setPasswordChangeDeadline(new Date(System.currentTimeMillis() + 86400000));
        u.setUserRole("ROLE_LECTURER");
        u.setIsLocked(Boolean.FALSE);

        String subject = "Nhắc nhở đổi mật khẩu tài khoản!";
        String htmlContent = String.format(
                "<p>Tài khoản của bạn đã được tạo thành công với tên đăng nhập %s và mật khẩu <strong>ou@123</strong></p>"
                + "<p>Vui lòng đăng nhập và đổi mật khẩu trong vòng 24h, nếu sau %s bạn không đổi mật khẩu chúng tôi sẽ phải khóa tài khoản của bạn!</p>"
                + "<p>Trân trọng,</p>"
                + "<p><strong>Alumni Social Network</strong></p>",
                "<strong>" + u.getUsername() + "</strong>",
                "<strong>" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(u.getPasswordChangeDeadline()) + "</strong>"
        );

        this.emailService.sendEmail(u.getEmail(), subject, htmlContent);

        return this.userRepo.addUser(u);
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        return this.userRepo.getUsers(params);
    }

    @Override
    public int getTotalAccountPages(String userRole) {
        return this.userRepo.getTotalAccountPages(userRole);
    }

    @Override
    public void deleteUser(int id) {
        this.userRepo.deleteUser(id);
    }

    @Override
    public void setLockedAlumni(int id) {
        this.userRepo.setLockedAlumni(id);
    }

    @Override
    public User getUserById(int id) {
        return this.userRepo.getUserById(id);
    }

    @Override
    public User updateUser(User u) {
        if (u.getAvatarFile() != null && !u.getAvatarFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(u.getAvatarFile().getBytes(),
                        ObjectUtils.asMap("resource type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

        if (u.getCoverFile() != null && !u.getCoverFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(u.getCoverFile().getBytes(),
                        ObjectUtils.asMap("resource type", "auto"));
                u.setCover(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

        return this.userRepo.updateUser(u);
    }

    @Override
    public User changePassword(String username, String password) {
        User u = this.userRepo.getUserByUsername(username);
        u.setPassword(this.passwordEncoder.encode(password));

        if (u.getUserRole() == "ROLE_LECTURER" && !u.getIsChecked()) {
            u.setIsChecked(true);
            u.setPasswordChangeDeadline(null);
        }

        return this.userRepo.updateUser(u);
    }

    @Override
    public void setLockedLecturer(int id) {
        this.userRepo.setLockedLecturer(id);
    }
}
