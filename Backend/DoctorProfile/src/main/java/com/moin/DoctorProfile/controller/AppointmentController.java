package com.moin.DoctorProfile.controller;

import com.moin.DoctorProfile.dto.response.AppointmentResponseDto;
import com.moin.DoctorProfile.exceptions.SlotIsBookedException;
import com.moin.DoctorProfile.service.BindingService;
import com.moin.DoctorProfile.service.serviceImplementation.AppointmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentServiceImpl appointmentService;
    @Autowired
    private BindingService bindingService;

//    @PostMapping("/book/{slotId}/{appointmentType}")
//    public ResponseEntity<UserProfileDto> createUserProfile(@PathVariable("slotId") Long SlotId,@PathVariable("appointmentType") String appointmentType){
//        UserProfileDto userProfile = userService.createUserProfile(userProfileDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(userProfile);
//    }


    @PostMapping("/book/{slotId}/{appointmentType}")
    public ResponseEntity<Object> createAppointment(@PathVariable("slotId") String slotId,@PathVariable("appointmentType") String appointmentType) throws SlotIsBookedException, Exception {
//        ResponseEntity<Object> errors = bindingService.getBindingError(bindingResult);
//        if (errors != null) return errors;
        AppointmentResponseDto responseDto = appointmentService.createAppointment(slotId,appointmentType);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @PostMapping("/cancel/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(@PathVariable String appointmentId) throws SlotIsBookedException, Exception {
        AppointmentResponseDto responseDto = appointmentService.cancelSlotBooking(appointmentId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully deleted your appointment");
    }
    @PostMapping("/complete/{appointmentId}")
    public ResponseEntity<?> completeAppointment(@PathVariable String appointmentId){
        AppointmentResponseDto responseDto = appointmentService.completeAppointment(appointmentId);
        return ResponseEntity.status(HttpStatus.CREATED).body("You are successfully completed your appointment");
    }
    @GetMapping("/getCurrentAppointments/{date}")
    public ResponseEntity<Object> getCurrentAppointments(@PathVariable LocalDate date){
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAllCurrentAppointments(date));
    }
    @GetMapping("/getUpcomingAppointments")
    public ResponseEntity<Object> getUpcomingAppointments(){
        List<AppointmentResponseDto> responses = appointmentService.alUpcomingPatientAppointments();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
    @GetMapping("/history/{patientId}")
    public ResponseEntity<Object> appointmentHistoryByPatient(@PathVariable String patientId){
        List<AppointmentResponseDto> responses = appointmentService.patientAppointmentHistory(patientId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}