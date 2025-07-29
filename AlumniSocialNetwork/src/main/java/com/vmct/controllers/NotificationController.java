package com.vmct.controllers;

import com.vmct.pojo.Notification;
import com.vmct.services.NotificationService;
import com.vmct.services.UserGroupService;
import com.vmct.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService userGroupService;

    // Trang danh sách thông báo
    @GetMapping
    public String listNotifications(Model model) {
        model.addAttribute("notifications", notificationService.getAll(null));
        return "notification-list";
    }

    // Trang form tạo mới hoặc sửa thông báo
    @GetMapping("/form")
    public String form(@RequestParam(name = "id", required = false) Long id, Model model) {
        Notification notification = (id != null) ? notificationService.getById(id) : new Notification();
        if (notification.getId() == null) {
            notification.setCreatedAt(new Date()); // Gán ngày tạo mặc định
        }
        model.addAttribute("notification", notification);
        model.addAttribute("userGroups", userGroupService.getAllGroups());
        model.addAttribute("users", userService.getAllUsers());
        return "notification-form";
    }

    // Lưu thông báo và tùy chọn gửi email
    @PostMapping("/save")
    public String save(
            @ModelAttribute("notification") Notification notification,
            @RequestParam(value = "sendImmediately", required = false) boolean sendImmediately,
            @RequestParam(value = "recipientType", required = false) String recipientType,
            @RequestParam(value = "groupIds", required = false) List<Long> groupIds,
            @RequestParam(value = "userIds", required = false) List<Long> userIds,
            Model model) {

        // Gán ngày tạo nếu là thông báo mới
        if (notification.getId() == null) {
            notification.setCreatedAt(new Date());
        }
        notificationService.createOrUpdate(notification);
        Long notificationId = notification.getId();

        if (sendImmediately && notificationId != null) {
            String message;
            try {
                // Chuẩn bị người nhận
                notificationService.prepareRecipients(notificationId, recipientType, groupIds, userIds);
                // Gửi email
                boolean success = notificationService.sendNotificationToRecipients(notificationId);
                message = success
                        ? "✅ Đã lưu và gửi thư mời thành công."
                        : "❌ Đã lưu nhưng gửi thư mời thất bại.";
            } catch (Exception e) {
                message = "⚠️ Đã lưu nhưng có lỗi khi gửi: " + e.getMessage();
            }
            model.addAttribute("message", message);
            return "notification-result";
        }

        return "redirect:/notification";
    }

    // Xóa thông báo
    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        notificationService.delete(id); // Xóa cả recipient liên quan trong service
        return "redirect:/notification";
    }

    // Gửi thư mời thủ công (tùy chọn sau khi lưu)
    @PostMapping("/send")
    public String send(
            @RequestParam("notificationId") Long notificationId,
            @RequestParam("recipientType") String recipientType,
            @RequestParam(name = "groupIds", required = false) List<Long> groupIds,
            @RequestParam(name = "userIds", required = false) List<Long> userIds,
            Model model) {

        String message;
        try {
            notificationService.prepareRecipients(notificationId, recipientType, groupIds, userIds);
            boolean success = notificationService.sendNotificationToRecipients(notificationId);
            message = success
                    ? "✅ Đã gửi thư mời thành công."
                    : "❌ Gửi thư mời thất bại.";
        } catch (Exception e) {
            message = "⚠️ Có lỗi xảy ra: " + e.getMessage();
        }
        model.addAttribute("message", message);
        return "notification-result";
    }
}