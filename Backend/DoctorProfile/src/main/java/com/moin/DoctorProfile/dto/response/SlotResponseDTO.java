package com.moin.DoctorProfile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotResponseDTO {
    private String slotId;
    private String doctorId;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;
    private LocalDate slotDate;
}
