package com.moin.UserProfile.service.serviceImplementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moin.UserProfile.dto.HealthDataGPTDto;
import com.moin.UserProfile.entity.HealthDataEntity;
import com.moin.UserProfile.entity.UserEntity;
import com.moin.UserProfile.exceptions.ResourceNotFoundException;
import com.moin.UserProfile.networkmanager.UserFeignClient;
import com.moin.UserProfile.repository.HealthDataRepository;
import com.moin.UserProfile.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HealthDataRepository healthDataRepository;
    public void callGptWithPatientData() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        String userId = user.get().getId();
        Optional<HealthDataEntity> healthDataOptional = healthDataRepository.findById(userId);
        HealthDataEntity healthDataEntity = healthDataOptional.get();
        HealthDataGPTDto patientHealthData = new ModelMapper().map(healthDataEntity, HealthDataGPTDto.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = " analyzes patient data to suggest diagnoses, treatment plans, and medication recommendations." +
                "response should be in single line for every expect." +
                "first think properly then give the answer properly"
                + objectMapper.writeValueAsString(patientHealthData);
         userFeignClient.chat(requestBody);

    }
}
