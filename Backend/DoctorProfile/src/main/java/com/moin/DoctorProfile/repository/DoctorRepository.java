package com.moin.DoctorProfile.repository;


import com.moin.DoctorProfile.entity.DoctorEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, String> {
    Optional<DoctorEntity> findByEmail(String email);
    List<DoctorEntity> findAllByIsAvailableIsTrue();
    List<DoctorEntity> findAllByDepartment(String department);
    long countBy();
    @Query("SELECT d FROM DoctorEntity d WHERE d.allocated_room = :allocatedRoom")
    Optional<DoctorEntity> findByAllocatedRoom(String allocatedRoom);
    @Query("SELECT d FROM DoctorEntity d WHERE d.registration_number_BDMC = :registrationNumberBdmc")
    Optional<DoctorEntity> findByRegistrationNumberBdmc(@Param("registrationNumberBdmc") String registrationNumberBdmc);
    @Query("SELECT DISTINCT department FROM DoctorEntity")
    List<String> findAllDepartments();
}
