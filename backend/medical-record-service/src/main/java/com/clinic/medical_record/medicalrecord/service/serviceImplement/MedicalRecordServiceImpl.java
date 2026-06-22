package com.clinic.medical_record.medicalrecord.service.serviceImplement;

import java.util.List;


import org.springframework.stereotype.Service;

import com.clinic.medical_record.client.DoctorClient;
import com.clinic.medical_record.client.PatientClient;
import com.clinic.medical_record.common.SecurityUtils;
import com.clinic.medical_record.exception.MedicalRecordNotFoundException;
import com.clinic.medical_record.medicalrecord.dto.request.CreateMedicalRecordRequest;
import com.clinic.medical_record.medicalrecord.dto.request.UpdateMedicalRecordRequest;
import com.clinic.medical_record.medicalrecord.dto.response.DoctorInternalResponse;
import com.clinic.medical_record.medicalrecord.dto.response.MedicalRecordResponse;
import com.clinic.medical_record.medicalrecord.dto.response.PatientInternalResponse;
import com.clinic.medical_record.medicalrecord.entity.MedicalRecordEntity;
import com.clinic.medical_record.medicalrecord.mapper.MedicalRecordMapper;
import com.clinic.medical_record.medicalrecord.repository.MedicalRecordRepository;
import com.clinic.medical_record.medicalrecord.service.MedicalRecordService;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import com.clinic.medical_record.client.AppointmentClient;
import com.clinic.medical_record.medicalrecord.dto.response.AppointmentInternalResponse;
import com.clinic.medical_record.common.exception.BadRequestException;
import com.clinic.medical_record.common.exception.DuplicateResourceException;
import com.clinic.medical_record.common.exception.ForbiddenException;
import com.clinic.medical_record.common.exception.ResourceNotFoundException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final DoctorClient doctorClient;
    private final PatientClient patientClient;
    private final AppointmentClient appointmentClient;

    @Override
    public MedicalRecordResponse createMedicalRecord(
            CreateMedicalRecordRequest request) {

        Long currentDoctorUserId = getCurrentUserId();

        // 1. Fetch current doctor
        DoctorInternalResponse doctor = doctorClient.findByUserId(
                currentDoctorUserId).getData();
        if (doctor == null) {
            throw new ResourceNotFoundException("Dokter tidak ditemukan");
        }

        // 2. Fetch appointment details internally
        AppointmentInternalResponse appointment;
        try {
            appointment = appointmentClient.getAppointmentByIdInternal(request.getAppointmentId()).getData();
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Janji temu tidak ditemukan");
        }
        if (appointment == null) {
            throw new ResourceNotFoundException("Janji temu tidak ditemukan");
        }

        // 3. Validasi Bisnis & Keamanan Medis
        // A. Pastikan dokter yang login adalah dokter yang terjadwal
        if (!appointment.getDoctorUserId().equals(doctor.getUserId())) {
            throw new ForbiddenException("Anda tidak memiliki akses untuk membuat rekam medis pada janji temu ini");
        }

        // B. Pastikan status janji temu tidak dibatalkan (CANCELLED)
        if ("CANCELLED".equalsIgnoreCase(appointment.getStatus())) {
            throw new BadRequestException("Tidak dapat membuat rekam medis untuk janji temu yang telah dibatalkan");
        }

        // C. Pastikan idempotensi (1-to-1 relationship)
        if (medicalRecordRepository.existsByAppointmentId(request.getAppointmentId())) {
            throw new DuplicateResourceException("Rekam medis untuk janji temu ini sudah terdaftar");
        }

        // 4. Fetch patient details
        PatientInternalResponse patient = patientClient.findPatientById(
                appointment.getPatientId()).getData();
        if (patient == null) {
            throw new ResourceNotFoundException("Pasien tidak ditemukan");
        }

        // 5. Map & Save Medical Record
        MedicalRecordEntity medicalRecord = medicalRecordMapper.toEntity(
                request,
                appointment,
                doctor,
                patient);

        medicalRecord = medicalRecordRepository.save(
                medicalRecord);

        // 6. Otomatis update status janji temu menjadi COMPLETED
        try {
            appointmentClient.updateAppointmentStatus(appointment.getId(), Map.of("status", "COMPLETED"));
        } catch (Exception ex) {
            // Log warning but don't fail transaction as saving medical record is more critical
            System.err.println("Gagal memperbarui status janji temu menjadi COMPLETED: " + ex.getMessage());
        }

        return medicalRecordMapper.toResponse(
                medicalRecord);
    }

    @Override
    public List<MedicalRecordResponse> getMyMedicalRecordsAsDoctor() {
        Long currentDoctorUserId = getCurrentUserId();
        List<MedicalRecordEntity> records = medicalRecordRepository.findAllByDoctorUserId(currentDoctorUserId);
        return medicalRecordMapper.toResponses(records);
    }

    @Override
    public List<MedicalRecordResponse> getMyMedicalRecordsAsPatient() {
        Long currentPatientUserId = getCurrentUserId();
        List<MedicalRecordEntity> records = medicalRecordRepository.findAllByPatientUserId(currentPatientUserId);
        return medicalRecordMapper.toResponses(records);
    }

    @Override
    public MedicalRecordResponse updateMedicalRecord(Long id, UpdateMedicalRecordRequest request) {
        Long currentDoctorUserId = getCurrentUserId();
        MedicalRecordEntity medicalRecord = medicalRecordRepository.findByIdAndDoctorUserId(id, currentDoctorUserId)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found"));
        medicalRecordMapper.updateEntity(medicalRecord, request);
        medicalRecord = medicalRecordRepository.save(medicalRecord);
        return medicalRecordMapper.toResponse(medicalRecord);
    }

    @Override
    public List<MedicalRecordResponse> getMedicalRecordsByPatientId(Long patientId) {
        List<MedicalRecordEntity> records = medicalRecordRepository.findAllByPatientId(patientId);
        return medicalRecordMapper.toResponses(records);
    }

    @Override
    public MedicalRecordResponse getMedicalRecordById(Long id) {
        MedicalRecordEntity record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found"));
        return medicalRecordMapper.toResponse(record);
    }

    private Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }
}
