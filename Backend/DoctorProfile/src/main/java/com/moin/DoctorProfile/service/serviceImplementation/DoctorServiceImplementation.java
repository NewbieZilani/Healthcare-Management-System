package com.moin.DoctorProfile.service.serviceImplementation;


import com.moin.DoctorProfile.dto.AvailableSlotRequestDto;
import com.moin.DoctorProfile.dto.DoctorDto;
import com.moin.DoctorProfile.dto.DoctorProfileDto;
import com.moin.DoctorProfile.dto.UserDto;
import com.moin.DoctorProfile.dto.request.SlotRequestDTO;
import com.moin.DoctorProfile.dto.request.RegistrationRequestDTO;
import com.moin.DoctorProfile.dto.request.UpdateRequestDto;
import com.moin.DoctorProfile.dto.response.SlotResponseDTO;
import com.moin.DoctorProfile.dto.response.RegistrationResponseDTO;
import com.moin.DoctorProfile.dto.response.ValidationResponseDTO;
import com.moin.DoctorProfile.entity.DoctorEntity;
import com.moin.DoctorProfile.entity.SlotEntity;
import com.moin.DoctorProfile.exceptions.*;
import com.moin.DoctorProfile.feignclient.UserClient;
import com.moin.DoctorProfile.repository.DoctorRepository;
import com.moin.DoctorProfile.repository.SlotRepository;
import com.moin.DoctorProfile.service.DoctorService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImplementation implements DoctorService, UserDetailsService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private UserClient userClient;

    @Override
    public RegistrationResponseDTO createDoctor(RegistrationRequestDTO doctor) {
        ModelMapper modelMapper = new ModelMapper();
        if (doctorRepository.findByEmail(doctor.getEmail()).isPresent())
            throw new AlreadyExistsException("Email already exists");
        if (doctorRepository.findByAllocatedRoom(doctor.getAllocated_room()).isPresent())
            throw new AlreadyExistsException("Room is already booked by someone");
        if (doctorRepository.findByRegistrationNumberBdmc(doctor.getRegistration_number_BDMC()).isPresent())
            throw new AlreadyExistsException("BDMC registration number already exist");
        DoctorEntity doctorProfile = new DoctorEntity();
        doctorProfile.setDoctor_id(UUID.randomUUID().toString());
        doctorProfile.setName(doctor.getName());
        doctorProfile.setEmail(doctor.getEmail());
        doctorProfile.setPassword(bCryptPasswordEncoder.encode(doctor.getPassword()));
        doctorProfile.setRole("DOCTOR");
        doctorProfile.setGender(doctor.getGender());
        doctorProfile.setDepartment(doctor.getDepartment());
        doctorProfile.setRegistration_number_BDMC(doctor.getRegistration_number_BDMC());
        doctorProfile.setAllocated_room(doctor.getAllocated_room());
        doctorProfile.setQualifications(doctor.getQualifications());
        doctorProfile.setIsValid(false);
        doctorProfile.setIsAvailable(false);
//      doctorProfile.setSlots(null);
        doctorProfile.setCreated_at(LocalDate.now());
        doctorProfile.setImage(doctor.getImage());
        DoctorEntity storedUserDetails = doctorRepository.save(doctorProfile);
        return modelMapper.map(storedUserDetails, RegistrationResponseDTO.class);
    }

    @Override
    public DoctorDto getDoctorByEmail(String email) throws EmailNotFoundException {
        if (doctorRepository.findByEmail(email).isEmpty())
            throw new EmailNotFoundException("No Doctor is found by this email");
        DoctorEntity doctorEntity = doctorRepository.findByEmail(email).get();
        DoctorDto returnValue = new DoctorDto();
        returnValue.setDoctor_id(doctorEntity.getDoctor_id());
        BeanUtils.copyProperties(doctorEntity, returnValue);
        return returnValue;
    }

    @Override
    public DoctorEntity readByEmail(String email) {
        return doctorRepository.findByEmail(email).get();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (doctorRepository.findByEmail(email).isEmpty())
            System.out.println("No user Found");
        DoctorEntity doctorEntity = doctorRepository.findByEmail(email).get();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(doctorEntity.getRole()));
        System.out.println("Role: " + roles);
        if (doctorEntity == null) throw new UsernameNotFoundException(email);
        return new User(doctorEntity.getEmail(), doctorEntity.getPassword(),
                true, true, true, true,
                roles);
    }

    @Override
    public DoctorProfileDto getDoctorDataById(String doctor_id) {
        Optional<DoctorEntity> doctorProfile = doctorRepository.findById(doctor_id);
        if (doctorProfile.isEmpty()) {
            System.out.println("No user found khalid\n\n\n\n");
        }

        if (doctorProfile.isPresent()) {
            DoctorEntity doctorProfileEntity = doctorProfile.get();
            return new ModelMapper().map(doctorProfileEntity, DoctorProfileDto.class);
        } else {
            throw new ResourceNotFoundException("User profile not found by id");
        }
    }

    @Override
    public DoctorProfileDto updateDoctor(UpdateRequestDto updateDoctor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<DoctorEntity> user = doctorRepository.findByEmail(authentication.getName());
        if (user.isEmpty())
            throw new UsernameNotFoundException("No user found");
        String userId = user.get().getDoctor_id();

        DoctorEntity existingDoctorProfile = doctorRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Profile Not Found For User ID: " + userId));

//        System.out.println("Doctor ID: " + existingDoctorProfile.getDoctor_id());
//        System.out.println("Qualifications: " + existingDoctorProfile.getQualifications());

//        List<String> qualifications = new ArrayList<>(existingDoctorProfile.getQualifications());
//        System.out.println("Qualification clone list: " + qualifications);
//
//        qualifications.addAll(updateDoctor.getQualifications());
        existingDoctorProfile.setName(updateDoctor.getName());
        existingDoctorProfile.setGender(updateDoctor.getGender());
        existingDoctorProfile.setDepartment(updateDoctor.getDepartment());
        existingDoctorProfile.setRegistration_number_BDMC(updateDoctor.getRegistration_number_BDMC());
        existingDoctorProfile.setQualifications(existingDoctorProfile.getQualifications());

        System.out.println("Doctor ID: " + existingDoctorProfile.getDoctor_id());
        System.out.println("Qualifications: " + existingDoctorProfile.getQualifications());

        doctorRepository.save(existingDoctorProfile);
        return new ModelMapper().map(existingDoctorProfile, DoctorProfileDto.class);
    }

    @Override
    public ValidationResponseDTO verifyDoctor(String doctor_id) {
        Optional<DoctorEntity> doctorProfile = doctorRepository.findById(doctor_id);
        if (doctorProfile.isPresent()) {
            DoctorEntity doctorProfileEntity = doctorProfile.get();
            if (doctorProfileEntity.getIsValid())
                throw new AlreadyExistsException("The Doctor is Already Verified");
            doctorProfileEntity.setIsValid(true);
            doctorProfileEntity.setIsAvailable(true);
            doctorRepository.save(doctorProfileEntity);
            return new ValidationResponseDTO(doctorProfileEntity.getIsValid(), "The doctor is Successfully Verified");
        } else {
            throw new ResourceNotFoundException("Unable to verify doctor of doctor_id: "
                    + doctorProfile.get().getDoctor_id());
        }
    }

    @Override
    public ValidationResponseDTO disableDoctor(String doctorId) {
        Optional<DoctorEntity> doctorProfile = doctorRepository.findById(doctorId);
        if (doctorProfile.isPresent()) {
            DoctorEntity doctorProfileEntity = doctorProfile.get();
            if (!doctorProfileEntity.getIsValid())
                throw new AlreadyExistsException("The Doctor is Already been Disable");
            doctorProfileEntity.setIsValid(false);
            doctorProfileEntity.setIsAvailable(false);
            doctorRepository.save(doctorProfileEntity);
            return new ValidationResponseDTO(doctorProfileEntity.getIsValid(), "The doctor has been disabled");
        } else {
            throw new ResourceNotFoundException("Unable to disable doctor with  doctorId: " + doctorProfile.get().getDoctor_id());
        }
    }

