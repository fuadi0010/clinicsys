package com.clinic.auth.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 5,max = 40)
    private String usernameRequest;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String emailRequest;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100)
    private String passwordRequest;

    private String role = "PATIENT";
}
