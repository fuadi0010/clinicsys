package com.clinic.auth.user.mapper.implement;

import org.springframework.stereotype.Component;

import com.clinic.auth.user.dto.response.UserResponse;
import com.clinic.auth.user.entity.UsersEntity;
import com.clinic.auth.user.mapper.UserMapper;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserResponse toResponse(
            UsersEntity user) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole()
                .getRoleName())
                .build();
    }
}
