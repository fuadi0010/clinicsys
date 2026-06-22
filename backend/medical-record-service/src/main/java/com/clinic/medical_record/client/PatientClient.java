package com.clinic.medical_record.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinic.medical_record.medicalrecord.dto.response.PatientInternalResponse;
import com.clinic.medical_record.common.apiresponse.ApiResponse;

@FeignClient(name = "patient-service", url = "${patient.service.url:http://localhost:8082}")
public interface PatientClient {

    @GetMapping("/patients/internal/{patientId}")
    ApiResponse<PatientInternalResponse> findPatientById(@PathVariable("patientId") Long patientId);
}
