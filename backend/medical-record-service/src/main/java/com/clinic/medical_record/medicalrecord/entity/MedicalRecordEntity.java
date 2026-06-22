package com.clinic.medical_record.medicalrecord.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records", indexes = {
    @jakarta.persistence.Index(name = "idx_medical_record_patient_id", columnList = "patientId"),
    @jakarta.persistence.Index(name = "idx_medical_record_patient_user_id", columnList = "patientUserId"),
    @jakarta.persistence.Index(name = "idx_medical_record_doctor_id", columnList = "doctorId"),
    @jakarta.persistence.Index(name = "idx_medical_record_doctor_user_id", columnList = "doctorUserId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long patientId;
    @Column(nullable = false)
    private Long patientUserId;
    @Column(nullable = false)
    private Long doctorId;
    @Column(nullable = false)
    private Long doctorUserId;
    @Column(nullable = false)
    private Long appointmentId;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String complaint;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnosis;
    @Column(columnDefinition = "TEXT")
    private String treatment;
    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private LocalDateTime examinationDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        this.examinationDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
