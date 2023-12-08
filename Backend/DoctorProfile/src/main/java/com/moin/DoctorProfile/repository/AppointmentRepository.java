package com.moin.DoctorProfile.repository;

import com.moin.DoctorProfile.dto.request.AppointmentRequestDto;
import com.moin.DoctorProfile.entity.AppointmentEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, String> {
    Optional<AppointmentEntity> findByPatientIdAndAndAppointmentDateAndStartTime(String patientId, LocalDate appointmentDate, LocalTime startTime);
    //@Query("SELECT a FROM AppointmentEntity a WHERE a.patientId = :patientId AND a.appointmentDate = :appointmentDate AND a.status = :status")
    List<AppointmentEntity> findByPatientIdAndAppointmentDateAndStatus(String patientId,
                                                                       LocalDate appointmentDate, String status);
    List<AppointmentEntity> findByPatientIdAndAppointmentDateAfterOrderByAppointmentDateAscStartTimeAsc(String patientId, LocalDate currentDate);
    List<AppointmentEntity> findAllByPatientId(String patientId);
    Optional<AppointmentEntity> findByPatientIdAndDoctorIdAndAppointmentDate(String patientId, String doctorId, LocalDate appointmentDate);

    Optional<AppointmentEntity> findBySlotId(String slotId);
    Optional<AppointmentEntity> findByAppointmentIdAndPatientId(String appointmentId, String userID);
}
