package com.clinic.appointment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinic.appointment.appointment.dto.response.PatientInternalResponse;
import com.clinic.appointment.common.apiresponse.ApiResponse;

@FeignClient(name = "patient-service", url = "${patient.service.url:http://localhost:8082}")
public interface PatientClient {

    @GetMapping("/patients/internal/user/{userId}")
    ApiResponse<PatientInternalResponse> findByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/patients/internal/{patientId}")
    ApiResponse<PatientInternalResponse> findPatientInternal(@PathVariable("patientId") Long patientId);
}
