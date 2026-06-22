package com.clinic.medical_record.medicalrecord.dto.request;

import lombok.Data;

@Data
public class UpdateMedicalRecordRequest {
    private String complaint;
    private String diagnosis;
    private String treatment;
    private String notes;
}
