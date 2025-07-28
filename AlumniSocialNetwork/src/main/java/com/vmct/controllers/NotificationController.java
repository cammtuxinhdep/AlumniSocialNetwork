package com.vmct.controllers;

import com.vmct.services.NotificationService;
import com.vmct.services.UserGroupService;
import com.vmct.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
     @Autowired
    private UserGroupService userGroupService;
    

  @GetMapping
public String showNotificationForm(Model model) {
    model.addAttribute("notifications", notificationService.getAll(null));
    model.addAttribute("userGroups", userGroupService.getAllGroups());
    model.addAttribute("users", userService.getAllUsers());
    return "notification-form";
}

    @PostMapping("/send")
    public String sendNotification(@RequestParam("notificationId") Long notificationId,
                                   Model model) {
        boolean success = notificationService.sendNotificationToRecipients(notificationId);
        model.addAttribute("message", success ? "Gửi thư mời thành công!" : "Không thể gửi thư mời.");
        return "notification-result";
    }
}
