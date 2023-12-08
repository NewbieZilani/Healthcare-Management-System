package com.moin.PharmacyInventory.controller;

import com.moin.PharmacyInventory.dto.MedicineDto;
import com.moin.PharmacyInventory.exceptions.ResourceNotFoundException;
import com.moin.PharmacyInventory.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicines")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;

    @PostMapping("/add")
    public ResponseEntity<?> addMedicine(@RequestBody MedicineDto medicineDto) {
        medicineService.addMedicine(medicineDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Medicine added Successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MedicineDto> updateMedicine(@PathVariable int id, @RequestBody MedicineDto medicineDto) {
      MedicineDto updateMedicine =   medicineService.updateMedicine(id, medicineDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateMedicine);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMedicine(@PathVariable int id) {
            medicineService.deleteMedicineById(id);
            return new ResponseEntity<>("Medicine deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/allMedicine")
    public ResponseEntity<List<MedicineDto>> getAllMedicines() {
        List<MedicineDto> medicines = medicineService.getAllMedicines();
        return new ResponseEntity<>(medicines, HttpStatus.OK);
    }

    @GetMapping("/medicineById/{id}")
    public ResponseEntity<MedicineDto> getMedicineById(@PathVariable int id) {
        MedicineDto medicineDto = medicineService.getMedicineById(id);
            return new ResponseEntity<>(medicineDto, HttpStatus.OK);

    }

    @GetMapping("/byName/{medicineName}")
    public ResponseEntity<?> getMedicineByName(@PathVariable("medicineName") String medicineName) {
        try {
            List<MedicineDto> medicineDto = medicineService.getMedicineByName(medicineName);
            return ResponseEntity.status(HttpStatus.OK).body(medicineDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving medicines by name");
        }
    }
    @GetMapping("/expiringInNext7Days")
    public ResponseEntity<?> getMedicinesExpiringInNext7Days() {
        try {
            List<MedicineDto> expiringMedicines = medicineService.getMedicinesExpiringInNext7Days();

            if (!expiringMedicines.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(expiringMedicines);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("No medicines will expire in the next 7 days");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving expiring medicines");
        }
    }
}