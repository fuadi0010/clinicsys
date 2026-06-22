package com.clinic.medical_record.medicalrecord.service;

import java.util.List;

import com.clinic.medical_record.medicalrecord.dto.request.CreateMedicalRecordRequest;
import com.clinic.medical_record.medicalrecord.dto.request.UpdateMedicalRecordRequest;
import com.clinic.medical_record.medicalrecord.dto.response.MedicalRecordResponse;

public interface MedicalRecordService {
    MedicalRecordResponse createMedicalRecord(CreateMedicalRecordRequest request);
    List<MedicalRecordResponse> getMyMedicalRecordsAsDoctor();
    List<MedicalRecordResponse> getMyMedicalRecordsAsPatient();
    MedicalRecordResponse updateMedicalRecord(Long id, UpdateMedicalRecordRequest request);
    List<MedicalRecordResponse> getMedicalRecordsByPatientId(Long patientId);
    MedicalRecordResponse getMedicalRecordById(Long id);
}
