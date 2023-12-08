package com.moin.DoctorProfile.dto;

import com.moin.DoctorProfile.validator.ValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
    @NotBlank(message = "Doctor ID must not be blank")
    private String doctor_id;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @ValidRole
    private String role;

    @Pattern(regexp = "^(Male|Female)$", message = "Gender must be 'Male' or 'Female'")
    private String gender;

    @NotBlank(message = "Department must not be blank")
    private String department;

    @NotBlank(message = "Registration number must not be blank")
    private String registration_number_BDMC;

    @NotBlank(message = "Allocated room must not be blank")
    private String allocated_room;

    @NotBlank(message = "Qualifications must not be blank")
    private String qualifications;

    private Boolean isAvailable;

    private Boolean isValid;

    private String created_at;
    private String image;
}