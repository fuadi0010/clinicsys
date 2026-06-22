package com.clinic.auth.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUsersRequet {
    @NotBlank
    private String username;
    @Email
    private String email;
}
