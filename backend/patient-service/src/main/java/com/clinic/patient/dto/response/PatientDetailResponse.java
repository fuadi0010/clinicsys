package com.clinic.patient.dto.response;

import java.time.LocalDate;

import com.clinic.patient.entity.BloodType;
import com.clinic.patient.entity.Gender;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientDetailResponse {

    private Long id;
    private Long userId;
    private String medicalRecordNumber;
    private String fullName;
    private Gender gender;
    private LocalDate birthDate;
    private BloodType bloodType;
    private String phoneNumber;
    private String address;
    private String identityNumber;
    private String insuranceNumber;
    private String allergyNotes;
    private String medicalNotes;
    private String profileImageUrl;
    private Boolean active;
}
