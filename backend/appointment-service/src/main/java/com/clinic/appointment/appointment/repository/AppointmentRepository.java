package com.clinic.appointment.appointment.repository;

import com.clinic.appointment.appointment.entity.AppointmentEntity;
import com.clinic.appointment.appointment.enumType.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findAllByPatientUserId(Long patientUserId);

    List<AppointmentEntity> findAllByDoctorUserId(Long doctorUserId);
    List<AppointmentEntity> findAllByDoctorUserIdAndStatusIn(Long doctorUserId, List<AppointmentStatus> statuses);

    List<AppointmentEntity> findAllByPatientId(Long patientId);

    List<AppointmentEntity> findAllByDoctorId(Long doctorId);
    List<AppointmentEntity> findAllByDoctorIdAndStatusIn(Long doctorId, List<AppointmentStatus> statuses);

    Optional<AppointmentEntity> findByIdAndPatientUserId(Long id, Long patientUserId);

    Optional<AppointmentEntity> findByIdAndDoctorUserId(Long id, Long doctorUserId);

    List<AppointmentEntity> findByStatusAndReminderSent(
            AppointmentStatus status,
            Boolean reminderSent);

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
            Long doctorId, LocalDate date, LocalTime time, AppointmentStatus status);

    List<AppointmentEntity> findByDoctorIdAndAppointmentDateAndStatusNot(
            Long doctorId, LocalDate date, AppointmentStatus status);

    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE AppointmentEntity a SET a.status = :newStatus WHERE a.appointmentDate < :date AND a.status IN :currentStatuses")
    int updateStatusForPastAppointments(
            @org.springframework.data.repository.query.Param("newStatus") AppointmentStatus newStatus,
            @org.springframework.data.repository.query.Param("date") LocalDate date,
            @org.springframework.data.repository.query.Param("currentStatuses") List<AppointmentStatus> currentStatuses);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(a) FROM AppointmentEntity a WHERE a.status IN :statuses")
    long countByStatusIn(@org.springframework.data.repository.query.Param("statuses") List<AppointmentStatus> statuses);
}
