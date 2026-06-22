package com.clinic.doctor.common.apiresponse;

import com.clinic.doctor.doctor.dto.response.UserInternalResponse;
import lombok.Data;

@Data
public class ApiResponseUser {
    private boolean success;
    private String message;
    private UserInternalResponse data;
}
