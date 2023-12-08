package com.moin.DoctorProfile.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDTO {
    @NotBlank(message = "Name must not be blank")
    @NotEmpty(message = "Name must not be empty")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password must not be blank")
    private String password;

    @Pattern(regexp = "^(Male|Female)$", message = "Gender must be 'Male' or 'Female'")
    private String gender;
    @NotBlank(message = "Department must not be blank")
    private String department;

    @NotBlank(message = "BDMC registration number must not be blank")
    private String registration_number_BDMC;
    @NotBlank(message = "Room must not be blank")
    private String allocated_room;
    @NotBlank(message = "Qualifications must not be blank")
    private String qualifications;
    private String image;
}
