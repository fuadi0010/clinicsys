package com.clinic.auth.auth.mapper.implement;

import org.springframework.stereotype.Component;

import com.clinic.auth.auth.dto.RegisterReponse;
import com.clinic.auth.auth.dto.RegisterRequest;
import com.clinic.auth.auth.mapper.AuthMapper;
import com.clinic.auth.user.entity.UsersEntity;

@Component
public class AuthMapperImpl implements AuthMapper{
    
    @Override
    public UsersEntity toEntity(RegisterRequest request){
        return UsersEntity.builder()
                .username(request.getUsernameRequest())
                .email(request.getEmailRequest())
                .password(request.getPasswordRequest())
                .build();
    }
    @Override
    public RegisterReponse toResponse(UsersEntity user){
        return RegisterReponse.builder()
                .id(user.getId())
                .usernameReponse(user.getUsername())
                .emailReponse(user.getEmail())
                .build();
    }
}
