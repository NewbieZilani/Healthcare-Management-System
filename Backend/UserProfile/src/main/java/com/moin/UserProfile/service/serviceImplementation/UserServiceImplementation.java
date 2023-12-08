package com.moin.UserProfile.service.serviceImplementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moin.UserProfile.dto.*;
import com.moin.UserProfile.entity.HealthDataEntity;
import com.moin.UserProfile.entity.UserEntity;
import com.moin.UserProfile.entity.UserProfileEntity;
import com.moin.UserProfile.exceptions.AlreadyExistsException;
import com.moin.UserProfile.exceptions.ResourceNotFoundException;
import com.moin.UserProfile.repository.HealthDataRepository;
import com.moin.UserProfile.repository.UserProfileRepository;
import com.moin.UserProfile.repository.UserRepository;
import com.moin.UserProfile.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements UserService, UserDetailsService {
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private HealthDataRepository healthDataRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private PatientService patientService;
    @Override
    public UserDto createUser(UserDto user) {
        ModelMapper modelMapper = new ModelMapper();
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new AlreadyExistsException("Email already exists");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID().toString());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setRole(user.getRole());
        UserEntity storedUserDetails = userRepository.save(userEntity);
        UserDto returnedValue = modelMapper.map(storedUserDetails, UserDto.class);
        return returnedValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).get();
        if (userEntity == null) throw new UsernameNotFoundException("No record found");
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).get();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(userEntity.getRole()));
        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getPassword(),
                true, true, true, true,
                roles);
    }

    @Override
    public UserProfileDto createUserProfile(UserProfileDto userProfileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        String userId = user.get().getId();

        if (userProfileRepository.findByUserId(userId).isPresent()) {
            throw new AlreadyExistsException("You are already create your profile. Now you may only update your profile");
        }

        UserProfileEntity userProfileEntity = new UserProfileEntity();
        userProfileEntity.setUserId(userId);
        userProfileEntity.setName(userProfileDto.getName());
        userProfileEntity.setGender(userProfileDto.getGender());
        userProfileEntity.setAddress(userProfileDto.getAddress());
        userProfileEntity.setAge(userProfileDto.getAge());
        userProfileEntity.setContactInfo(userProfileDto.getContactInfo());

        userProfileDto.setEmail(user.get().getEmail());
        userProfileDto.setUserID(userId);

        UserProfileEntity userProfile = userProfileRepository.save(userProfileEntity);
        return userProfileDto;
    }

    @Override
    public UserProfileDto getUserProfileDataById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        String userId = user.get().getId();
        String email = user.get().getEmail();
        Optional<UserProfileEntity> userProfileOptional = userProfileRepository.findByUserId(userId);

        if (userProfileOptional.isPresent()) {
            UserProfileEntity userProfileEntity = userProfileOptional.get();
            UserProfileDto map = new ModelMapper().map(userProfileEntity, UserProfileDto.class);
            map.setEmail(email);
            return map;
        } else {
            throw new ResourceNotFoundException("User profile not found for the authenticated user");
        }
    }
    @Override
    public UserProfileDto getUserProfileDataById(String userId) {

        Optional<UserProfileEntity> userProfileOptional = userProfileRepository.findByUserId(userId);

        if (userProfileOptional.isPresent()) {
            UserProfileEntity userProfileEntity = userProfileOptional.get();
            return new ModelMapper().map(userProfileEntity, UserProfileDto.class);
        } else {
            throw new ResourceNotFoundException("User profile not found by id");
        }
    }

    @Override
    public List<UserProfileDto> getAllUserProfiles() {
        List<UserEntity> userEntities = userRepository.findByRole("PATIENT");
        List<String> userId = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            userId.add(userEntity.getId());
        }
        List<UserProfileEntity> userProfileEntities = userProfileRepository.findByUserIdIn(userId);
        return userProfileEntities.stream()
                .map(userProfileEntity -> new ModelMapper().map(userProfileEntity, UserProfileDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserProfileDto updateUserProfile(UserProfileDto userProfileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        String userId = user.get().getId();

        Optional<UserProfileEntity> existingUserProfileOptional = userProfileRepository.findByUserId(userId);

        if (existingUserProfileOptional.isPresent()) {
            UserProfileEntity existingUserProfile = existingUserProfileOptional.get();
            existingUserProfile.setName(userProfileDto.getName());
            existingUserProfile.setGender(userProfileDto.getGender());
            existingUserProfile.setAddress(userProfileDto.getAddress());
            existingUserProfile.setAge(userProfileDto.getAge());
            existingUserProfile.setContactInfo(userProfileDto.getContactInfo());
            userProfileRepository.save(existingUserProfile);
            return userProfileDto;
        } else {
            throw new ResourceNotFoundException("User profile not found for user ID: " + userId);
        }
    }

    @Override
    public HealthDataDto createHealthData(HealthDataDto healthDataDto) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        String id = user.get().getId();
        if (healthDataRepository.findById(id).isPresent()) {
            throw new AlreadyExistsException("This user already exists in the database");
        }

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        try {
            healthDataDto.setBmi(healthDataDto.getWeightKg() / ((healthDataDto.getHeightCm() / 100) * (healthDataDto.getHeightCm() / 100)));
            healthDataDto.setDate(LocalDate.now());
            healthDataDto.setUserId(id);

            HealthDataEntity healthDataEntity = new HealthDataEntity();
            healthDataEntity.setUserId(id);
            healthDataEntity.setHeightCm(healthDataDto.getHeightCm());
            healthDataEntity.setWeightKg(healthDataDto.getWeightKg());
            healthDataEntity.setBloodSugarLevel(healthDataDto.getBloodSugarLevel());
            healthDataEntity.setHeartRate(healthDataDto.getHeartRate());
            healthDataEntity.setBmi(healthDataDto.getBmi());
            healthDataEntity.setDate(healthDataDto.getDate());
            healthDataEntity.setSleepHour(healthDataDto.getSleepHour());
            healthDataEntity.setBloodGroup(healthDataDto.getBloodGroup());
            healthDataEntity.setBloodPressure(healthDataDto.getBloodPressure());
            healthDataEntity.setSmoke(healthDataDto.getSmoke());

            healthDataRepository.save(healthDataEntity);

            return healthDataDto;
        } finally {
            patientService.callGptWithPatientData();
        }
    }

    @Override
    public List<HealthDataDto> getAllHealthData() {
        List<HealthDataEntity> healthDataEntities = healthDataRepository.findAll();
        return healthDataEntities.stream()
                .map(healthDataEntity -> new ModelMapper().map(healthDataEntity, HealthDataDto.class))
                .collect(Collectors.toList());
    }
    @Override
    public SendHealthDataDto sendHealthData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }

        String userId = user.get().getId();
        UserProfileEntity userProfileEntity = userProfileRepository.findByUserId(userId).get();
        HealthDataEntity healthDataEntity = healthDataRepository.findById(userId).get();
        SendHealthDataDto sendHealthDataDto = new SendHealthDataDto();
        sendHealthDataDto.setUserId(userId);
        sendHealthDataDto.setHeightCm(healthDataEntity.getHeightCm());
        sendHealthDataDto.setWeightKg(healthDataEntity.getWeightKg());
        sendHealthDataDto.setBloodSugarLevel(healthDataEntity.getBloodSugarLevel());
        sendHealthDataDto.setHeartRate(healthDataEntity.getHeartRate());
        sendHealthDataDto.setBmi(healthDataEntity.getBmi());
        sendHealthDataDto.setDate(healthDataEntity.getDate());
        sendHealthDataDto.setSleepHour(healthDataEntity.getSleepHour());
        sendHealthDataDto.setSmoke(healthDataEntity.getSmoke());
        sendHealthDataDto.setBloodGroup(healthDataEntity.getBloodGroup());
        sendHealthDataDto.setBloodPressure(healthDataEntity.getBloodPressure());
        sendHealthDataDto.setSmoke(healthDataEntity.getSmoke());
        sendHealthDataDto.setAge(userProfileEntity.getAge());
        return sendHealthDataDto;
    }

    @Override
    public UserEntity readByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Override
    public HealthDataDto getUserHealthDataById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        String userId = user.get().getId();
        Optional<HealthDataEntity> healthDataOptional = healthDataRepository.findById(userId);
        if (healthDataOptional.isPresent()) {
            HealthDataEntity healthDataEntity = healthDataOptional.get();
            HealthDataDto map = new ModelMapper().map(healthDataEntity, HealthDataDto.class);
            return map;
        } else {
            throw new ResourceNotFoundException("User profile not found for the authenticated user");
        }
    }

    @Override
    public HealthDataDto updateUserHealthData(HealthDataDto healthDataDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        String id = user.get().getId();
        Optional<HealthDataEntity> existingHealthDataOptional = healthDataRepository.findById(id);
        if (existingHealthDataOptional.isPresent()) {
            HealthDataEntity existingHealthData = existingHealthDataOptional.get();
            existingHealthData.setHeightCm(healthDataDto.getHeightCm());
            existingHealthData.setWeightKg(healthDataDto.getWeightKg());
            existingHealthData.setBloodSugarLevel(healthDataDto.getBloodSugarLevel());
            existingHealthData.setHeartRate(healthDataDto.getHeartRate());
            existingHealthData.setBmi(healthDataDto.getWeightKg() / ((healthDataDto.getHeightCm() / 100) * (healthDataDto.getHeightCm() / 100)));
            existingHealthData.setSleepHour(healthDataDto.getSleepHour());
            existingHealthData.setDate(LocalDate.now());
            existingHealthData.setSmoke(healthDataDto.getSmoke());
            existingHealthData.setBloodPressure(healthDataDto.getBloodPressure());
            existingHealthData.setBloodGroup(healthDataDto.getBloodGroup());
            healthDataRepository.save(existingHealthData);
            return healthDataDto;
        } else {
            throw new ResourceNotFoundException("Health data not found for user ID" + id);
        }
    }
    @Override
    public long getTotalPatientCount() {
        return userProfileRepository.countBy();
    }

    @Override
    public List<UserProfileDto> getUserProfileDataByName(String name) {
        List<UserProfileEntity> userProfileEntities = userProfileRepository.findByName(name);

        if (!userProfileEntities.isEmpty()) {
            return userProfileEntities
                    .stream()
                    .map(userProfileEntity -> new ModelMapper().map(userProfileEntity, UserProfileDto.class))
                    .collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException("No user profiles found by name");
        }
    }
//    @Override
//    public UserDto getUserByEmail(String email) {
//        UserEntity userEntity = userRepository.findByEmail(email).get();
//        return new ModelMapper().map(userEntity, UserDto.class);
//    }
}