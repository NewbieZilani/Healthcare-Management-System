package com.moin.Notification.service;

import com.moin.Notification.dto.NotificationDto;
import com.moin.Notification.exception.CustomException;

import java.util.List;

public interface NotificationService {
    void createNotification(NotificationDto notificationDto) throws CustomException;
    void updateNotification(NotificationDto notificationDto) throws CustomException;
    void deleteNotification(Long notificationId) throws CustomException;
    List<NotificationDto> getAllNotificationsByUserId(String userId) throws CustomException;
    void markNotificationAsRead(Long notificationId) throws CustomException;
    void markAllNotificationsAsRead(String userId) throws CustomException;
    List<NotificationDto> getUnreadNotificationsByUserId(String userId) throws CustomException;

}
