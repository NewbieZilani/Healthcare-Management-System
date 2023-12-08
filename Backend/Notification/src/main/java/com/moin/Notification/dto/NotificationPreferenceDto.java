package com.moin.Notification.dto;

import com.moin.Notification.enums.PreferenceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPreferenceDto {
    private Long notificationPreferenceId;
    private String userId;
    private PreferenceType prefType;
    private Boolean enabled;
}
