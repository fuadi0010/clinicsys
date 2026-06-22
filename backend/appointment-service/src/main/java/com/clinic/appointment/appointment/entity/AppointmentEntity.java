package com.clinic.appointment.appointment.entity;

import com.clinic.appointment.appointment.enumType.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointments", indexes = {
    @jakarta.persistence.Index(name = "idx_appointment_patient_id", columnList = "patientId"),
    @jakarta.persistence.Index(name = "idx_appointment_patient_user_id", columnList = "patientUserId"),
    @jakarta.persistence.Index(name = "idx_appointment_doctor_id", columnList = "doctorId"),
    @jakarta.persistence.Index(name = "idx_appointment_doctor_user_id", columnList = "doctorUserId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private Long patientUserId;

    private Long doctorId;
    private Long doctorUserId;

    private String patientEmail;

    private String doctorName;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Builder.Default
    private Boolean reminderSent = false;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = AppointmentStatus.PENDING;
        }

        this.reminderSent = false;
    }
}
