package com.moin.DoctorProfile.controller;

import com.moin.DoctorProfile.dto.request.SlotRequestDTO;
import com.moin.DoctorProfile.dto.request.RegistrationRequestDTO;
import com.moin.DoctorProfile.dto.request.UpdateRequestDto;
import com.moin.DoctorProfile.dto.response.SlotResponseDTO;
import com.moin.DoctorProfile.dto.response.RegistrationResponseDTO;
import com.moin.DoctorProfile.exceptions.AlreadyExistsException;
import com.moin.DoctorProfile.exceptions.AuthenticationExceptionFound;
import com.moin.DoctorProfile.exceptions.EmailNotFoundException;
import com.moin.DoctorProfile.service.BindingService;
import com.moin.DoctorProfile.service.serviceImplementation.DoctorServiceImplementation;
import com.moin.DoctorProfile.dto.AvailableSlotRequestDto;
import com.moin.DoctorProfile.dto.DoctorDto;
import com.moin.DoctorProfile.dto.DoctorProfileDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorServiceImplementation doctorService;
    @Autowired
    private BindingService bindingService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegistrationRequestDTO registerRequest, BindingResult bindingResult){
        ResponseEntity<Object> errors = bindingService.getBindingError(bindingResult);
        if (errors != null) return errors;
        RegistrationResponseDTO registerResponse = doctorService.createDoctor(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }
    @PostMapping("/verify/{doctorId}")
    public ResponseEntity<?> doctorVerification(@PathVariable("doctorId") String doctorId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body( doctorService.verifyDoctor(doctorId));
        }catch (AlreadyExistsException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PostMapping("/disableDoctor/{doctorId}")
    public ResponseEntity<?> disableDoctor(@PathVariable("doctorId") String doctorId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body( doctorService.disableDoctor(doctorId));
        }catch (AlreadyExistsException e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
//    @PostMapping("/changeAvailabilityStatus/{doctorId}")
//    public ResponseEntity<?> changeStatus(@PathVariable("doctorId") String doctorId){
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body( doctorService.changeStatus(doctorId));
//        }catch (AlreadyExistsException e)
//        {
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage());
//        }
//    }
    @PutMapping("/update")
    public ResponseEntity<Object> updateDoctorProfile(@RequestBody UpdateRequestDto doctorProfileDto
            , BindingResult bindingResult){
        ResponseEntity<Object> errors = bindingService.getBindingError(bindingResult);
        if (errors != null) return errors;
        DoctorProfileDto userProfile = doctorService.updateDoctor(doctorProfileDto);
        return ResponseEntity.status(HttpStatus.OK).body(userProfile);
    }
    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable("email") String email){
        try {
            return ResponseEntity.status(HttpStatus.OK).body( doctorService.getDoctorByEmail(email));
        }catch (EmailNotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getById/{doctorId}")
    public ResponseEntity<Object> getProfileById(@PathVariable("doctorId") String doctorId){
        try {
            DoctorProfileDto userProfile = doctorService.getDoctorDataById(doctorId);
            return ResponseEntity.status(HttpStatus.OK).body(userProfile);
        }catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/getAllDoctor")
    public ResponseEntity<Object> getAllDoctor(){
        try {
            List<DoctorDto> doctorProfile = doctorService.getAllDoctor();
            return ResponseEntity.status(HttpStatus.OK).body(doctorProfile);
        }catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/getAllAvailableDoctor")
    public ResponseEntity<Object> getDoctorByAvailability(){
        try {
            List<DoctorDto> userProfile = doctorService.getDoctorsByIsAvailable();
            return ResponseEntity.status(HttpStatus.OK).body(userProfile);
        }catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/getAllDoctorByDepartment/{department}")
    public ResponseEntity<Object> getDoctorByDepartment(@PathVariable("department") String department){
        try {
            List<DoctorDto> userProfile = doctorService.getDoctorsByDepartment(department);
            return ResponseEntity.status(HttpStatus.OK).body(userProfile);
        }catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PostMapping("/createSlots")
    public ResponseEntity<Object> createAppointmentSlot(@RequestBody SlotRequestDTO slotRequestDTO
            , BindingResult bindingResult){
        ResponseEntity<Object> errors = bindingService.getBindingError(bindingResult);
        if (errors != null) return errors;
        return ResponseEntity.status(HttpStatus.OK).body(doctorService.createSlotsFromDTO(slotRequestDTO));
    }
    @GetMapping("/getAllSlots/{doctorId}/{date}")
    public ResponseEntity<Object> getAllSlots(@PathVariable("doctorId") String doctorId, @PathVariable("date") LocalDate date){
        try {
            List<SlotResponseDTO> responseDTOS = doctorService.getAllSlots(doctorId,date );
            return ResponseEntity.status(HttpStatus.OK).body(responseDTOS);
        }catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/getAllAvailableSlotDoctor/{doctorId}/{date}")
    public ResponseEntity<Object> getDoctorByDepartment(@PathVariable("doctorId") String doctorId, @PathVariable("date") LocalDate date){
        try {
            List<SlotResponseDTO> responseDTOS = doctorService.getAllAvailableSlotsByDoctorId(doctorId,date );
            return ResponseEntity.status(HttpStatus.OK).body(responseDTOS);
        }catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/getAllBookedSlot/{doctorId}/{date}")
    public ResponseEntity<Object> getAllBookedSlot(@PathVariable("doctorId") String doctorId, @PathVariable("date") LocalDate date){
        try {
            List<SlotResponseDTO> responseDTOS = doctorService.getAllBookedSlotsByDoctorId(doctorId,date );
            return ResponseEntity.status(HttpStatus.OK).body(responseDTOS);
        }catch (AuthenticationExceptionFound e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PostMapping("/bookAppointment/{slotId}")
    public ResponseEntity<?> bookSlot(@PathVariable("slotId") String slotId){
        try {
            return ResponseEntity.status(HttpStatus.OK).body( doctorService.bookSlot(slotId));
        }catch (AlreadyExistsException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage());
        }
    }
    @GetMapping("/totalDoctor")
    public ResponseEntity<String> getTotalDoctorCount() {
        long totalDoctorCount = doctorService.getTotalDoctor();
        return new ResponseEntity<>("Total Number Of Doctor : " +totalDoctorCount, HttpStatus.OK);
    }
    @GetMapping("/allDepartments")
    public ResponseEntity<List<String>> findAllDepartments() {
        List<String> departments = doctorService.findAllDepartments();
        return ResponseEntity.ok(departments);
    }
}