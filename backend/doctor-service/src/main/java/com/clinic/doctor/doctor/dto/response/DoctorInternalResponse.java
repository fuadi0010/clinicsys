package com.clinic.doctor.doctor.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorInternalResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String specialization;
    private Double averageRating;
}
