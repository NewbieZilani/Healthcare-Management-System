package com.moin.DoctorProfile;

import com.moin.DoctorProfile.dto.response.AppointmentResponseDto;
import com.moin.DoctorProfile.entity.AppointmentEntity;
import com.moin.DoctorProfile.entity.DoctorEntity;
import com.moin.DoctorProfile.feignclient.UserClient;
import com.moin.DoctorProfile.repository.AppointmentRepository;
import com.moin.DoctorProfile.repository.DoctorRepository;
import com.moin.DoctorProfile.exceptions.CustomException;
import com.moin.DoctorProfile.dto.UserDto;
import com.moin.DoctorProfile.service.serviceImplementation.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UpcomingAppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test methods will go here
    @Test
    void alUpcomingPatientAppointments_Success() {
        // Arrange
        UserDto user = new UserDto(); // Set necessary user properties
        user.setUserID("someUserId");
        user.setName("John Doe");
        when(userClient.getCurrentUserProfile()).thenReturn(user);

        AppointmentEntity appointment = new AppointmentEntity(); // Create a sample appointment
        appointment.setDoctorId("doctor123");
        appointment.setAppointmentDate(LocalDate.now().plusDays(1)); // Future date
        appointment.setStatus("CREATED"); // Set the status
        // Set other necessary properties of AppointmentEntity

        List<AppointmentEntity> appointments = new ArrayList<>();
        appointments.add(appointment); // Add the appointment to the list
        when(appointmentRepository.findByPatientIdAndAppointmentDateAfterOrderByAppointmentDateAscStartTimeAsc(eq(user.getUserID()), any(LocalDate.class)))
                .thenReturn(appointments);

        DoctorEntity doctor = new DoctorEntity(); // Populate doctor entity
        doctor.setName("Dr. Smith");
        when(doctorRepository.findById(eq(appointment.getDoctorId()))).thenReturn(Optional.of(doctor));

        // Act
        List<AppointmentResponseDto> result = appointmentService.alUpcomingPatientAppointments();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        // Additional assertions as needed, such as checking properties of the returned DTO
    }



    @Test
    void alUpcomingPatientAppointments_UnauthorizedUser() {
        // Arrange
        when(userClient.getCurrentUserProfile()).thenReturn(null);

        // Act & Assert
        assertThrows(CustomException.class, () -> {
            appointmentService.alUpcomingPatientAppointments();
        });
    }

    @Test
    void alUpcomingPatientAppointments_NoUpcomingAppointments() {
        // Arrange
        UserDto user = new UserDto(); // Set necessary user properties
        when(userClient.getCurrentUserProfile()).thenReturn(user);

        when(appointmentRepository.findByPatientIdAndAppointmentDateAfterOrderByAppointmentDateAscStartTimeAsc(anyString(), any(LocalDate.class)))
                .thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(CustomException.class, () -> {
            appointmentService.alUpcomingPatientAppointments();
        });
    }

}
