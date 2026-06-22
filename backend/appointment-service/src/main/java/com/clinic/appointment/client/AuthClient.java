package com.clinic.appointment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clinic.appointment.appointment.dto.response.UserInternalResponse;
import com.clinic.appointment.common.apiresponse.ApiResponse;

@FeignClient(name = "auth-service",url = "${auth.service.url}")
public interface AuthClient {
    @GetMapping("/auth/internal/users/{id}")
    ApiResponse<UserInternalResponse> findById(
            @PathVariable Long id
    );
}
