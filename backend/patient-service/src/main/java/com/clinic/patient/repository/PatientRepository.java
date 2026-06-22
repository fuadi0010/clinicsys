package com.clinic.patient.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.patient.entity.PatientEntity;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    Optional<PatientEntity> findByUserId(Long userId);

    Optional<PatientEntity> findByMedicalRecordNumber(String medicalRecordNumber);

    boolean existsByMedicalRecordNumber(String medicalRecordNumber);

    boolean existsByUserId(Long userId);

    boolean existsByIdentityNumber(String identityNumber);

    boolean existsByInsuranceNumber(String insuranceNumber);
}
