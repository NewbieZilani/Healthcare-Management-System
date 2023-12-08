package com.moin.DoctorProfile;

import com.moin.DoctorProfile.dto.UserDto;
import com.moin.DoctorProfile.dto.response.AppointmentResponseDto;
import com.moin.DoctorProfile.entity.AppointmentEntity;
import com.moin.DoctorProfile.entity.DoctorEntity;
import com.moin.DoctorProfile.feignclient.UserClient;
import com.moin.DoctorProfile.repository.AppointmentRepository;
import com.moin.DoctorProfile.exceptions.CustomException;
import com.moin.DoctorProfile.service.serviceImplementation.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test methods will go here

    @Test
    void cancelSlotBooking_Success() throws CustomException {
        // Arrange
        String appointmentId = "appointment123";
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setAppointmentDate(LocalDate.now().plusDays(1)); // Future date
        appointment.setStartTime(LocalTime.now().minusHours(1)); // Past time

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // Act
        AppointmentResponseDto responseDto = appointmentService.cancelSlotBooking(appointmentId);

        // Assert
        assertNotNull(responseDto);
        assertEquals("CANCELED", appointment.getStatus());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void cancelSlotBooking_Failure_TimePassed() {
        // Arrange
        String appointmentId = "appointment123";
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setAppointmentDate(LocalDate.now()); // Today's date
        appointment.setStartTime(LocalTime.now().minusHours(1)); // Past time

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            appointmentService.cancelSlotBooking(appointmentId);
        });
        assertEquals("You can not decline the appointment! date time exceed!", thrown.getMessage());
    }


    @Test
    void cancelSlotBooking_Failure_NonExistentAppointment() {
        // Arrange
        String appointmentId = "nonexistent123";

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException thrown = assertThrows(CustomException.class, () -> {
            appointmentService.cancelSlotBooking(appointmentId);
        });
        assertEquals("Unable to cancel the appointment", thrown.getMessage());
    }



}