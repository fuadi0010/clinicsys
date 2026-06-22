package com.clinic.doctor.doctor.service.serviceImplement;

import java.io.IOException;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clinic.doctor.common.SecurityUtils;
import com.clinic.doctor.exception.DoctorAlreadyExistsException;
import com.clinic.doctor.exception.DoctorNotFoundException;
import com.clinic.doctor.common.exception.BadRequestException;
import com.clinic.doctor.doctor.dto.request.CreateDoctorRequest;
import com.clinic.doctor.doctor.dto.request.UpdateDoctorRequest;
import com.clinic.doctor.doctor.dto.response.CreateDoctorResponse;
import com.clinic.doctor.doctor.dto.response.DoctorDetailResponse;
import com.clinic.doctor.doctor.dto.response.DoctorInternalResponse;
import com.clinic.doctor.doctor.dto.response.DoctorListResponse;
import com.clinic.doctor.doctor.entity.DoctorEntity;
import com.clinic.doctor.doctor.mapper.DoctorMapper;
import com.clinic.doctor.doctor.repository.DoctorRepository;
import com.clinic.doctor.doctor.service.DoctorService;
import com.clinic.doctor.doctor.storage.FileStorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {
        private final DoctorRepository doctorRepository;
        private final DoctorMapper doctorMapper;
        private final FileStorageService fileStorageService;
        private final org.springframework.web.client.RestTemplate restTemplate;

        @org.springframework.beans.factory.annotation.Value("${internal.security.token:SecretInternalToken123}")
        private String internalToken;

        @Override
        public CreateDoctorResponse createDoctor(
                        CreateDoctorRequest request) {

                Long currentUserId = getCurrentUserId();

                if (doctorRepository.existsByUserId(
                                currentUserId)) {

                        throw new DoctorAlreadyExistsException("Doctor already exists");
                }

                if (doctorRepository.existsByStrNumber(
                                request.getStrNumber())) {

                        throw new DoctorAlreadyExistsException("STR number already exists");
                }

                DoctorEntity doctor = doctorMapper.toEntity(
                                request);

                doctor.setUserId(
                                currentUserId);

                doctor = doctorRepository.save(
                                doctor);

                return doctorMapper.toResponse(
                                doctor);
        }

        @Override
        public DoctorDetailResponse getMyProfile() {

                Long currentUserId = getCurrentUserId();

                DoctorEntity doctor = doctorRepository
                                .findByUserId(currentUserId)
                                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

                return doctorMapper.toDetailResponse(doctor);
        }

        @Override
        public DoctorDetailResponse getDoctorById(Long id) {
                DoctorEntity doctor = doctorRepository.findById(id)
                                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));
                return doctorMapper.toDetailResponse(doctor);
        }

        @Override
        public DoctorDetailResponse updateMyProfile(
                        UpdateDoctorRequest request) {

                Long currentUserId = getCurrentUserId();

                DoctorEntity doctor = doctorRepository
                                .findByUserId(currentUserId)
                                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

                doctorMapper.updateEntity(doctor, request);

                doctor = doctorRepository.save(doctor);

                return doctorMapper.toDetailResponse(doctor);
        }

        @Override
        public List<DoctorListResponse> getAllDoctors() {

                List<DoctorEntity> doctors = doctorRepository.findAll();

                return doctorMapper.toListResponses(doctors);
        }

        @Override
        public DoctorInternalResponse findDoctorInternal(Long doctorId) {
                DoctorEntity doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));
                return DoctorInternalResponse.builder()
                                .id(doctor.getId())
                                .userId(doctor.getUserId())
                                .fullName(doctor.getFullName())
                                .specialization(doctor.getSpecialization().name())
                                .averageRating(doctor.getAverageRating())
                                .build();
        }

        @Override
        public DoctorInternalResponse findByUserId(Long userId) {
                DoctorEntity doctor = doctorRepository.findByUserId(userId)
                                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));
                return DoctorInternalResponse.builder()
                                .id(doctor.getId())
                                .userId(doctor.getUserId())
                                .fullName(doctor.getFullName())
                                .specialization(doctor.getSpecialization().name())
                                .averageRating(doctor.getAverageRating())
                                .build();
        }

        @Override
        public String uploadProfileImage(MultipartFile file) throws IOException {
                Long currentUserId = getCurrentUserId();
                DoctorEntity doctor = doctorRepository.findByUserId(currentUserId)
                                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

                if (file.isEmpty()) {
                        throw new BadRequestException("File is empty");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                        throw new BadRequestException("Only image allowed");
                }
                
                String imageUrl = fileStorageService.saveFile(file);
                doctor.setProfileImageUrl(imageUrl);
                doctorRepository.save(doctor);
                return imageUrl;
        }

        private Long getCurrentUserId() {
                return SecurityUtils.getCurrentUserId();
        }

        @Override
        @Transactional
        public DoctorDetailResponse createDoctorByAdmin(com.clinic.doctor.doctor.dto.request.AdminCreateDoctorRequest request, MultipartFile file) throws IOException {
                // 1. Cek duplikasi STR
                if (doctorRepository.existsByStrNumber(request.getStrNumber())) {
                        throw new com.clinic.doctor.exception.DoctorAlreadyExistsException("STR number already exists");
                }

                // 2. Hubungi auth-service
                String authServiceUrl = "http://localhost:8081/auth/internal/register-doctor";
                java.util.Map<String, String> authRequest = new java.util.HashMap<>();
                authRequest.put("usernameRequest", request.getUsername());
                authRequest.put("emailRequest", request.getEmail());
                authRequest.put("passwordRequest", request.getPassword());
                authRequest.put("role", "DOCTOR");

                org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                headers.set("X-Internal-Token", internalToken);
                headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
                org.springframework.http.HttpEntity<java.util.Map<String, String>> entity = new org.springframework.http.HttpEntity<>(authRequest, headers);

                com.clinic.doctor.common.apiresponse.ApiResponseUser responseEntity;
                try {
                        responseEntity = restTemplate.postForObject(authServiceUrl, entity, com.clinic.doctor.common.apiresponse.ApiResponseUser.class);
                } catch (org.springframework.web.client.HttpStatusCodeException ex) {
                        throw new com.clinic.doctor.common.exception.BadRequestException("Gagal mendaftarkan akun dokter: " + ex.getResponseBodyAsString());
                } catch (Exception ex) {
                        throw new com.clinic.doctor.common.exception.BadRequestException("Gagal menghubungi Auth Service: " + ex.getMessage());
                }

                if (responseEntity == null || responseEntity.getData() == null) {
                        throw new com.clinic.doctor.common.exception.BadRequestException("Response dari Auth Service kosong atau tidak valid");
                }

                com.clinic.doctor.doctor.dto.response.UserInternalResponse createdUser = responseEntity.getData();
                Long userId = createdUser.getId();

                String profileImageUrl = null;
                if (file != null && !file.isEmpty()) {
                        String contentType = file.getContentType();
                        if (contentType == null || !contentType.startsWith("image/")) {
                                throw new BadRequestException("Only image allowed");
                        }
                        profileImageUrl = fileStorageService.saveFile(file);
                }

                // 3. Simpan profil dokter ke DB lokal
                DoctorEntity doctor = DoctorEntity.builder()
                                .userId(userId)
                                .fullName(request.getFullName())
                                .specialization(request.getSpecialization())
                                .strNumber(request.getStrNumber())
                                .phoneNumber(request.getPhoneNumber())
                                .consultationFee(request.getConsultationFee())
                                .bio(request.getBio())
                                .profileImageUrl(profileImageUrl)
                                .build();

                try {
                        doctor = doctorRepository.save(doctor);
                } catch (Exception ex) {
                        // Saga kompensasi
                        try {
                                String deleteUserUrl = "http://localhost:8081/auth/internal/users/" + userId;
                                org.springframework.http.HttpHeaders deleteHeaders = new org.springframework.http.HttpHeaders();
                                deleteHeaders.set("X-Internal-Token", internalToken);
                                org.springframework.http.HttpEntity<Void> deleteEntity = new org.springframework.http.HttpEntity<>(deleteHeaders);
                                restTemplate.exchange(deleteUserUrl, org.springframework.http.HttpMethod.DELETE, deleteEntity, Void.class);
                        } catch (Exception e) {
                                System.err.println("Gagal menghapus user kompensasi dengan ID: " + userId + ". Error: " + e.getMessage());
                        }
                        throw ex;
                }

                return doctorMapper.toDetailResponse(doctor);
        }

        @Override
        public long countDoctors() {
                return doctorRepository.count();
        }

        @Override
        @Transactional
        public void updateDoctorRating(Long id, Double averageRating) {
                DoctorEntity doctor = doctorRepository.findById(id)
                                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));
                doctor.setAverageRating(averageRating);
                doctorRepository.save(doctor);
        }

}
