package com.moin.UserProfile.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moin.UserProfile.dto.*;
import com.moin.UserProfile.exceptions.AuthenticationExceptionFound;
import com.moin.UserProfile.service.serviceImplementation.UserServiceImplementation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserServiceImplementation userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto){
            UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registered Successfully");
        }

    @PostMapping("/createProfile")
    public ResponseEntity<UserProfileDto> createUserProfile(@RequestBody UserProfileDto userProfileDto){
            UserProfileDto userProfile = userService.createUserProfile(userProfileDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userProfile);
    }
    @GetMapping("/getProfileById")
    public ResponseEntity<?> getUserProfileById(){
        try{
            UserProfileDto userProfile = userService.getUserProfileDataById();
            return ResponseEntity.status(HttpStatus.OK).body(userProfile);
        }
        catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/getUserProfile")
    public ResponseEntity<?> getById(@RequestParam("userId") String userId){
        try {
            UserProfileDto userProfile = userService.getUserProfileDataById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(userProfile);
        }catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PutMapping("/updateUserProfile")
    public ResponseEntity<UserProfileDto> updateUserProfile(@RequestBody UserProfileDto userProfileDto){
        UserProfileDto userProfile = userService.updateUserProfile(userProfileDto);
        return ResponseEntity.status(HttpStatus.OK).body(userProfile);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserProfileDto>> getAllUserProfiles() {
        List<UserProfileDto> userProfiles = userService.getAllUserProfiles();
        return ResponseEntity.ok(userProfiles);
    }

    @GetMapping("/health-data")
    public SendHealthDataDto sendHealthData(){
        return userService.sendHealthData();
    }

    @PostMapping("/createHealthDAta")
    public ResponseEntity<HealthDataDto> createHealthData(@RequestBody HealthDataDto healthDataDto) throws JsonProcessingException {
        HealthDataDto healthData = userService.createHealthData(healthDataDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(healthData);
    }

    @GetMapping("/getHealthDataById")
    public ResponseEntity<HealthDataDto> getUserHealthDataById(){
        HealthDataDto healthData = userService.getUserHealthDataById();
        return ResponseEntity.status(HttpStatus.OK).body(healthData);
    }

    @PutMapping("/updateUserHealthData")
    public void updateUserHealthData(@RequestBody HealthDataDto healthDataDto){
        userService.updateUserHealthData(healthDataDto);
    }
    @GetMapping("/totalPatient")
    public ResponseEntity<String> getTotalPatientCount() {
        long totalPatientCount = userService.getTotalPatientCount();
        return new ResponseEntity<>("Total Number Of Patients :" +totalPatientCount, HttpStatus.OK);
    }

    @GetMapping("/getAllHealthData")
    public ResponseEntity<List<HealthDataDto>> getAllHealthData() {
        List<HealthDataDto> healthDataList = userService.getAllHealthData();
        return ResponseEntity.ok(healthDataList);
    }

    @GetMapping("/getUserProfileByName/{name}")
    public ResponseEntity<?> getByName(@PathVariable("name") String name){
        try {
            List<UserProfileDto> userProfiles = userService.getUserProfileDataByName(name);

            if (!userProfiles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(userProfiles);
            } else {
                // You may choose to return a different HTTP status code, such as HttpStatus.NOT_FOUND
                return ResponseEntity.status(HttpStatus.OK).body("No user profiles found by name");
            }
        } catch (AuthenticationExceptionFound e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

//    @GetMapping("/getUserByEmail/{email}")
//    public UserDto getUserByEmail(@RequestParam("email") String email){
//        return userService.getUserByEmail(email);
//    }
}
