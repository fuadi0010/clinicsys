package com.clinic.appointment.appointment.dto.response;

import lombok.Data;

@Data
public class PatientInternalResponse {
    private Long id;
    private Long userId;
    private String fullName;
}
