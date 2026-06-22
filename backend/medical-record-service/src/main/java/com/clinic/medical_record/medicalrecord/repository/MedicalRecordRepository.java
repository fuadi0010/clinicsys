package com.clinic.medical_record.medicalrecord.repository;

import com.clinic.medical_record.medicalrecord.entity.MedicalRecordEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecordEntity, Long> {
    List<MedicalRecordEntity> findAllByDoctorUserId(Long doctorUserId);
    List<MedicalRecordEntity> findAllByPatientUserId(Long patientUserId);
    List<MedicalRecordEntity> findAllByPatientId(Long patientId);
    Optional<MedicalRecordEntity> findByIdAndDoctorUserId(Long id, Long doctorUserId);
    boolean existsByAppointmentId(Long appointmentId);
}
