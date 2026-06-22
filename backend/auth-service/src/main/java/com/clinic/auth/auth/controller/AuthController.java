package com.clinic.auth.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.auth.auth.dto.LoginRequest;
import com.clinic.auth.auth.dto.LoginResponse;
import com.clinic.auth.auth.dto.RegisterReponse;
import com.clinic.auth.auth.dto.RegisterRequest;
import com.clinic.auth.auth.dto.RefreshTokenRequest;
import com.clinic.auth.auth.dto.UserInternalResponse;
import com.clinic.auth.auth.otp.dto.ResendOtpRequest;
import com.clinic.auth.auth.otp.dto.VerifyOtpRequest;
import com.clinic.auth.auth.service.AuthService;
import com.clinic.auth.common.apiresponse.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
        private final AuthService authService;

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<RegisterReponse>> register(@Valid @RequestBody RegisterRequest request) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("Register successful", authService.register(request)));
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
                return ResponseEntity.ok(ApiResponse.success("Login successful", authService.login(request)));
        }

        @PostMapping("/verify-otp")
        public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestBody VerifyOtpRequest request) {
                return ResponseEntity.ok(ApiResponse.success("Verification successful", authService.verifyOtp(request)));
        }

        @PostMapping("/resend-otp")
        public ResponseEntity<ApiResponse<String>> resendOtp(@RequestBody ResendOtpRequest request) {
                return ResponseEntity.ok(ApiResponse.success("OTP sent successfully", authService.resendOtp(request)));
        }

        @GetMapping("/internal/users/{id}")
        public ResponseEntity<ApiResponse<UserInternalResponse>> findById(@PathVariable Long id) {
                return ResponseEntity.ok(ApiResponse.success(authService.findById(id)));
        }

        @PostMapping("/internal/register-doctor")
        public ResponseEntity<ApiResponse<UserInternalResponse>> registerDoctorInternal(@Valid @RequestBody RegisterRequest request) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("Doctor user created internally", authService.registerDoctorInternal(request)));
        }

        @org.springframework.web.bind.annotation.DeleteMapping("/internal/users/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteUserInternal(@PathVariable Long id) {
                authService.deleteUserInternal(id);
                return ResponseEntity.ok(ApiResponse.success("User deleted internally", null));
        }

        @org.springframework.web.bind.annotation.PutMapping("/internal/users/{id}/deactivate")
        public ResponseEntity<ApiResponse<Void>> deactivateUserInternal(@PathVariable Long id) {
                authService.deactivateUserInternal(id);
                return ResponseEntity.ok(ApiResponse.success("User deactivated internally", null));
        }

        @PostMapping("/refresh")
        public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
                return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", authService.refreshToken(request.getRefreshToken())));
        }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse<Void>> logout(@RequestBody RefreshTokenRequest request) {
                authService.logout(request.getRefreshToken());
                return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
        }
}
