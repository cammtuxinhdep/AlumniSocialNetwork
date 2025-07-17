/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.services;

import com.vmct.pojo.Users;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Thanh Nhat
 */
public interface UserService extends UserDetailsService {
       Users getUserByEmail(String email);
    Users addUser(Map<String, String> params, MultipartFile avatar);
    boolean updatePassword(Long userId, String newPassword);
    boolean isPasswordExpired(Long userId);
    void lockUserIfPasswordExpired(Long userId);
}
