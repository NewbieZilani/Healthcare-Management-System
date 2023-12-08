package com.moin.CommunityPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userID;
    private String name;
    //private String email;
    private String gender;
    private Integer age;
    private String contactInfo;
    private String address;
    private String image;
}