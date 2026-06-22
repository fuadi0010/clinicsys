package com.clinic.medical_record.medicalrecord.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicalRecordResponse {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long appointmentId;
    private String complaint;
    private String diagnosis;
    private String treatment;
    private String notes;
    private LocalDateTime examinationDate;
}
