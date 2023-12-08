package com.moin.DoctorProfile.service;

import com.moin.DoctorProfile.dto.request.AppointmentRequestDto;
import com.moin.DoctorProfile.dto.response.AppointmentResponseDto;
import com.moin.DoctorProfile.exceptions.CustomException;
import com.moin.DoctorProfile.exceptions.SlotIsBookedException;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentResponseDto createAppointment(String slotId,String appointmentType) throws SlotIsBookedException, Exception;
    AppointmentResponseDto cancelSlotBooking(String appointmentId) throws CustomException;
    AppointmentResponseDto completeAppointment(String appointmentId);
    List<AppointmentResponseDto> getAllCurrentAppointments(LocalDate date);
    List<AppointmentResponseDto> alUpcomingPatientAppointments();
    List<AppointmentResponseDto> patientAppointmentHistory(String patientId);
}
