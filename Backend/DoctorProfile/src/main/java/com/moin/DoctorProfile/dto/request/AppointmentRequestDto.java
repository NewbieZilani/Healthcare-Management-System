package com.moin.DoctorProfile.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDto {
    @NotNull(message = "No Doctor Id found!")
    private String doctorId;
    @NotNull(message = "No Slot Id found!")
    private String slotId;
    @NotNull(message = "No appointmentType is mentioned!")
    private String appointmentType;
    private String conferenceLink;
    private LocalTime startTime;
    @NotNull(message = "Please Specify the date.")
    private LocalDate appointmentDate;
}
