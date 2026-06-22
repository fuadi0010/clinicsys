package com.clinic.appointment.appointment.dto.response;

import lombok.Data;

@Data
public class DoctorInternalResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String specialization;
}
