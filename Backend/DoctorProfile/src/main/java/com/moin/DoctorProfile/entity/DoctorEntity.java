package com.moin.DoctorProfile.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor_table")
public class DoctorEntity {
        @Id
        private String doctor_id;
        @NotBlank(message = "Name must not be blank")
        private String name;
        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be a valid email address")
        @Column(unique = true)
        private String email;
        @NotBlank(message = "Password must not be blank")
        private String password;
        @NotBlank(message = "Role must not be blank")
        private String role;
        @Pattern(regexp = "^(Male|Female)$", message = "Gender must be 'Male' or 'Female'")
        private String gender;
        @NotBlank(message = "BDMC registration number must not be blank")
        @Column(unique = true)
        private String registration_number_BDMC;
        @NotBlank(message = "Department must not be blank")
        private String department;
        @NotBlank(message = "Allocated room must not be blank")
        @Column(unique = true)
        private String allocated_room;
        @NotBlank(message = "Qualifications must not be blank")
        private String qualifications;
        @NotNull(message = "Validity must not be null")
        private Boolean isValid;
        @NotNull(message = "Availability must not be null")
        private Boolean isAvailable;
        //private LocalTime appointmentStartTime;
       // private Integer duration;
//        @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
//        private List<SlotEntity> slots;
        @NotNull(message = "Creation date must not be null")
        private LocalDate created_at;
        @NotNull(message = "Image url must not be null")
        private String image;
}