//    @Override
//    public Boolean changeStatus(String doctorId) {
//        Optional<DoctorEntity> doctorProfile = doctorRepository.findById(doctorId);
//        if (doctorProfile.isPresent()) {
//            DoctorEntity doctorProfileEntity = doctorProfile.get();
//            if (doctorProfileEntity.getIsAvailable()) {
//                doctorProfileEntity.setIsAvailable(false);
//            } else doctorProfileEntity.setIsAvailable(true);
//            doctorRepository.save(doctorProfileEntity);
//            return true;
//        } else {
//            throw new ResourceNotFoundException("Unable to change the status of the doctor");
//        }
//    }

    @Override
    public List<DoctorDto> getAllDoctor() {
        List<DoctorEntity> doctorEntityList = doctorRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();
        return doctorEntityList.stream()
                .map(doctorEntity -> modelMapper.map(doctorEntity, DoctorDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorDto> getDoctorsByIsAvailable() {
        List<DoctorEntity> doctorEntityList = doctorRepository.findAllByIsAvailableIsTrue();
        if (doctorEntityList != null) {
            ModelMapper modelMapper = new ModelMapper();
            List<DoctorDto> doctorDtoList = doctorEntityList.stream()
                    .map(doctorEntity -> modelMapper.map(doctorEntity, DoctorDto.class))
                    .collect(Collectors.toList());
            return doctorDtoList;
        } else {
            throw new ResourceNotFoundException("Unable to Find Any Doctor");
        }
    }

    @Override
    public List<DoctorDto> getDoctorsByDepartment(String department) {
        List<DoctorEntity> doctorEntityList = doctorRepository.findAllByDepartment(department);
        if (doctorEntityList != null) {
            ModelMapper modelMapper = new ModelMapper();
            List<DoctorDto> doctorDtoList = doctorEntityList.stream()
                    .map(doctorEntity -> modelMapper.map(doctorEntity, DoctorDto.class))
                    .collect(Collectors.toList());
            return doctorDtoList;
        } else {
            throw new ResourceNotFoundException("Unable to Find Any Doctor");
        }
    }

    @Override
    public List<SlotResponseDTO> createSlotsFromDTO(SlotRequestDTO slotDTO) throws CustomException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<DoctorEntity> doctor = doctorRepository.findByEmail(authentication.getName());
        if (slotDTO.getSlotDate().isBefore(LocalDate.now()))
            throw new CustomException("Invalid Request! Can not create appointment on previous date");
        if(!doctor.get().getIsValid())
        {
            throw new CustomException("Still you are not validated so wait for sometime");
        }
        List<SlotEntity> slots = slotRepository.findAvailableSlotsByDoctorIdAndDate(doctor.get().getDoctor_id(), slotDTO.getSlotDate());
        if (slots.isEmpty()) {

            LocalTime startTime = slotDTO.getStartTime();
            LocalTime finishTime = startTime.plusMinutes(slotDTO.getDuration());
            List<SlotResponseDTO> appointmentSlots = new ArrayList<>();
            ModelMapper modelMapper = new ModelMapper();
            SlotEntity slot = new SlotEntity();
            // Creating slots with a fixed duration of 20 minutes
            while (startTime.isBefore(finishTime)) {
                slot.setSlotId(UUID.randomUUID().toString());
                slot.setDoctorId(doctor.get().getDoctor_id());
                slot.setStartTime(startTime);
                slot.setEndTime(startTime.plusMinutes(20));
                slot.setIsAvailable(true);
                slot.setSlotDate(slotDTO.getSlotDate());
                slotRepository.save(slot);
                appointmentSlots.add(modelMapper.map(slot, SlotResponseDTO.class));
                startTime = startTime.plusMinutes(20);
            }
            return appointmentSlots;
        } else {
            throw new CustomException("Doctor Appointment Slot can not be created on this date:" + slotDTO.getSlotDate() + ". \n Already existed slot for the date!");
        }
    }

    @Override
    public SlotResponseDTO isSlotBooked(String slotId) throws SlotIsBookedException {
        Optional<SlotEntity> checkSlotAvailability = slotRepository.findById(slotId);
        if (checkSlotAvailability.get().getIsAvailable())
            return new ModelMapper().map(
                    checkSlotAvailability.get(),
                    SlotResponseDTO.class);
        else throw new SlotIsBookedException("The slot is already been Booked");
    }

    @Override
    public SlotResponseDTO getSlotBySlot_id(String slot_id) {
        SlotEntity slot = slotRepository.findById(slot_id).get();
        if (slot.getIsAvailable()) {
            return new ModelMapper().map(slot, SlotResponseDTO.class);
        } else throw new CustomException("Slot is booked");
    }

    @Override
    public List<SlotResponseDTO> getAllSlots(String doctorId, LocalDate date) {
        List<SlotEntity> availableSlots = slotRepository.findByDoctorIdAndSlotDate(doctorId, date);
        if (availableSlots.isEmpty()) {
            throw new CustomException("No available slot is found");
        } else {
            return availableSlots.stream()
                    .map(this::mapToSlotResponseDTO)
                    .collect(Collectors.toList());
        }
    }

    private SlotResponseDTO mapToSlotResponseDTO(SlotEntity slotEntity) {
        return new SlotResponseDTO(
                slotEntity.getSlotId(),
                slotEntity.getDoctorId(),
                slotEntity.getStartTime(),
                slotEntity.getEndTime(),
                slotEntity.getIsAvailable(),
                slotEntity.getSlotDate()
        );
    }


    @Override
    public List<SlotResponseDTO> getAllAvailableSlotsByDoctorId(String doctorId, LocalDate date) {
        List<SlotEntity> availableSlots = slotRepository
                .findSlotEntitiesByDoctorIdAndSlotDateOrderByEndTime(doctorId, date).get();
        if (availableSlots.isEmpty()) {
            throw new CustomException("No available slot is found");
        } else {
            ModelMapper mapper = new ModelMapper();
            List<SlotResponseDTO> dtoList = new ArrayList<>();
            for (SlotEntity slotEntity : availableSlots) {
                SlotResponseDTO dto = mapper.map(slotEntity, SlotResponseDTO.class);
                if (slotEntity.getIsAvailable())
                    dtoList.add(dto);
            }
            return dtoList;
        }
    }

    @Override
    public List<SlotResponseDTO> getAllBookedSlotsByDoctorId(String doctorId, LocalDate date) {
        List<SlotEntity> bookedSlots = slotRepository
                .findByDoctorIdAndSlotDateAndIsAvailable(doctorId, date, false);
        if (bookedSlots.isEmpty()) {
            throw new CustomException("No Booked appointment is found");
        } else {
            ModelMapper mapper = new ModelMapper();
            List<SlotResponseDTO> dtoList = new ArrayList<>();
            for (SlotEntity slotEntity : bookedSlots) {
                SlotResponseDTO dto = mapper.map(slotEntity, SlotResponseDTO.class);
                    dtoList.add(dto);
            }
            return dtoList;
        }
    }


    @Override
    public SlotResponseDTO bookSlot(String slotId) {
        UserDto user = userClient.getCurrentUserProfile();
        if (user == null) {
            throw new CustomException("You are not authorized to appoint");
        }
        String patientId = user.getUserID();
        Optional<SlotEntity> checkSlot = slotRepository.findById(slotId);
        if (checkSlot.isEmpty())
            throw new CustomException("Unable to find Slot!");
        else {
            SlotEntity slot = checkSlot.get();
            if (!slot.getIsAvailable())
                throw new CustomException("Slot has already been booked");
            slot.setIsAvailable(false);
            //slot.setPatientId(patientId);
            slotRepository.save(slot);
            return new ModelMapper().map(slot, SlotResponseDTO.class);
        }
    }

    @Override
    public SlotResponseDTO CancelBookingSlot(String slotId) {
        Optional<SlotEntity> checkSlot = slotRepository.findById(slotId);
        if (checkSlot.isEmpty())
            throw new CustomException("Unable to find Slot!");
        else {
            SlotEntity slot = checkSlot.get();
            if (slot.getIsAvailable())
                throw new CustomException("Slot is still available");
            slot.setIsAvailable(true);
            slotRepository.save(slot);
            return new ModelMapper().map(slot, SlotResponseDTO.class);
        }
    }
    @Override
    public long getTotalDoctor() {
        return doctorRepository.countBy();
    }
    @Override
    public List<String> findAllDepartments() {
        return doctorRepository.findAllDepartments();
    }
}