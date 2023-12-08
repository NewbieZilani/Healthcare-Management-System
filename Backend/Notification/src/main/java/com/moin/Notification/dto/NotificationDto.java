package com.moin.Notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long notificationId;
    private String userId;
    private String notificationType;
    private String notificationText;
    private LocalDateTime sent;
    private Boolean isRead;
}
