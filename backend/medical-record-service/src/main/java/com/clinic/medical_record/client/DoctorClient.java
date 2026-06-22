package com.clinic.medical_record.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinic.medical_record.medicalrecord.dto.response.DoctorInternalResponse;
import com.clinic.medical_record.common.apiresponse.ApiResponse;

@FeignClient(name = "doctor-service", url = "${doctor.service.url:http://localhost:8083}")
public interface DoctorClient {

    @GetMapping("/doctors/internal/user/{userId}")
    ApiResponse<DoctorInternalResponse> findByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/doctors/internal/{doctorId}")
    ApiResponse<DoctorInternalResponse> findDoctorById(@PathVariable("doctorId") Long doctorId);
}
