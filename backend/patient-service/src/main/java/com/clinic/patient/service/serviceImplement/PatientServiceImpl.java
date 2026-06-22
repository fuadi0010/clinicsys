package com.clinic.patient.service.serviceImplement;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clinic.patient.common.SecurityUtils;
import com.clinic.patient.exception.PatientAlreadyExistsException;
import com.clinic.patient.exception.PatientNotFoundException;
import com.clinic.patient.common.exception.BadRequestException;
import com.clinic.patient.dto.request.CreatePatientRequest;
import com.clinic.patient.dto.request.UpdatePatientRequest;
import com.clinic.patient.dto.response.PatientDetailResponse;
import com.clinic.patient.dto.response.PatientInternalResponse;
import com.clinic.patient.dto.response.PatientResponse;
import com.clinic.patient.entity.PatientEntity;
import com.clinic.patient.mapper.PatientMapper;
import com.clinic.patient.repository.PatientRepository;
import com.clinic.patient.service.PatientService;
import com.clinic.patient.storage.FileStorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final FileStorageService fileStorageService;
    private final org.springframework.web.client.RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Value("${internal.security.token:SecretInternalToken123}")
    private String internalToken;

    @Override
    public PatientResponse createPatient(CreatePatientRequest request) {
        Long userId = getCurrentUserId();

        if (patientRepository.existsByUserId(userId)) {
            throw new PatientAlreadyExistsException("Patient already exists");
        }

        if (request.getIdentityNumber() != null && patientRepository.existsByIdentityNumber(request.getIdentityNumber())) {
            throw new PatientAlreadyExistsException("Nomor NIK sudah terdaftar");
        }

        if (request.getPhoneNumber() != null) {
            request.setPhoneNumber(formatPhoneNumber(request.getPhoneNumber()));
        }

        PatientEntity patient = patientMapper.toEntity(request);
        patient.setUserId(userId);

        // Generate unique medical record number
        patient.setMedicalRecordNumber(generateMedicalRecordNumber());

        patient = patientRepository.save(patient);
        return patientMapper.toResponse(patient);
    }

    @Override
    public PatientDetailResponse getMyProfile() {
        Long userId = getCurrentUserId();
        PatientEntity patient = patientRepository
                .findByUserId(userId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        return patientMapper.toDetailResponse(patient);
    }

    @Override
    public PatientDetailResponse updateMyProfile(UpdatePatientRequest request) {
        Long currentUserId = getCurrentUserId();
        PatientEntity patient = patientRepository
                .findByUserId(currentUserId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        if (request.getGender() != null && patient.getGender() != null && !patient.getGender().equals(request.getGender())) {
            throw new BadRequestException("Perubahan jenis kelamin memerlukan persetujuan Admin");
        }

        if (request.getBirthDate() != null && patient.getBirthDate() != null && !patient.getBirthDate().equals(request.getBirthDate())) {
            throw new BadRequestException("Perubahan tanggal lahir memerlukan persetujuan Admin");
        }

        if (request.getPhoneNumber() != null) {
            request.setPhoneNumber(formatPhoneNumber(request.getPhoneNumber()));
        }

        patientMapper.updateEntity(patient, request);
        patient = patientRepository.save(patient);
        return patientMapper.toDetailResponse(patient);
    }

    private String formatPhoneNumber(String rawPhone) {
        if (rawPhone == null || rawPhone.trim().isEmpty()) {
            return null;
        }
        String clean = rawPhone.replaceAll("[^0-9]", "");
        if (clean.startsWith("0")) {
            clean = clean.substring(1);
        } else if (clean.startsWith("62")) {
            clean = clean.substring(2);
        }
        if (!clean.matches("^8[0-9]{8,12}$")) {
            throw new BadRequestException("Format nomor telepon tidak valid. Harus diawali dengan 08 atau 8 dan memiliki panjang 10-13 digit angka.");
        }
        String p1 = clean.substring(0, 3);
        String p2 = clean.substring(3, Math.min(7, clean.length()));
        String p3 = clean.length() > 7 ? clean.substring(7) : "";
        
        StringBuilder formatted = new StringBuilder("+62 ");
        formatted.append(p1).append(" ").append(p2);
        if (!p3.isEmpty()) {
            formatted.append(" ").append(p3);
        }
        return formatted.toString();
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDetailResponse getPatientByMedicalRecordNumber(String medicalRecordNumber) {
        PatientEntity patient = patientRepository.findByMedicalRecordNumber(medicalRecordNumber)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        return patientMapper.toDetailResponse(patient);
    }

    @Override
    public PatientDetailResponse getPatientById(Long id) {
        PatientEntity patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        return patientMapper.toDetailResponse(patient);
    }

    @Override
    public void deletePatient(Long id) {
        PatientEntity patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        
        // Soft delete pasien
        patient.setActive(false);
        patientRepository.save(patient);

        // Deaktivasi user di auth-service secara internal
        try {
            String deactivateUrl = "http://localhost:8081/auth/internal/users/" + patient.getUserId() + "/deactivate";
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("X-Internal-Token", internalToken);
            org.springframework.http.HttpEntity<Void> requestEntity = new org.springframework.http.HttpEntity<>(headers);
            restTemplate.exchange(deactivateUrl, org.springframework.http.HttpMethod.PUT, requestEntity, Void.class);
        } catch (Exception e) {
            throw new com.clinic.patient.common.exception.BusinessException("Gagal menonaktifkan kredensial login pasien: " + e.getMessage());
        }
    }

    private String generateMedicalRecordNumber() {
        java.time.LocalDate now = java.time.LocalDate.now();
        String datePart = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String number;
        do {
            int randomNum = (int) (Math.random() * 10000);
            number = String.format("RM-%s-%04d", datePart, randomNum);
        } while (patientRepository.existsByMedicalRecordNumber(number));
        return number;
    }

    @Override
    public PatientInternalResponse findByUserId(Long userId) {
        PatientEntity patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        return PatientInternalResponse.builder()
                .id(patient.getId())
                .userId(patient.getUserId())
                .fullName(patient.getFullName())
                .build();
    }

    @Override
    public PatientInternalResponse findPatientInternal(Long patientId) {
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        return PatientInternalResponse.builder()
                .id(patient.getId())
                .userId(patient.getUserId())
                .fullName(patient.getFullName())
                .build();
    }

    @Override
    public String uploadProfileImage(MultipartFile file) throws IOException {
        Long userId = getCurrentUserId();
        PatientEntity patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Mohon lengkapi data diri Anda terlebih dahulu sebelum mengunggah foto profil"));
        
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Only image allowed");
        }

        String imageUrl = fileStorageService.saveFile(file);
        patient.setProfileImageUrl(imageUrl);
        patientRepository.save(patient);
        return imageUrl;
    }

    private Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    @Override
    public long countPatients() {
        return patientRepository.count();
    }
}
