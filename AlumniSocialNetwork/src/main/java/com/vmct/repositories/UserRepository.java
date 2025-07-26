<<<<<<< Updated upstream
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import com.vmct.pojo.Users;
import java.util.List;

/**
 *
 * @author Thanh Nhat
 */
public interface UserRepository {
      List<Users> getAllUsers();
    Users getUserById(Long id);
    void addUser(Users user);
    void updateUser(Users user);
    void deleteUser(Long id);
    Users getUserByEmail(String email);
    boolean updatePassword(Long userId, String newPassword);
    boolean isPasswordExpired(Long userId);
    void lockUserIfPasswordExpired(Long userId);
}
=======
//package com.vmct.repositories;
//
//import com.vmct.pojo.User;
//
//public interface UserRepository {
//    User getUserById(Long id);
//    boolean addOrUpdateUser(User user);
//    boolean deleteUser(Long id);
//}
>>>>>>> Stashed changes
