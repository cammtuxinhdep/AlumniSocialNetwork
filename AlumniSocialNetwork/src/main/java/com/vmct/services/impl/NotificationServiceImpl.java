package com.vmct.services.impl;

import com.vmct.pojo.Notification;
import com.vmct.pojo.NotificationRecipient;
import com.vmct.pojo.User;
import com.vmct.pojo.UserGroup;
import com.vmct.repositories.NotificationRecipientRepository;
import com.vmct.repositories.NotificationRepository;
import com.vmct.services.EmailService;
import com.vmct.services.NotificationService;
import com.vmct.services.UserGroupService;
import com.vmct.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationRecipientRepository recipientRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserGroupService userGroupService;

    @Override
    public boolean createOrUpdate(Notification notification) {
        return this.notificationRepository.addOrUpdateNotification(notification);
    }

    @Override
    public Notification getById(Long id) {
        return this.notificationRepository.getNotificationById(id);
    }

    @Override
    public List<Notification> getAll(Map<String, String> params) {
        return this.notificationRepository.getAllNotifications(params);
    }

    @Override
    public boolean delete(Long id) {
        return this.notificationRepository.deleteNotification(id);
    }

    @Override
    public void prepareRecipients(Long notificationId, String recipientType, List<Long> groupIds, List<Long> userIds) {
        switch (recipientType) {
            case "all" -> {
                NotificationRecipient r = new NotificationRecipient();
                r.setNotificationId(new Notification(notificationId));
                r.setIsAll(true);
                recipientRepository.addNotificationRecipient(r);
            }
            case "group" -> {
                if (groupIds != null && !groupIds.isEmpty()) {
                    for (Long groupId : groupIds) {
                        NotificationRecipient r = new NotificationRecipient();
                        r.setNotificationId(new Notification(notificationId));
                        r.setGroupId(new UserGroup(groupId));
                        recipientRepository.addNotificationRecipient(r);
                    }
                }
            }
            case "individual" -> {
                if (userIds != null && !userIds.isEmpty()) {
                    for (Long userId : userIds) {
                        User user = userService.getUserById(userId);
                        if (user != null) {
                            NotificationRecipient r = new NotificationRecipient();
                            r.setNotificationId(new Notification(notificationId));
                            r.setUserId(user);
                            recipientRepository.addNotificationRecipient(r);
                        }
                    }
                }
            }
        }
    }

 @Override
public boolean sendNotificationToRecipients(Long notificationId) {
    Notification notification = this.getById(notificationId);
    if (notification == null || notification.getTitle() == null || notification.getContent() == null) {
        throw new IllegalArgumentException("Thông báo không hợp lệ hoặc thiếu tiêu đề/nội dung.");
    }

    List<NotificationRecipient> recipients = recipientRepository.getRecipientsByNotificationId(notificationId);
    if (recipients == null || recipients.isEmpty()) {
        throw new IllegalStateException("Không có người nhận nào được chọn.");
    }

    List<String> recipientEmails = new ArrayList<>();
    for (NotificationRecipient recipient : recipients) {
        if (recipient.getIsAll() != null && recipient.getIsAll()) {
            for (User user : userService.getAllUsers()) {
                if (user.getEmail() != null && !user.getEmail().isEmpty() && isValidEmail(user.getEmail())) {
                    recipientEmails.add(user.getEmail());
                }
            }
        } else if (recipient.getGroupId() != null) {
            for (User user : userGroupService.getUsersByGroupIds(List.of(recipient.getGroupId().getId()))) {
                if (user.getEmail() != null && !user.getEmail().isEmpty() && isValidEmail(user.getEmail())) {
                    recipientEmails.add(user.getEmail());
                }
            }
        } else if (recipient.getUserId() != null && recipient.getUserId().getEmail() != null && isValidEmail(recipient.getUserId().getEmail())) {
            recipientEmails.add(recipient.getUserId().getEmail());
        }
    }

    System.out.println("Recipient emails: " + recipientEmails); // Debug
    if (recipientEmails.isEmpty()) {
        throw new IllegalStateException("Không có email hợp lệ để gửi.");
    }

    String subject = notification.getTitle();
    String htmlContent = String.format(
            "<h2>%s</h2><p>%s</p>%s%s",
            notification.getTitle(),
            notification.getContent(),
            notification.getEventDatetime() != null ? "<p><strong>Thời gian:</strong> " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(notification.getEventDatetime()) + "</p>" : "",
            notification.getEventLocation() != null ? "<p><strong>Địa điểm:</strong> " + notification.getEventLocation() + "</p>" : ""
    );

    try {
        emailService.sendEmailToGroup(recipientEmails, subject, htmlContent); 
        System.out.println("Email sending initiated for " + recipientEmails.size() + " recipients."); // Log
        return true; 
    } catch (Exception ex) {
        throw new RuntimeException("Lỗi khi gửi email: " + ex.getMessage(), ex);
    }
}
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}