/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import com.vmct.pojo.User;
import java.util.List;

/**
 *
 * @author HP
 */
public interface UserRepository {
    User getUserByUsername(String username);
    User getUserById(Long id);
    User addUser(User u);
    boolean authenticate(String username, String password);
    List<User> getAllUsers();
}