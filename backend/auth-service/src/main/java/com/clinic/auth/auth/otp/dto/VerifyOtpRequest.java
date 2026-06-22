package com.clinic.auth.auth.otp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String otp;
}
