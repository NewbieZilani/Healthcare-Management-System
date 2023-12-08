package com.moin.Notification.service;

import com.moin.Notification.dto.NotificationPreferenceDto;
import com.moin.Notification.exception.CustomException;

import java.util.List;

public interface NotificationPreferenceService {
    void createNotificationPreference(NotificationPreferenceDto notificationPreferenceDto) throws CustomException;
    void updateNotificationPreference(NotificationPreferenceDto notificationPreferenceDto) throws CustomException;
    void deleteNotificationPreference(Long notificationPreferenceId) throws CustomException;
    NotificationPreferenceDto getNotificationPreferenceByPreferenceType(String userId, String preferenceType) throws CustomException;
    List<NotificationPreferenceDto> getAllNotificationPreferencesByUserId(String userId) throws CustomException;
}
