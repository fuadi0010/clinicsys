package com.clinic.medical_record.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.clinic.medical_record.medicalrecord.dto.response.AppointmentInternalResponse;
import com.clinic.medical_record.common.apiresponse.ApiResponse;

import java.util.Map;

@FeignClient(name = "appointment-service", url = "${appointment.service.url:http://localhost:8084}", configuration = FeignClientConfig.class)
public interface AppointmentClient {

    @GetMapping("/appointments/internal/{id}")
    ApiResponse<AppointmentInternalResponse> getAppointmentByIdInternal(@PathVariable("id") Long id);

    @PatchMapping("/appointments/{id}/status")
    ApiResponse<Object> updateAppointmentStatus(@PathVariable("id") Long id, @RequestBody Map<String, Object> request);
}
