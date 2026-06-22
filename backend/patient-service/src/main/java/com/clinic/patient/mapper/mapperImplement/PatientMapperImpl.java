package com.clinic.patient.mapper.mapperImplement;

import org.springframework.stereotype.Component;

import com.clinic.patient.dto.request.CreatePatientRequest;
import com.clinic.patient.dto.request.UpdatePatientRequest;
import com.clinic.patient.dto.response.PatientDetailResponse;
import com.clinic.patient.dto.response.PatientResponse;
import com.clinic.patient.entity.PatientEntity;
import com.clinic.patient.mapper.PatientMapper;

@Component
public class PatientMapperImpl implements PatientMapper {

    @Override
    public PatientEntity toEntity(CreatePatientRequest request) {
        if (request == null) {
            return null;
        }
        String insNum = request.getInsuranceNumber() != null ? request.getInsuranceNumber().trim() : null;
        return PatientEntity.builder()
                .fullName(request.getFullName())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .bloodType(request.getBloodType())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .identityNumber(request.getIdentityNumber())
                .insuranceNumber(insNum == null || insNum.isEmpty() ? null : insNum)
                .allergyNotes(request.getAllergyNotes())
                .medicalNotes(request.getMedicalNotes())
                .active(true)
                .build();
    }

    @Override
    public PatientResponse toResponse(PatientEntity patient) {
        if (patient == null) {
            return null;
        }
        return PatientResponse.builder()
                .id(patient.getId())
                .medicalRecordNumber(patient.getMedicalRecordNumber())
                .fullName(patient.getFullName())
                .gender(patient.getGender())
                .birthDate(patient.getBirthDate())
                .bloodType(patient.getBloodType())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .profileImageUrl(patient.getProfileImageUrl())
                .active(patient.getActive())
                .build();
    }

    @Override
    public PatientDetailResponse toDetailResponse(PatientEntity patient) {
        if (patient == null) {
            return null;
        }
        return PatientDetailResponse.builder()
                .id(patient.getId())
                .userId(patient.getUserId())
                .medicalRecordNumber(patient.getMedicalRecordNumber())
                .fullName(patient.getFullName())
                .gender(patient.getGender())
                .birthDate(patient.getBirthDate())
                .bloodType(patient.getBloodType())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .identityNumber(patient.getIdentityNumber())
                .insuranceNumber(patient.getInsuranceNumber())
                .allergyNotes(patient.getAllergyNotes())
                .medicalNotes(patient.getMedicalNotes())
                .profileImageUrl(patient.getProfileImageUrl())
                .active(patient.getActive())
                .build();
    }

    @Override
    public void updateEntity(PatientEntity patient, UpdatePatientRequest request) {
        if (patient == null || request == null) {
            return;
        }
        if (request.getFullName() != null) {
            patient.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            patient.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            patient.setAddress(request.getAddress());
        }
        if (request.getBloodType() != null) {
            patient.setBloodType(request.getBloodType());
        }
        if (request.getInsuranceNumber() != null) {
            String insNum = request.getInsuranceNumber().trim();
            patient.setInsuranceNumber(insNum.isEmpty() ? null : insNum);
        }
        if (request.getAllergyNotes() != null) {
            patient.setAllergyNotes(request.getAllergyNotes());
        }
        if (request.getMedicalNotes() != null) {
            patient.setMedicalNotes(request.getMedicalNotes());
        }
        if (request.getBirthDate() != null) {
            patient.setBirthDate(request.getBirthDate());
        }
    }
}
