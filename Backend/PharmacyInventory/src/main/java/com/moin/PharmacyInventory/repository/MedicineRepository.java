package com.moin.PharmacyInventory.repository;

import com.moin.PharmacyInventory.entity.MedicineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MedicineRepository extends JpaRepository<MedicineEntity, Integer> {
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MedicineEntity m WHERE m.medicineName = :medicineName")
    boolean existsByMedicineName(@Param("medicineName") String medicineName);

    @Query("SELECT m FROM MedicineEntity m WHERE m.medicineName LIKE %:medicineName%")
    List<MedicineEntity> findByMedicineName(@Param("medicineName") String medicineName);

    // You can add custom query methods if needed
    @Query("SELECT m FROM MedicineEntity m WHERE m.expirationDate BETWEEN :startDate AND :endDate")
    List<MedicineEntity> findByExpirationDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}