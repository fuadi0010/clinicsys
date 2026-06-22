package com.clinic.medical_record.medicalrecord.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentInternalResponse {
    private Long id;
    private Long patientId;
    private Long patientUserId;
    private Long doctorId;
    private Long doctorUserId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private String notes;
}
