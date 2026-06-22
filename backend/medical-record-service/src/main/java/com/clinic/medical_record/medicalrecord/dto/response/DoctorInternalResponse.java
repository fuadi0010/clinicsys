package com.clinic.medical_record.medicalrecord.dto.response;

import lombok.Data;

@Data
public class DoctorInternalResponse {
    private Long id;
    private Long userId;
    private String fullName;
}
