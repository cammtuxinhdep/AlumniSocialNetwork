package com.vmct.services.impl;

import com.vmct.pojo.Notification;
import com.vmct.pojo.NotificationRecipient;
import com.vmct.repositories.NotificationRecipientRepository;
import com.vmct.repositories.NotificationRepository;
import com.vmct.services.EmailService;
import com.vmct.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;

    @Autowired
    private NotificationRecipientRepository recipientRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public boolean createOrUpdate(Notification notification) {
        return this.notificationRepo.addOrUpdateNotification(notification);
    }

    @Override
    public Notification getById(Long id) {
        return this.notificationRepo.getNotificationById(id);
    }

    @Override
    public List<Notification> getAll(Map<String, String> params) {
        return this.notificationRepo.getAllNotifications(params);
    }

    @Override
    public boolean delete(Long id) {
        return this.notificationRepo.deleteNotification(id);
    }

    @Override
    public boolean sendNotificationToRecipients(Long notificationId) {
        Notification notification = this.getById(notificationId);
        if (notification == null)
            return false;

        List<NotificationRecipient> recipients = recipientRepository.getRecipientsByNotificationId(notificationId);

        if (recipients == null || recipients.isEmpty())
            return false;

        String subject = "Thư mời tham gia sự kiện từ nhà trường";
        String content = notification.getContent(); // Có thể format lại nếu là HTML

        for (NotificationRecipient recipient : recipients) {
            if (recipient.getUserId() != null && recipient.getUserId().getEmail() != null) {
                try {
                    emailService.sendEmail(recipient.getUserId().getEmail(), subject, content);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return true;
    }
}

