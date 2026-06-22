package com.clinic.patient.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clinic.patient.dto.request.CreatePatientRequest;
import com.clinic.patient.dto.request.UpdatePatientRequest;
import com.clinic.patient.dto.response.PatientDetailResponse;
import com.clinic.patient.dto.response.PatientInternalResponse;
import com.clinic.patient.dto.response.PatientResponse;
import com.clinic.patient.service.PatientService;
import com.clinic.patient.common.apiresponse.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(@Valid @RequestBody CreatePatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Patient created successfully", patientService.createPatient(request)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PatientDetailResponse>> getMyProfile() {
        return ResponseEntity.ok(ApiResponse.success(patientService.getMyProfile()));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<PatientDetailResponse>> updateMyProfile(@Valid @RequestBody UpdatePatientRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", patientService.updateMyProfile(request)));
    }

    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadProfileImage(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(ApiResponse.success("Profile image uploaded successfully", patientService.uploadProfileImage(file)));
    }

    // Admin & Receptionist endpoints
    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAllPatients() {
        return ResponseEntity.ok(ApiResponse.success(patientService.getAllPatients()));
    }

    @GetMapping("/rm/{medicalRecordNumber}")
    public ResponseEntity<ApiResponse<PatientDetailResponse>> getPatientByMedicalRecordNumber(
            @PathVariable String medicalRecordNumber) {
        return ResponseEntity.ok(ApiResponse.success(patientService.getPatientByMedicalRecordNumber(medicalRecordNumber)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientDetailResponse>> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(patientService.getPatientById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.success("Patient deleted successfully", null));
    }

    @GetMapping("/internal/user/{userId}")
    public ResponseEntity<ApiResponse<PatientInternalResponse>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(patientService.findByUserId(userId)));
    }

    @GetMapping("/internal/{patientId}")
    public ResponseEntity<ApiResponse<PatientInternalResponse>> findPatientInternal(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.success(patientService.findPatientInternal(patientId)));
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countPatients() {
        return ResponseEntity.ok(ApiResponse.success(patientService.countPatients()));
    }
}
