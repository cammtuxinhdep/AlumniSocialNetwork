/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.controllers;

import com.vmct.pojo.User;
import com.vmct.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author HP
 */
@Controller
@RequestMapping("/accounts")
public class AccountController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping()
    public String listAccounts(Model model, @RequestParam Map<String, String> params) {
        
        // Phân trang hiển thị user theo role
        String role = params.get("role");
        int totalPages = this.userService.getTotalAccountPages(role);
        
        if (!params.containsKey("page") && totalPages > 1) {
            params.put("page", "1");
        }
        
        String page = params.get("page");
        int currentPage = (page != null && page.matches("\\d+")) ? Integer.parseInt(page) : 1;
        
        model.addAttribute("accounts",this.userService.getUsers(params));
        model.addAttribute("kw", params.get("kw"));
        model.addAttribute("role", role);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        return "/accounts";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable(value = "id") int id,
            @RequestParam Map<String, String> params) {
        this.userService.deleteUser(id);
        String role = params.get("role");
        String page = params.getOrDefault("page", "1");
        return "redirect:/accounts?role=" + role + "&page=" + page;
    }
    
    @PostMapping("/add")
    public String addLecturer(@RequestParam Map<String, String> params) {
        this.userService.addLecturer(params);
        return "redirect:/accounts";
    }
}
