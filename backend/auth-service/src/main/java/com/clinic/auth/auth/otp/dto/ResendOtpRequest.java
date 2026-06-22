package com.clinic.auth.auth.otp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResendOtpRequest {
    private String email;
}
