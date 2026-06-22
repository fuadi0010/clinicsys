package com.clinic.doctor.doctor.dto.response;

import java.math.BigDecimal;

import com.clinic.doctor.doctor.enumType.DoctorSpecialization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorDetailResponse {
    private Long id;
    private Long userId;

    private String fullName;

    private DoctorSpecialization specialization;

    private String strNumber;

    private String phoneNumber;

    private String bio;

    private BigDecimal consultationFee;

    private String profileImageUrl;

    private Double averageRating;
}
