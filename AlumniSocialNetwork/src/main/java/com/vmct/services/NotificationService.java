package com.vmct.services;

import com.vmct.pojo.Notification;
import java.util.List;
import java.util.Map;

public interface NotificationService {
    boolean createOrUpdate(Notification notification);
    Notification getById(Long id);
    List<Notification> getAll(Map<String, String> params);
    boolean delete(Long id);
    boolean sendNotificationToRecipients(Long notificationId);
    void prepareRecipients(Long notificationId, String recipientType,List<Long> groupIds, List<Long> userIds);
}
