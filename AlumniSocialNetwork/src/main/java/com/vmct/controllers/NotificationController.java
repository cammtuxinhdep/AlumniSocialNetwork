package com.vmct.controllers;

import com.vmct.pojo.Notification;
import com.vmct.services.NotificationService;
import com.vmct.services.UserGroupService;
import com.vmct.services.UserService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserGroupService userGroupService;

    // Hiển thị danh sách thư mời
    @GetMapping
    public String listNotifications(Model model) {
        model.addAttribute("notifications", notificationService.getAll(null));
        return "notification-list";
    }

    // Hiển thị form tạo/sửa thư mời
    @GetMapping("/form")
    public String form(@RequestParam(name = "id", required = false) Long id, Model model) {
        Notification notification = (id != null) ? notificationService.getById(id) : new Notification();
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(new Date());
        }
        model.addAttribute("notification", notification);
        setCommonModelAttributes(model);
        return "notification-form";
    }
@PostMapping("/save")
public String save(
        @ModelAttribute("notification") @Valid Notification notification,
        BindingResult result,
        @RequestParam(value = "sendImmediately", required = false) boolean sendImmediately,
        @RequestParam(value = "recipientType", required = false) String recipientType,
        @RequestParam(value = "groupIds", required = false) List<Long> groupIds,
        @RequestParam(value = "userIds", required = false) List<Long> userIds,
        Model model) {

    System.out.println("Received notification: " + notification);
    System.out.println("Send immediately: " + sendImmediately);
    System.out.println("Recipient Type: " + recipientType);
    System.out.println("Group IDs: " + groupIds);
    System.out.println("User IDs: " + userIds);

    String message;

    // Xử lý validation
    if (result.hasErrors()) {
        model.addAttribute("message", "⚠️ Dữ liệu không hợp lệ. Vui lòng kiểm tra lại!");
        model.addAttribute("notification", notification);
        setCommonModelAttributes(model);
        return "notification-form";
    }

    // Kiểm tra bổ sung
    if (notification.getTitle() == null || notification.getTitle().trim().isEmpty()
            || notification.getContent() == null || notification.getContent().trim().isEmpty()) {
        model.addAttribute("message", "⚠️ Tiêu đề và nội dung không được để trống.");
        model.addAttribute("notification", notification);
        setCommonModelAttributes(model);
        return "notification-form";
    }

    // Kiểm tra eventDatetime và eventLocation nếu cần
    if (notification.getEventDatetime() == null) {
        model.addAttribute("message", "⚠️ Thời gian tổ chức không được để trống.");
        model.addAttribute("notification", notification);
        setCommonModelAttributes(model);
        return "notification-form";
    }
    if (notification.getEventLocation() == null || notification.getEventLocation().trim().isEmpty()) {
        model.addAttribute("message", "⚠️ Địa điểm không được để trống.");
        model.addAttribute("notification", notification);
        setCommonModelAttributes(model);
        return "notification-form";
    }

    // Lưu thông báo
    if (notification.getId() == null) {
        notification.setCreatedAt(new Date());
    }
    notificationService.createOrUpdate(notification);
    Notification savedNotification = notificationService.getById(notification.getId());
    Long notificationId = (savedNotification != null) ? savedNotification.getId() : null;

    // Gửi email nếu được chọn
    if (sendImmediately && notificationId != null) {
        try {
            notificationService.prepareRecipients(notificationId, recipientType, groupIds, userIds);
            boolean success = notificationService.sendNotificationToRecipients(notificationId);
            message = success
                    ? "✅ Đã lưu và gửi thư mời thành công."
                    : "❌ Đã lưu nhưng gửi thư mời thất bại. Vui lòng kiểm tra log.";
        } catch (Exception e) {
            message = "⚠️ Đã lưu nhưng có lỗi khi gửi: " + e.getMessage();
            System.err.println("Detailed error: " + e.getMessage());
            e.printStackTrace();
        }
    } else {
        message = "✅ Đã lưu thông báo thành công.";
    }

    model.addAttribute("notification", savedNotification);
    model.addAttribute("message", message);
    setCommonModelAttributes(model);
    return "notification-form";
}
    // Đặt dữ liệu dùng chung cho model
    private void setCommonModelAttributes(Model model) {
        model.addAttribute("userGroups", userGroupService.getAllGroups());
        model.addAttribute("users", userService.getAllUsers());
    }
}   