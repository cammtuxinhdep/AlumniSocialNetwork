package com.vmct.repositories;

import com.vmct.pojo.Notification;
import java.util.List;
import java.util.Map;

public interface NotificationRepository {
    boolean addOrUpdateNotification(Notification notification);
    Notification getNotificationById(Long id);
    List<Notification> getAllNotifications(Map<String, String> params);
    boolean deleteNotification(Long id);
}
