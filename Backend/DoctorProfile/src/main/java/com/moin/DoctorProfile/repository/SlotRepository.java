package com.moin.DoctorProfile.repository;

import com.moin.DoctorProfile.entity.SlotEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<SlotEntity, String> {
    Optional<List<SlotEntity>> findSlotEntitiesByDoctorIdAndSlotDateOrderByEndTime(String doctorId, LocalDate slotDate);
    @Query("SELECT s FROM SlotEntity s " +
            "WHERE s.doctorId = :doctorId " +
            "AND s.slotDate = :slotDate " +
            "AND s.isAvailable = true")
    List<SlotEntity> findAvailableSlotsByDoctorIdAndDate(@Param("doctorId") String doctorId, @Param("slotDate") LocalDate slotDate);

    List<SlotEntity> findByDoctorIdAndSlotDate(String doctorId, LocalDate date);

    List<SlotEntity> findByDoctorIdAndSlotDateAndIsAvailable(String doctorId, LocalDate date, Boolean b);

    Optional<SlotEntity> findBySlotId(String slotId);
}