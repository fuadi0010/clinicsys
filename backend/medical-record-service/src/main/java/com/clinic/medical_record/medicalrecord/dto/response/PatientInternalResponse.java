package com.clinic.medical_record.medicalrecord.dto.response;

import lombok.Data;

@Data
public class PatientInternalResponse {
    private Long id;
    private Long userId;
    private String email;
    private String fullName;
}
