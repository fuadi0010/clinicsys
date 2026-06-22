package com.clinic.doctor.doctor.dto.response;

import com.clinic.doctor.doctor.enumType.DoctorSpecialization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorListResponse {
    private Long id;
    private Long userId;

    private String fullName;

    private DoctorSpecialization specialization;

    private String profileImageUrl;

    private java.math.BigDecimal consultationFee;

    private Double averageRating;
}
