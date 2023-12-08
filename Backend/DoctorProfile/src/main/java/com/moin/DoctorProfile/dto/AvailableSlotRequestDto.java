package com.moin.DoctorProfile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableSlotRequestDto {
    private String doctorId;
    private LocalDate slotDate;
}
