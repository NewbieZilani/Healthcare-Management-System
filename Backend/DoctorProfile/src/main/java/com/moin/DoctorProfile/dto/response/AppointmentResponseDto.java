package com.moin.DoctorProfile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDto {
    private String appointmentId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String slotId;
    private String appointmentType;
    private String conferenceLink;
    private String status;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalDate createdAt;
}
