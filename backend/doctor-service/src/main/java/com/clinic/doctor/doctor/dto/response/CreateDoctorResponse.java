package com.clinic.doctor.doctor.dto.response;

import java.math.BigDecimal;

import com.clinic.doctor.doctor.enumType.DoctorSpecialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDoctorResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private DoctorSpecialization specialization;
    private BigDecimal consultationFee;
}
