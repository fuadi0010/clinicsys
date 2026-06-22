package com.clinic.auth.auth.mapper;

import com.clinic.auth.auth.dto.RegisterReponse;
import com.clinic.auth.auth.dto.RegisterRequest;
import com.clinic.auth.user.entity.UsersEntity;

public interface AuthMapper {
    UsersEntity toEntity(RegisterRequest request);
    RegisterReponse toResponse(UsersEntity users);
}
