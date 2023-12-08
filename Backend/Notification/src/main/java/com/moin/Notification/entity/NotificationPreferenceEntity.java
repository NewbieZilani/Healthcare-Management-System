package com.moin.Notification.entity;

import com.moin.Notification.enums.PreferenceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "NotificationPreference")
public class NotificationPreferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationPreferenceId;
    private String userId;
    @Enumerated(EnumType.STRING)
    private PreferenceType prefType;
    private Boolean enabled;
}
