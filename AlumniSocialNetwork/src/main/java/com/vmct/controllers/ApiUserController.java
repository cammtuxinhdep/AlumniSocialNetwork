/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.controllers;

import com.vmct.dto.UserDTO;
import com.vmct.pojo.User;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.vmct.services.UserService;
import com.vmct.utils.JwtUtils;
import java.security.Principal;
import java.util.Collections;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author HP
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {

    @Autowired
    private UserService userDetailsService;

    @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestParam Map<String, String> info, @RequestParam(value = "avatar") MultipartFile avatar) {
        User u = this.userDetailsService.register(info, avatar);

        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User u) throws Exception {
        if (this.userDetailsService.authenticate(u.getUsername(), u.getPassword())) {
            try {
                String token = JwtUtils.generateToken(u.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo token");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản không tồn tại");
    }

    @RequestMapping("/secure/profile")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<UserDTO> getProfile(Principal principal) throws Exception {
        return new ResponseEntity<>(this.userDetailsService.getUserByUsernameDTO(principal.getName()), HttpStatus.OK);
    }

    @PostMapping("/secure/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> data) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            String oldPassword = data.get("oldPassword");
            String newPassword = data.get("newPassword");

            this.userDetailsService.changePassword(username, oldPassword, newPassword);
            return ResponseEntity.ok(Collections.singletonMap("message", "Password changed successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", ex.getMessage()));
        }
    }

    @PostMapping("/secure/avatar")
    public ResponseEntity<UserDTO> changeAvatar(@RequestParam(value = "avatar") MultipartFile avatar) {
        this.userDetailsService.updateAvatar(SecurityContextHolder.getContext().getAuthentication().getName(), avatar);
        return new ResponseEntity<>(this.userDetailsService.getUserByUsernameDTO(SecurityContextHolder.getContext().getAuthentication().getName()),
                HttpStatus.OK);
    }

    @PostMapping("/secure/cover")
    public ResponseEntity<UserDTO> changeCover(@RequestParam(value = "cover") MultipartFile cover) {
        this.userDetailsService.updateCover(SecurityContextHolder.getContext().getAuthentication().getName(), cover);
        return new ResponseEntity<>(this.userDetailsService.getUserByUsernameDTO(SecurityContextHolder.getContext().getAuthentication().getName()),
                HttpStatus.OK);
    }
    
    @GetMapping("/secure/profile/{username}")
    public ResponseEntity<UserDTO> getUsernameProfile(@PathVariable(value = "username") String username) {
        return new ResponseEntity<>(this.userDetailsService.getUserByUsernameDTO(username), HttpStatus.OK);
    }
}
