package com.moin.UserProfile.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moin.UserProfile.dto.*;
import com.moin.UserProfile.entity.UserEntity;

import java.util.List;


public interface UserService {
    UserDto createUser(UserDto user) throws Exception;
    UserDto getUser(String email);
    UserProfileDto createUserProfile(UserProfileDto userProfileDto);
    UserProfileDto getUserProfileDataById();
    UserProfileDto getUserProfileDataById(String id);

    UserProfileDto updateUserProfile(UserProfileDto userProfileDto);
    SendHealthDataDto sendHealthData();
    HealthDataDto createHealthData(HealthDataDto healthDataDto) throws JsonProcessingException;
    UserEntity readByEmail(String email);
    HealthDataDto getUserHealthDataById();
    HealthDataDto updateUserHealthData(HealthDataDto healthDataDto);
    List<UserProfileDto> getAllUserProfiles();
    long getTotalPatientCount();
    List<HealthDataDto> getAllHealthData();
    List<UserProfileDto> getUserProfileDataByName(String userName);
    //UserDto getUserByEmail(String userEmail);

}
