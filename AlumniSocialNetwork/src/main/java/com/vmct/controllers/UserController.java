// UserController.java
package com.vmct.controllers;

import com.vmct.pojo.Users;
import com.vmct.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new Users());
        return "users/add";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam Map<String, String> params,
                          @RequestParam("avatar") MultipartFile avatar,
                          Model model) {
        try {
            userService.addUser(params, avatar);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đăng ký thất bại: " + e.getMessage());
            return "users/add";
        }
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam Long userId,
                                 @RequestParam String newPassword,
                                 Model model) {
        if (userService.updatePassword(userId, newPassword)) {
            return "redirect:/login?updated";
        }
        model.addAttribute("errorMessage", "Cập nhật mật khẩu thất bại!");
        return "users/change-password";
    }

    @GetMapping("/lock-check/{id}")
    public String checkAndLockUser(@PathVariable("id") Long userId) {
        userService.lockUserIfPasswordExpired(userId);
        return "redirect:/login";
    }
} 
