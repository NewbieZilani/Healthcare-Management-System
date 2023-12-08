package com.moin.PharmacyInventory.service;

import com.moin.PharmacyInventory.dto.MedicineDto;

import java.util.List;

public interface MedicineService {
    void addMedicine(MedicineDto medicineDto);
    MedicineDto updateMedicine(int medicineId, MedicineDto medicineDto);
    void deleteMedicineById(int medicineId);
    List<MedicineDto> getAllMedicines();
    MedicineDto getMedicineById(int id);
    List<MedicineDto> getMedicineByName(String medicineName);
    List<MedicineDto> getMedicinesExpiringInNext7Days();

}