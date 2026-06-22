package com.clinic.patient.mapper;

import com.clinic.patient.dto.request.CreatePatientRequest;
import com.clinic.patient.dto.request.UpdatePatientRequest;
import com.clinic.patient.dto.response.PatientDetailResponse;
import com.clinic.patient.dto.response.PatientResponse;
import com.clinic.patient.entity.PatientEntity;

public interface PatientMapper {
    PatientEntity toEntity(CreatePatientRequest request);
    PatientResponse toResponse(PatientEntity patient);
    PatientDetailResponse toDetailResponse(PatientEntity patient);
    void updateEntity(PatientEntity patient, UpdatePatientRequest request);
}
