/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.services;

import com.vmct.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HP
 */
public interface UserService extends UserDetailsService {
    User getUserByUsername(String username);
    User register(Map<String, String> params, MultipartFile avatar);
    boolean authenticate(String username, String password);
<<<<<<< HEAD
    List<User> getAllUsers();
=======
    User addLecturer(Map<String, String> params);
>>>>>>> 04a4d0aab0373ed2d1ebed257d2bcb62e85632b5
}
