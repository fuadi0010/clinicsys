package com.clinic.medical_record.medicalrecord.mapper;

import java.util.List;

import com.clinic.medical_record.medicalrecord.dto.request.CreateMedicalRecordRequest;
import com.clinic.medical_record.medicalrecord.dto.request.UpdateMedicalRecordRequest;
import com.clinic.medical_record.medicalrecord.dto.response.DoctorInternalResponse;
import com.clinic.medical_record.medicalrecord.dto.response.MedicalRecordResponse;
import com.clinic.medical_record.medicalrecord.dto.response.PatientInternalResponse;
import com.clinic.medical_record.medicalrecord.entity.MedicalRecordEntity;

import com.clinic.medical_record.medicalrecord.dto.response.AppointmentInternalResponse;

public interface MedicalRecordMapper {
    MedicalRecordEntity toEntity(CreateMedicalRecordRequest request, AppointmentInternalResponse appointment,
            DoctorInternalResponse doctor, PatientInternalResponse patient);

    MedicalRecordResponse toResponse(MedicalRecordEntity entity);

    List<MedicalRecordResponse> toResponses(List<MedicalRecordEntity> entities);

    void updateEntity(MedicalRecordEntity entity, UpdateMedicalRecordRequest request);
}
