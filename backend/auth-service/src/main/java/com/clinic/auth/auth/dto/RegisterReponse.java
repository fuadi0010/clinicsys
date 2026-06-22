package com.clinic.auth.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterReponse {
    private Long id;
    private String usernameReponse;
    private String emailReponse;
}
