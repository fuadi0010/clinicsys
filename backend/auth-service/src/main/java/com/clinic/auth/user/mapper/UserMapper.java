package com.clinic.auth.user.mapper;

import com.clinic.auth.user.dto.response.UserResponse;
import com.clinic.auth.user.entity.UsersEntity;

public interface UserMapper {
    UserResponse toResponse(UsersEntity user);
}
