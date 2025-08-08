/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.services;

import com.vmct.dto.UserDTO;
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

    UserDTO getUserByUsernameDTO(String username);

    User getUserByUsername(String username);

    User getUserById(Long id);

    User register(Map<String, String> params, MultipartFile avatar);

    boolean authenticate(String username, String password);

    User addLecturer(User u);

    List<User> getAllUsers();

    List<User> getUsers(Map<String, String> params);

    int getTotalAccountPages(String userRole);

    void deleteUser(int id);

    void setLockedAlumni(int id);

    User updateUser(User u);

    User getUserById(int id);

    void changePassword(String username, String oldPassword, String newPassword);

    void setLockedLecturer(int id);
    void updateAvatar(String username, MultipartFile avatar);
    void updateCover(String username, MultipartFile cover);
}
