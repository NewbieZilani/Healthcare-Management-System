package com.moin.DoctorProfile.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestDto {
    private String name;
    private String gender;
    private String department;
    private String registration_number_BDMC;
    private String allocated_room;
    private String qualifications;
}