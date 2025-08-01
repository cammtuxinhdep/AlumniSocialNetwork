package com.vmct.repositories;

import com.vmct.pojo.NotificationRecipient;
import java.util.List;

public interface NotificationRecipientRepository {
    boolean addNotificationRecipient(NotificationRecipient recipient);
    List<NotificationRecipient> getRecipientsByNotificationId(Long notificationId);
}
