package com.moin.Notification.feignclient;

import com.moin.Notification.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", configuration = FeignClientConfiguration.class)
public interface UserClient {
    @GetMapping("/users/getUserByEmail")
    UserDto getUserByEmail(@RequestParam("email") String email);

    @GetMapping("/users/getProfileById")
    UserDto getCurrentUserProfile();

    @GetMapping("users/getUserProfile")
    UserDto getUserProfileById(@RequestParam("userId") String userId);
}