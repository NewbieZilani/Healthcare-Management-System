package com.moin.Notification.repositroy;

import com.moin.Notification.entity.NotificationPreferenceEntity;
import com.moin.Notification.enums.PreferenceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreferenceEntity, Long> {
    Optional<NotificationPreferenceEntity> findByUserIdAndPrefType(String userID, PreferenceType s);

    List<NotificationPreferenceEntity> findAllByUserId(String userId);
}
