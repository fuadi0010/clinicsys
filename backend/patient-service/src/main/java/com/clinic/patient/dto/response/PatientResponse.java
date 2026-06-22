package com.clinic.patient.dto.response;

import java.time.LocalDate;

import com.clinic.patient.entity.BloodType;
import com.clinic.patient.entity.Gender;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientResponse {
    private Long id;
    private String medicalRecordNumber;
    private String fullName;
    private Gender gender;
    private LocalDate birthDate;
    private BloodType bloodType;
    private String phoneNumber;
    private String address;
    private String profileImageUrl;
    private Boolean active;
}
