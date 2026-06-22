package com.clinic.appointment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinic.appointment.appointment.dto.response.DoctorInternalResponse;
import com.clinic.appointment.common.apiresponse.ApiResponse;

@FeignClient(name = "doctor-service", url = "${doctor.service.url:http://localhost:8083}")
public interface DoctorClient {

    @GetMapping("/doctors/internal/{doctorId}")
    ApiResponse<DoctorInternalResponse> findDoctorById(@PathVariable("doctorId") Long doctorId);
}
