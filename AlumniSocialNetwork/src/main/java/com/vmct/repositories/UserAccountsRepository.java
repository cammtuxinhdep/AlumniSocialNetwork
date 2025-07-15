/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import com.vmct.pojo.UserAccounts;
import java.util.List;

/**
 *
 * @author HP
 */
public interface UserAccountsRepository {
    List<UserAccounts> getUsers();
    UserAccounts getUserById(Integer userId);
    UserAccounts  saveUser(UserAccounts user);
}
