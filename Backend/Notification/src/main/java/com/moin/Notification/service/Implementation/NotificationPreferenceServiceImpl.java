package com.moin.Notification.service.Implementation;

import com.moin.Notification.dto.NotificationPreferenceDto;
import com.moin.Notification.dto.UserDto;
import com.moin.Notification.entity.NotificationPreferenceEntity;
import com.moin.Notification.enums.PreferenceType;
import com.moin.Notification.exception.CustomException;
import com.moin.Notification.feignclient.UserClient;
import com.moin.Notification.repositroy.NotificationPreferenceRepository;
import com.moin.Notification.service.NotificationPreferenceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserClient userClient;
    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;
    @Override
    public void createNotificationPreference(NotificationPreferenceDto notificationPreferenceDto) throws CustomException {
        try{
            UserDto user = userClient.getCurrentUserProfile();
            if (user == null) {
                throw new CustomException("User not found");
            }
            NotificationPreferenceEntity notificationPreferenceEntity = modelMapper.map(notificationPreferenceDto, NotificationPreferenceEntity.class);
            notificationPreferenceEntity.setUserId(user.getUserID());
            notificationPreferenceEntity.setEnabled(true);
            notificationPreferenceRepository.save(notificationPreferenceEntity);
        } catch (Exception e){
            throw new CustomException("Notification preference could not be saved");
        }
    }
    @Override
    public void updateNotificationPreference(NotificationPreferenceDto notificationPreferenceDto) throws CustomException {
        try {
            Long id = notificationPreferenceDto.getNotificationPreferenceId();
            Optional<NotificationPreferenceEntity> existingEntity = notificationPreferenceRepository.findById(id);
            if (existingEntity.isEmpty()) {
                throw new CustomException("Notification preference not found");
            }
            NotificationPreferenceEntity updatedEntity = modelMapper.map(notificationPreferenceDto, NotificationPreferenceEntity.class);
            notificationPreferenceRepository.save(updatedEntity);
        } catch (Exception e) {
            throw new CustomException("Notification preference could not be updated");
        }
    }

    @Override
    public void deleteNotificationPreference(Long notificationPreferenceId) throws CustomException {
        try {
            Optional<NotificationPreferenceEntity> entity = notificationPreferenceRepository.findById(notificationPreferenceId);
            if (entity.isEmpty()) {
                throw new CustomException("Notification preference not found");
            }
            notificationPreferenceRepository.deleteById(notificationPreferenceId);
        } catch (Exception e) {
            throw new CustomException("Notification preference could not be deleted");
        }
    }
    @Override
    public NotificationPreferenceDto getNotificationPreferenceByPreferenceType(String userId, String preferenceType) throws CustomException {
        try{
            Optional<NotificationPreferenceEntity> entity = notificationPreferenceRepository.findByUserIdAndPrefType(userId
                    , PreferenceType.valueOf(preferenceType));
            if(entity.isEmpty()) {
                throw new CustomException("Notification preference not found");
            }
            return modelMapper.map(entity.get(), NotificationPreferenceDto.class);
        } catch (Exception e){
            throw new CustomException("Notification preference could not be fetched");
    }
    }
    @Override
    public List<NotificationPreferenceDto> getAllNotificationPreferencesByUserId(String userId) throws CustomException {
        try{
            List<NotificationPreferenceEntity> entities = notificationPreferenceRepository.findAllByUserId(userId);
            if (entities.isEmpty()) {
                throw new CustomException("Notification preferences not found");
            }
            return entities.stream()
                    .map(entity -> modelMapper.map(entity, NotificationPreferenceDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new CustomException("Notification preferences could not be fetched");
        }
    }
}
