package com.clinic.medical_record.medicalrecord.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMedicalRecordRequest {
    @NotNull
    private Long appointmentId;
    @NotBlank
    @jakarta.validation.constraints.Size(max = 1000, message = "Keluhan maksimal 1000 karakter")
    private String complaint;
    @NotBlank
    @jakarta.validation.constraints.Size(max = 1000, message = "Diagnosis maksimal 1000 karakter")
    private String diagnosis;
    @jakarta.validation.constraints.Size(max = 1000, message = "Tindakan maksimal 1000 karakter")
    private String treatment;
    @jakarta.validation.constraints.Size(max = 1000, message = "Catatan dokter maksimal 1000 karakter")
    private String notes;
}
