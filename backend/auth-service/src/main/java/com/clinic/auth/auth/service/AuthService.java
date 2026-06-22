package com.clinic.auth.auth.service;

import com.clinic.auth.auth.dto.LoginRequest;
import com.clinic.auth.auth.dto.LoginResponse;
import com.clinic.auth.auth.dto.RegisterReponse;
import com.clinic.auth.auth.dto.RegisterRequest;
import com.clinic.auth.auth.dto.UserInternalResponse;
import com.clinic.auth.auth.otp.dto.ResendOtpRequest;
import com.clinic.auth.auth.otp.dto.VerifyOtpRequest;

public interface AuthService {
    public RegisterReponse register(RegisterRequest request);
    public LoginResponse login(LoginRequest request);
    public String verifyOtp(VerifyOtpRequest request);
    String resendOtp(ResendOtpRequest request);
    UserInternalResponse findById(Long id);
    LoginResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
    UserInternalResponse registerDoctorInternal(RegisterRequest request);
    void deleteUserInternal(Long id);
    void deactivateUserInternal(Long id);
}
