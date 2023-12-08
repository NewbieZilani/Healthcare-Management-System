package com.moin.DoctorProfile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponseDTO {
    private String name;
    private String email;
    private String role;
    private String gender;
    private String registration_number_BDMC;
    private String allocated_room;
    private String qualifications;
    private Boolean isAvailable;
    private Boolean isValid;
    private LocalDate created_at;

}