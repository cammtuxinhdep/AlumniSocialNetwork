/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import com.vmct.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HP
 */
public interface UserRepository {
    User getUserByUsername(String username);
    List<User> getAllUsers();
    User getUserById(Long id);
    User addUser(User u);
    boolean authenticate(String username, String password);
    List<User> getUsers(Map<String, String> params);
    int getTotalAccountPages(String userRole);
    void deleteUser(int id);
    void setLockedAlumni(int id);
    User updateUser(User u);
    User getUserById(int id);
}
