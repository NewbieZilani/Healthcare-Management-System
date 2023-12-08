package com.moin.Notification.repositroy;

import com.moin.Notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAllByUserId(String userId);

    List<NotificationEntity> findUnreadByUserId(String userId);
}
