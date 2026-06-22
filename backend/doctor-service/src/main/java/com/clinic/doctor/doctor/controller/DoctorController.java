package com.clinic.doctor.doctor.controller;

import com.clinic.doctor.doctor.dto.request.CreateDoctorRequest;
import com.clinic.doctor.doctor.dto.request.UpdateDoctorRequest;
import com.clinic.doctor.doctor.dto.response.CreateDoctorResponse;
import com.clinic.doctor.doctor.dto.response.DoctorDetailResponse;
import com.clinic.doctor.doctor.dto.response.DoctorInternalResponse;
import com.clinic.doctor.doctor.dto.response.DoctorListResponse;
import com.clinic.doctor.doctor.service.DoctorService;
import com.clinic.doctor.common.apiresponse.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

        private final DoctorService doctorService;

        @PostMapping("/create")
        public ResponseEntity<ApiResponse<CreateDoctorResponse>> createDoctor(
                        @Valid @RequestBody CreateDoctorRequest request) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("Doctor created successfully", doctorService.createDoctor(request)));
        }

        @GetMapping("/me")
        public ResponseEntity<ApiResponse<DoctorDetailResponse>> getMyProfile() {
                return ResponseEntity.ok(ApiResponse.success(doctorService.getMyProfile()));
        }

        @PatchMapping("/me")
        public ResponseEntity<ApiResponse<DoctorDetailResponse>> updateMyProfile(
                        @Valid @RequestBody UpdateDoctorRequest request) {
                return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", doctorService.updateMyProfile(request)));
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<DoctorListResponse>>> getAllDoctors() {
                return ResponseEntity.ok(ApiResponse.success(doctorService.getAllDoctors()));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<DoctorDetailResponse>> getDoctorById(@PathVariable Long id) {
                return ResponseEntity.ok(ApiResponse.success(doctorService.getDoctorById(id)));
        }

        @GetMapping("/internal/{doctorId}")
        public ResponseEntity<ApiResponse<DoctorInternalResponse>> findDoctorInternal(@PathVariable Long doctorId) {
                return ResponseEntity.ok(ApiResponse.success(doctorService.findDoctorInternal(doctorId)));
        }

        @GetMapping("/internal/user/{userId}")
        public ResponseEntity<ApiResponse<DoctorInternalResponse>> findByUserId(@PathVariable Long userId) {
                return ResponseEntity.ok(ApiResponse.success(doctorService.findByUserId(userId)));
        }

        @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<String>> uploadProfileImage(@RequestParam("file") MultipartFile file) throws IOException {
                return ResponseEntity.ok(ApiResponse.success("Profile image uploaded successfully", doctorService.uploadProfileImage(file)));
        }

        @PostMapping(value = "/admin/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<DoctorDetailResponse>> createDoctorByAdmin(
                        @RequestPart("request") @Valid com.clinic.doctor.doctor.dto.request.AdminCreateDoctorRequest request,
                        @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("Doctor created successfully by admin", doctorService.createDoctorByAdmin(request, file)));
        }

        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/count")
        public ResponseEntity<ApiResponse<Long>> countDoctors() {
                return ResponseEntity.ok(ApiResponse.success(doctorService.countDoctors()));
        }

        @org.springframework.web.bind.annotation.PutMapping("/internal/{doctorId}/rating")
        public ResponseEntity<ApiResponse<Void>> updateDoctorRating(
                        @PathVariable Long doctorId,
                        @RequestParam Double averageRating) {
                doctorService.updateDoctorRating(doctorId, averageRating);
                return ResponseEntity.ok(ApiResponse.success("Doctor rating updated successfully", null));
        }
}
