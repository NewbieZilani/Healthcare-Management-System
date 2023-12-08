package com.moin.DoctorProfile.service.serviceImplementation;

import com.moin.DoctorProfile.dto.UserDto;
import com.moin.DoctorProfile.dto.request.AppointmentRequestDto;
import com.moin.DoctorProfile.dto.response.AppointmentResponseDto;
import com.moin.DoctorProfile.dto.response.SlotResponseDTO;
import com.moin.DoctorProfile.entity.AppointmentEntity;
import com.moin.DoctorProfile.entity.DoctorEntity;
import com.moin.DoctorProfile.entity.SlotEntity;
import com.moin.DoctorProfile.exceptions.CustomException;
import com.moin.DoctorProfile.exceptions.SlotIsBookedException;
import com.moin.DoctorProfile.feignclient.UserClient;
import com.moin.DoctorProfile.repository.AppointmentRepository;
import com.moin.DoctorProfile.repository.DoctorRepository;
import com.moin.DoctorProfile.repository.SlotRepository;
import com.moin.DoctorProfile.service.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private DoctorServiceImplementation doctorService;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserClient userClient;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Override
    public AppointmentResponseDto createAppointment(String slotId,String appointmentType) throws SlotIsBookedException, Exception {
        //if any appointment present with the date, time and patientId
        UserDto user = userClient.getCurrentUserProfile();
        if(user == null) {
            throw new CustomException("You are not authorized to appoint");
        }
        String patientId = user.getUserID();
        //String patientName = user.getName();
        Optional<SlotEntity> slotEntity = slotRepository.findBySlotId(slotId);
        String doctorId = slotEntity.get().getDoctorId();
        Optional<AppointmentEntity> appointmentEntity = appointmentRepository.findByPatientIdAndDoctorIdAndAppointmentDate(patientId,doctorId, slotEntity.get().getSlotDate());
        if(appointmentEntity.isPresent())
            throw new CustomException("One day you can book appointment from same doctor max one time");
        Optional<AppointmentEntity> appointmentEntity1 = appointmentRepository.findBySlotId(slotId);
        if(appointmentEntity1.isPresent())
            throw new CustomException("The slot is already booked by someone else");
        if (appointmentRepository.findByPatientIdAndAndAppointmentDateAndStartTime(
                user.getUserID(), slotEntity.get().getSlotDate(),
                slotEntity.get().getStartTime()).isEmpty()) {
            //if the slot is been booked already
            SlotResponseDTO slotResponseDTO = doctorService.isSlotBooked(slotId);
            if (slotResponseDTO != null) {
                ModelMapper mapper = new ModelMapper();
                SlotEntity slot = mapper.map(slotResponseDTO, SlotEntity.class);
                doctorService.bookSlot(slot.getSlotId());
                AppointmentEntity appointment = new AppointmentEntity();
                appointment.setAppointmentId(UUID.randomUUID().toString());
                appointment.setPatientId(patientId);
                appointment.setDoctorId(slotEntity.get().getDoctorId());
                appointment.setSlotId(slotId);
                appointment.setAppointmentType(appointmentType);
//                if(!Objects.equals(appointmentType, "OFFLINE")) {
//                    appointment.setConferenceLink(requestDto.getConferenceLink());
//                }
//                else {
//                    appointment.setConferenceLink(null);
//                }
                appointment.setAppointmentDate(slotResponseDTO.getSlotDate());
                appointment.setStartTime(slot.getStartTime());
                appointment.setStatus("CREATED");
                appointment.setCreatedAt(LocalDateTime.now());
                appointmentRepository.save(appointment);
                Optional<DoctorEntity> doctor = doctorRepository.findById(appointment.getDoctorId());
                AppointmentResponseDto appointmentResponseDto = new AppointmentResponseDto();
                appointmentResponseDto.setAppointmentId(appointment.getAppointmentId());
                appointmentResponseDto.setPatientName(user.getName());
                appointmentResponseDto.setAppointmentTime(appointment.getStartTime());
                appointmentResponseDto.setAppointmentDate(appointment.getAppointmentDate());
                appointmentResponseDto.setAppointmentType(appointment.getAppointmentType());
                appointmentResponseDto.setDoctorName(doctor.get().getName());
                return appointmentResponseDto;
            } else throw new Exception("The slot is already booked by someone else");
        } else throw new CustomException("Unable to Book an Appointment.");
    }
    @Override
    public AppointmentResponseDto cancelSlotBooking(String appointmentId) throws CustomException {
        UserDto user = userClient.getCurrentUserProfile();
        Optional<AppointmentEntity> appointment = appointmentRepository.findByAppointmentIdAndPatientId(appointmentId,user.getUserID());
        SlotEntity slotEntity = slotRepository.findBySlotId(appointment.get().getSlotId()).get();
        if (appointment.isPresent()) {
            if (appointment.get().getAppointmentDate().isEqual(LocalDate.now())
                    && appointment.get().getStartTime().isBefore(LocalTime.now()))
                throw new CustomException("You can not decline the appointment! date time exceed!");
            appointment.get().setStatus("CANCELED");
            appointmentRepository.save(appointment.get());
            slotEntity.setIsAvailable(true);
            slotRepository.save(slotEntity);
            return new ModelMapper().map(appointment, AppointmentResponseDto.class);
        } else throw new CustomException("Unable to cancel the appointment");
    }

    @Override
    public AppointmentResponseDto completeAppointment(String appointmentId) {
        Optional<AppointmentEntity> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            if (!appointment.get().getStatus().equals("CANCELED")) {
                appointment.get().setStatus("COMPLETED");
                appointmentRepository.save(appointment.get());
                return new ModelMapper().map(appointment, AppointmentResponseDto.class);
            } else
                throw new CustomException("Appointment is failed to be complete. Possible reason status is null or CANCELED");
        } else throw new CustomException("Unable to cancel the appointment");
    }
    @Override
    public List<AppointmentResponseDto> getAllCurrentAppointments(LocalDate date) {
            //if any appointment present with the date, time and patientId
            UserDto user = userClient.getCurrentUserProfile();
            if(user == null) {
                throw new CustomException("You are not authorized");
            }
            String patientId = user.getUserID();
            String patientName = user.getName();
        List<AppointmentEntity> getAppointments = appointmentRepository.findByPatientIdAndAppointmentDateAndStatus(patientId,date,"CREATED");

        if (getAppointments != null) {
            List<AppointmentResponseDto> appointmentResponseDto = new ArrayList<>();
            ModelMapper modelMapper = new ModelMapper();
            for (AppointmentEntity appointmentEntity : getAppointments) {
                Optional<DoctorEntity> doctor = doctorRepository.findById(appointmentEntity.getDoctorId());
                    AppointmentResponseDto responseDto = modelMapper.map(appointmentEntity, AppointmentResponseDto.class);
                    responseDto.setDoctorName(doctor.get().getName());
                    responseDto.setPatientName(patientName);
                    appointmentResponseDto.add(responseDto);
            }
            return appointmentResponseDto;
        } else {
            throw new CustomException("No appointment found on the current date");
        }
    }
    @Override
    public List<AppointmentResponseDto> alUpcomingPatientAppointments() {
        UserDto user = userClient.getCurrentUserProfile();
        if(user == null) {
            throw new CustomException("You are not authorized");
        }
        String patientId = user.getUserID();
        LocalDate currentDate = LocalDate.now();

        List<AppointmentEntity> upcomingAppointments = appointmentRepository
                .findByPatientIdAndAppointmentDateAfterOrderByAppointmentDateAscStartTimeAsc(patientId, currentDate);
        if (upcomingAppointments.isEmpty())
            throw new CustomException("No upcoming appointment is present.");
        else {
            List<AppointmentResponseDto> appointmentResponseDto= new ArrayList<>();
            ModelMapper modelMapper = new ModelMapper();
            for (AppointmentEntity appointmentEntity : upcomingAppointments) {
                Optional<DoctorEntity> doctor = doctorRepository.findById(appointmentEntity.getDoctorId());
                if (appointmentEntity.getStatus().equals("CREATED")) {
                    AppointmentResponseDto responseDto = modelMapper.map(appointmentEntity, AppointmentResponseDto.class);
                    responseDto.setDoctorName(doctor.get().getName());
                    responseDto.setPatientName(user.getName());
                    appointmentResponseDto.add(responseDto);
                }
            }
            return appointmentResponseDto;
        }
    }
    @Override
    public List<AppointmentResponseDto> patientAppointmentHistory(String patientId) {
        List<AppointmentEntity> appointmentHistory = appointmentRepository
                .findAllByPatientId(patientId);
        if (appointmentHistory == null)
            throw new CustomException("No Patient History is present.");
        else {
            List<AppointmentResponseDto> appointmentResponseDtos = new ArrayList<>();
            ModelMapper modelMapper = new ModelMapper();
            for (AppointmentEntity appointmentEntity : appointmentHistory) {
                AppointmentResponseDto responseDto = modelMapper.map(appointmentEntity, AppointmentResponseDto.class);
                appointmentResponseDtos.add(responseDto);
            }
            return appointmentResponseDtos;
        }
    }
}
