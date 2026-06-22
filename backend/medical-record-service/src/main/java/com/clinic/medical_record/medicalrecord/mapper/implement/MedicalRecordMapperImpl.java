package com.clinic.medical_record.medicalrecord.mapper.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.clinic.medical_record.medicalrecord.dto.request.CreateMedicalRecordRequest;
import com.clinic.medical_record.medicalrecord.dto.request.UpdateMedicalRecordRequest;
import com.clinic.medical_record.medicalrecord.dto.response.DoctorInternalResponse;
import com.clinic.medical_record.medicalrecord.dto.response.MedicalRecordResponse;
import com.clinic.medical_record.medicalrecord.dto.response.PatientInternalResponse;
import com.clinic.medical_record.medicalrecord.entity.MedicalRecordEntity;
import com.clinic.medical_record.medicalrecord.mapper.MedicalRecordMapper;

import com.clinic.medical_record.medicalrecord.dto.response.AppointmentInternalResponse;

@Component
public class MedicalRecordMapperImpl implements MedicalRecordMapper {
    @Override
    public MedicalRecordEntity toEntity(
            CreateMedicalRecordRequest request,
            AppointmentInternalResponse appointment,
            DoctorInternalResponse doctor,
            PatientInternalResponse patient) {

        MedicalRecordEntity medicalRecord = new MedicalRecordEntity();

        medicalRecord.setAppointmentId(
                appointment.getId());

        medicalRecord.setComplaint(
                request.getComplaint());

        medicalRecord.setDiagnosis(
                request.getDiagnosis());

        medicalRecord.setTreatment(
                request.getTreatment());

        medicalRecord.setNotes(
                request.getNotes());

        medicalRecord.setExaminationDate(
                LocalDateTime.now());

        // DOCTOR
        medicalRecord.setDoctorId(
                doctor.getId());

        medicalRecord.setDoctorUserId(
                doctor.getUserId());

        // PATIENT
        medicalRecord.setPatientId(
                patient.getId());

        medicalRecord.setPatientUserId(
                patient.getUserId());

        return medicalRecord;
    }

    @Override
    public MedicalRecordResponse toResponse(MedicalRecordEntity entity) {
        return MedicalRecordResponse
                .builder()
                .id(entity.getId())
                .patientId(entity.getPatientId())
                .doctorId(entity.getDoctorId())
                .appointmentId(entity.getAppointmentId())
                .complaint(entity.getComplaint())
                .diagnosis(entity.getDiagnosis())
                .treatment(entity.getTreatment())
                .notes(entity.getNotes())
                .examinationDate(entity.getExaminationDate())
                .build();
    }

    @Override
    public List<MedicalRecordResponse> toResponses(List<MedicalRecordEntity> entities) {
        return entities.stream().map(entity -> toResponse(entity)).toList();
    }

    @Override
    public void updateEntity(MedicalRecordEntity entity, UpdateMedicalRecordRequest request) {
        if (request.getComplaint() != null) {
            entity.setComplaint(request.getComplaint());
        }
        if (request.getDiagnosis() != null) {
            entity.setDiagnosis(request.getDiagnosis());
        }
        if (request.getTreatment() != null) {
            entity.setTreatment(request.getTreatment());
        }
        if (request.getNotes() != null) {
            entity.setNotes(request.getNotes());
        }
    }
}
