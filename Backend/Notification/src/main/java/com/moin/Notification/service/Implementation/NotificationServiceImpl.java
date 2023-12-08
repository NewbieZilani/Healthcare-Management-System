package com.moin.Notification.service.Implementation;

import com.moin.Notification.dto.NotificationDto;
import com.moin.Notification.dto.UserDto;
import com.moin.Notification.entity.NotificationEntity;
import com.moin.Notification.entity.NotificationPreferenceEntity;
import com.moin.Notification.enums.PreferenceType;
import com.moin.Notification.exception.CustomException;
import com.moin.Notification.feignclient.UserClient;
import com.moin.Notification.repositroy.NotificationPreferenceRepository;
import com.moin.Notification.repositroy.NotificationRepository;
import com.moin.Notification.service.EmailNotificationService;
import com.moin.Notification.service.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;
    @Autowired
    private EmailNotificationService emailNotificationService;

    @Override
    public void createNotification(NotificationDto notificationDto) throws CustomException {
        try {
            UserDto user = userClient.getCurrentUserProfile();
            if (user == null) {
                throw new CustomException("User not found");
            }
            NotificationEntity notificationEntity = modelMapper.map(notificationDto, NotificationEntity.class);
            notificationEntity.setUserId(user.getUserID());
            notificationEntity.setIsRead(false);
            notificationEntity.setSent(LocalDateTime.now());
            notificationRepository.save(notificationEntity);
            Optional<NotificationPreferenceEntity> emailPreference = notificationPreferenceRepository
                    .findByUserIdAndPrefType(user.getUserID(), PreferenceType.Email);
            if (emailPreference.isPresent() && emailPreference.get().getEnabled()) {
                String email = user.getEmail();
                String subject = "Notification from HealthCare Management System : " + notificationDto.getNotificationType();
                String body = notificationDto.getNotificationText();
                emailNotificationService.sendEmail(email, subject, body);
            }
        } catch (Exception e) {
            throw new CustomException("Notification could not be saved");
        }
    }

    @Override
    public void updateNotification(NotificationDto notificationDto) throws CustomException {
        try {
            NotificationEntity notification = notificationRepository.findById(notificationDto.getNotificationId())
                    .orElseThrow(() -> new CustomException("Notification not found"));
            modelMapper.map(notificationDto, notification);
            notificationRepository.save(notification);
        } catch (Exception e) {
            throw new CustomException("Error updating notification");
        }
    }

    @Override
    public void deleteNotification(Long notificationId) throws CustomException {
        try {
            if (!notificationRepository.existsById(notificationId)) {
                throw new CustomException("Notification not found");
            }
            notificationRepository.deleteById(notificationId);
        } catch (Exception e) {
            throw new CustomException("Error deleting notification");
        }
    }

    @Override
    public List<NotificationDto> getAllNotificationsByUserId(String userId) throws CustomException {
        try {
            List<NotificationEntity> notificationEntities = notificationRepository.findAllByUserId(userId);
            if (notificationEntities.isEmpty()) {
                throw new CustomException("No notifications found");
            }
            return notificationEntities.stream()
                    .map(entity -> modelMapper.map(entity, NotificationDto.class))
                    .toList();
        } catch (Exception e) {
            throw new CustomException("Error fetching notifications");
        }
    }


    @Override
    public void markNotificationAsRead(Long notificationId) throws CustomException {
        try {
            NotificationEntity notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new CustomException("Notification not found"));
            notification.setIsRead(true);
            notificationRepository.save(notification);
        } catch (Exception e) {
            throw new CustomException("Error marking notification as read");
        }
    }

    @Override
    public void markAllNotificationsAsRead(String userId) throws CustomException {
        try {
            List<NotificationEntity> notifications = notificationRepository.findAllByUserId(userId);
            notifications.forEach(notification -> notification.setIsRead(true));
            notificationRepository.saveAll(notifications);
        } catch (Exception e) {
            throw new CustomException("Error marking all notifications as read");
        }
    }

    @Override
    public List<NotificationDto> getUnreadNotificationsByUserId(String userId) throws CustomException {
        try {
            List<NotificationEntity> notificationEntities = notificationRepository.findUnreadByUserId(userId);
            if (notificationEntities.isEmpty()) {
                return new ArrayList<>();
            }
            return notificationEntities.stream()
                    .map(entity -> modelMapper.map(entity, NotificationDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Error fetching unread notifications");
        }
    }

}

