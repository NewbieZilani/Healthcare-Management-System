package com.moin.DoctorProfile.service;

import com.moin.DoctorProfile.dto.AvailableSlotRequestDto;
import com.moin.DoctorProfile.dto.request.RegistrationRequestDTO;
import com.moin.DoctorProfile.dto.request.SlotRequestDTO;
import com.moin.DoctorProfile.dto.request.UpdateRequestDto;
import com.moin.DoctorProfile.dto.response.RegistrationResponseDTO;
import com.moin.DoctorProfile.dto.response.SlotResponseDTO;
import com.moin.DoctorProfile.dto.response.ValidationResponseDTO;
import com.moin.DoctorProfile.entity.DoctorEntity;
import com.moin.DoctorProfile.exceptions.CustomException;
import com.moin.DoctorProfile.exceptions.EmailNotFoundException;
import com.moin.DoctorProfile.dto.DoctorDto;
import com.moin.DoctorProfile.dto.DoctorProfileDto;
import com.moin.DoctorProfile.exceptions.SlotIsBookedException;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {
    RegistrationResponseDTO createDoctor(RegistrationRequestDTO user) throws Exception;
    public DoctorProfileDto updateDoctor(UpdateRequestDto updateDoctor);
    DoctorDto getDoctorByEmail(String email) throws EmailNotFoundException;
    DoctorProfileDto getDoctorDataById(String id);
    DoctorEntity readByEmail(String email);
    ValidationResponseDTO verifyDoctor(String doctor_id);
    ValidationResponseDTO disableDoctor(String doctorId);
    //Boolean changeStatus(String doctorId);
    List<DoctorDto> getDoctorsByIsAvailable();
    List<DoctorDto> getDoctorsByDepartment(String department);
    List<SlotResponseDTO> createSlotsFromDTO(SlotRequestDTO slotDTO) throws CustomException;
    SlotResponseDTO isSlotBooked(String slotId) throws SlotIsBookedException;
    //List<SlotResponseDTO> getAllAvailableSlotsByDoctorId(AvailableSlotRequestDto requestDto);
    SlotResponseDTO bookSlot(String slotId);
    SlotResponseDTO CancelBookingSlot(String slotId);
    SlotResponseDTO getSlotBySlot_id(String slot_id);
    List<SlotResponseDTO> getAllAvailableSlotsByDoctorId(String doctorId, LocalDate date);
    long getTotalDoctor();
    List<DoctorDto> getAllDoctor();

    List<SlotResponseDTO> getAllSlots(String doctorId, LocalDate date);
    List<SlotResponseDTO> getAllBookedSlotsByDoctorId(String doctorId, LocalDate date);
    List<String> findAllDepartments();
}
