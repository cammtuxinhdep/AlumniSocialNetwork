/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vmct.pojo.Users;
import com.vmct.repositories.UserRepository;
import com.vmct.services.UserService;
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

/**
 *
 * @author Thanh Nhat
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public Users addUser(Map<String, String> params, MultipartFile avatar) {
        Users u = new Users();
        u.setFullName(params.get("fullName"));
        u.setEmail(params.get("email"));
        u.setPassword(passwordEncoder.encode(params.get("password")));
        u.setStudentId(params.get("studentId"));
        u.setRole(params.get("role"));

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(avatar.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(uploadResult.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("Avatar upload failed: " + ex.getMessage());
            }
        }

        userRepository.addUser(u);
        return u;
    }

    @Override
    public boolean updatePassword(Long userId, String newPassword) {
        return userRepository.updatePassword(userId, passwordEncoder.encode(newPassword));
    }

    @Override
    public boolean isPasswordExpired(Long userId) {
        return userRepository.isPasswordExpired(userId);
    }

    @Override
    public void lockUserIfPasswordExpired(Long userId) {
        userRepository.lockUserIfPasswordExpired(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users u = userRepository.getUserByEmail(username);
        if (u == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + u.getRole()));

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(), u.getPassword(), authorities);
    }
}