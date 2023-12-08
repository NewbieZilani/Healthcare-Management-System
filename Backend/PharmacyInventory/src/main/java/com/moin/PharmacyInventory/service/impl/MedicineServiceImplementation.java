package com.moin.PharmacyInventory.service.impl;

import com.moin.PharmacyInventory.dto.MedicineDto;
import com.moin.PharmacyInventory.entity.MedicineEntity;
import com.moin.PharmacyInventory.exceptions.AlreadyExistsException;
import com.moin.PharmacyInventory.exceptions.ResourceNotFoundException;
import com.moin.PharmacyInventory.repository.MedicineRepository;
import com.moin.PharmacyInventory.service.MedicineService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MedicineServiceImplementation implements MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MedicineServiceImplementation( ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public void addMedicine(MedicineDto medicineDto) {
        if (medicineRepository.existsByMedicineName(medicineDto.getMedicineName()))
            throw new AlreadyExistsException("Medicine Already Exists");
        MedicineEntity medicineEntity = modelMapper.map(medicineDto, MedicineEntity.class);
        medicineRepository.save(medicineEntity);
    }
    @Override
    public MedicineDto updateMedicine(int medicineId, MedicineDto medicineDto) {
        Optional<MedicineEntity> medicine = medicineRepository.findById(medicineId);
        if (medicine.isPresent()) {
            MedicineEntity existingMedicine = medicine.get();
            modelMapper.map(medicineDto, existingMedicine);
            MedicineEntity updatedMedicine = medicineRepository.save(existingMedicine);
            return modelMapper.map(updatedMedicine, MedicineDto.class);
        } else {
            throw new ResourceNotFoundException("Medicine is  not found for medicine ID: " + medicineId);
        }
    }

    @Override
    public void deleteMedicineById(int medicineId) {
        Optional<MedicineEntity> optionalMedicine = medicineRepository.findById(medicineId);

        if (optionalMedicine.isPresent()) {
            MedicineEntity medicineToDelete = optionalMedicine.get();
            medicineRepository.delete(medicineToDelete);
        } else {
            throw new ResourceNotFoundException("Medicine is  not found for medicine ID: " + medicineId);
        }
    }

    public List<MedicineDto> getAllMedicines() {
        List<MedicineEntity> medicineEntities = medicineRepository.findAll();
        return convertToDtoList(medicineEntities);
    }

    private List<MedicineDto> convertToDtoList(List<MedicineEntity> medicineEntities) {
        return medicineEntities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MedicineDto convertToDto(MedicineEntity medicineEntity) {
        // Use a model mapper or manually map fields from the entity to the DTO
        MedicineDto medicineDto = new MedicineDto();
        medicineDto.setMedicineName(medicineEntity.getMedicineName());
        medicineDto.setGeneric(medicineEntity.getGeneric());
        medicineDto.setManufacturer(medicineEntity.getManufacturer());
        medicineDto.setManufacturingDate(medicineEntity.getManufacturingDate());
        medicineDto.setExpirationDate(medicineEntity.getExpirationDate());
        medicineDto.setMedicineType(medicineEntity.getMedicineType());
        medicineDto.setIsAvailable(medicineEntity.getIsAvailable());
        return medicineDto;
    }

    public MedicineDto getMedicineById(int id) {
        Optional<MedicineEntity> optionalMedicine = medicineRepository.findById(id);

        if (optionalMedicine.isPresent()) {
            MedicineEntity medicineEntity = optionalMedicine.get();
            return convertToDto(medicineEntity);
        } else {
            throw new ResourceNotFoundException("Medicine is  not found for medicine ID: " + id);
        }
    }
    public List<MedicineDto> getMedicineByName(String medicineName) {
        List<MedicineEntity> medicineEntities = medicineRepository.findByMedicineName(medicineName);
        if (!medicineEntities.isEmpty()) {
            return medicineEntities
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException("No medicines found for medicine name: " + medicineName);
        }
    }
    public List<MedicineDto> getMedicinesExpiringInNext7Days() {
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDateThreshold = currentDate.plusDays(7);

        List<MedicineEntity> expiringMedicines = medicineRepository.findByExpirationDateBetween(currentDate, expirationDateThreshold);

        return expiringMedicines
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}

