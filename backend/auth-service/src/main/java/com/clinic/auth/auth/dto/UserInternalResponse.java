package com.clinic.auth.auth.dto;

import lombok.Data;

@Data
public class UserInternalResponse {
    private Long id;

    private String username;

    private String email;
}
