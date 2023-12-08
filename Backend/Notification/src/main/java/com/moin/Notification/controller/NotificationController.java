package com.moin.Notification.controller;

import com.moin.Notification.dto.NotificationDto;
import com.moin.Notification.dto.NotificationPreferenceDto;
import com.moin.Notification.exception.CustomException;
import com.moin.Notification.service.EmailNotificationService;
import com.moin.Notification.service.NotificationPreferenceService;
import com.moin.Notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private EmailNotificationService emailNotificationService;
    @Autowired
    private NotificationPreferenceService notificationPreferenceService;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/create")
    public ResponseEntity<?> createNotification(@RequestBody NotificationDto notificationDto) throws CustomException {
        notificationService.createNotification(notificationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Notification created successfully");
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateNotification(@RequestBody NotificationDto notificationDto) throws CustomException {
        notificationService.updateNotification(notificationDto);
        return ResponseEntity.status(HttpStatus.OK).body("Notification updated successfully");
    }
    @PutMapping("/delete/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable("notificationId") Long notificationId) throws CustomException {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.status(HttpStatus.OK).body("Notification deleted successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDto>> getAllNotificationsByUserId(@PathVariable("userId") String userId) throws CustomException {
        List<NotificationDto> notifications = notificationService.getAllNotificationsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(notifications);
    }

    @PostMapping("/markRead/{notificationId}")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable("notificationId") Long notificationId) throws CustomException {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.status(HttpStatus.OK).body("Notification marked as read successfully");
    }
    @PostMapping("/markAllAsRead/{userId}")
    public ResponseEntity<?> markAllNotificationsAsRead(@PathVariable("userId") String userId) throws CustomException {
        notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.status(HttpStatus.OK).body("All notifications marked as read successfully");
    }
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<NotificationDto>> getUnreadNotificationsByUserId(@PathVariable("userId") String userId) throws CustomException {
        List<NotificationDto> notifications = notificationService.getUnreadNotificationsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(notifications);
    }
    @PostMapping("/email/{emailTo}/{subject}/{messageBody}")
    public ResponseEntity<?> sendEmail(@PathVariable("emailTo") String emailTo, @PathVariable("subject") String subject, @PathVariable("messageBody") String messageBody) throws CustomException {
        emailNotificationService.sendEmail(emailTo, subject, messageBody);
        return ResponseEntity.status(HttpStatus.OK).body("Email sent successfully");
    }
    @PostMapping("/preferences/create")
    public ResponseEntity<?> createNotificationPreference(@RequestBody NotificationPreferenceDto notificationPreferenceDto) throws CustomException {
        notificationPreferenceService.createNotificationPreference(notificationPreferenceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Notification preference created successfully");
    }

    @PutMapping("/preferences/update")
    public ResponseEntity<?> updateNotificationPreference(@RequestBody NotificationPreferenceDto notificationPreferenceDto) throws CustomException {
        notificationPreferenceService.updateNotificationPreference(notificationPreferenceDto);
        return ResponseEntity.status(HttpStatus.OK).body("Notification preference updated successfully");
    }

    @DeleteMapping("/preferences/delete/{notificationPreferenceId}")
    public ResponseEntity<?> deleteNotificationPreference(@PathVariable("notificationPreferenceId") Long notificationPreferenceId) throws CustomException {
        notificationPreferenceService.deleteNotificationPreference(notificationPreferenceId);
        return ResponseEntity.status(HttpStatus.OK).body("Notification preference deleted successfully");
    }
    @GetMapping("/preferences/{userId}")
    public ResponseEntity<List<NotificationPreferenceDto>> getAllNotificationPreferencesByUserId(@PathVariable("userId") String userId) throws CustomException {
        List<NotificationPreferenceDto> notificationPreferences = notificationPreferenceService.getAllNotificationPreferencesByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(notificationPreferences);
    }
    @GetMapping("/preferences/{userId}/{preferenceType}")
    public ResponseEntity<NotificationPreferenceDto> getNotificationPreferenceByPreferenceType(@PathVariable("userId") String userId, @PathVariable("preferenceType") String preferenceType) throws CustomException {
        NotificationPreferenceDto notificationPreferenceByPreferenceType= notificationPreferenceService.getNotificationPreferenceByPreferenceType(userId, preferenceType);
        return ResponseEntity.status(HttpStatus.OK).body(notificationPreferenceByPreferenceType);
    }
}
