package com.clinic.patient.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.clinic.patient.dto.request.CreatePatientRequest;
import com.clinic.patient.dto.request.UpdatePatientRequest;
import com.clinic.patient.dto.response.PatientDetailResponse;
import com.clinic.patient.dto.response.PatientInternalResponse;
import com.clinic.patient.dto.response.PatientResponse;

public interface PatientService {
    PatientResponse createPatient(CreatePatientRequest request);
    PatientDetailResponse getMyProfile();
    PatientDetailResponse updateMyProfile(UpdatePatientRequest request);
    java.util.List<PatientResponse> getAllPatients();
    PatientDetailResponse getPatientByMedicalRecordNumber(String medicalRecordNumber);
    PatientDetailResponse getPatientById(Long id);
    void deletePatient(Long id);
    PatientInternalResponse findByUserId( Long userId );
    PatientInternalResponse findPatientInternal( Long patientId );
    String uploadProfileImage( MultipartFile file ) throws IOException;
    long countPatients();
}
