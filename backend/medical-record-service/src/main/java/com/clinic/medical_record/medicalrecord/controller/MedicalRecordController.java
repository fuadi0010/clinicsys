package com.clinic.medical_record.medicalrecord.controller;

import com.clinic.medical_record.medicalrecord.dto.request.CreateMedicalRecordRequest;
import com.clinic.medical_record.medicalrecord.dto.request.UpdateMedicalRecordRequest;
import com.clinic.medical_record.medicalrecord.dto.response.MedicalRecordResponse;
import com.clinic.medical_record.medicalrecord.service.MedicalRecordService;
import com.clinic.medical_record.common.apiresponse.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> createMedicalRecord(
            @Valid @RequestBody CreateMedicalRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Medical record created successfully", medicalRecordService.createMedicalRecord(request)));
    }

    @GetMapping("/doctor/me")
    public ResponseEntity<ApiResponse<List<MedicalRecordResponse>>> getMyMedicalRecordsAsDoctor() {
        return ResponseEntity.ok(ApiResponse.success(medicalRecordService.getMyMedicalRecordsAsDoctor()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<MedicalRecordResponse>>> getMyMedicalRecordsAsPatient() {
        return ResponseEntity.ok(ApiResponse.success(medicalRecordService.getMyMedicalRecordsAsPatient()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> updateMedicalRecord(@PathVariable Long id,
            @RequestBody UpdateMedicalRecordRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Medical record updated successfully", medicalRecordService.updateMedicalRecord(id, request)));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<MedicalRecordResponse>>> getMedicalRecordsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.success(medicalRecordService.getMedicalRecordsByPatientId(patientId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> getMedicalRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(medicalRecordService.getMedicalRecordById(id)));
    }
}
