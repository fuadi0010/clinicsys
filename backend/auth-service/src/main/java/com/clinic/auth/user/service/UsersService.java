package com.clinic.auth.user.service;

import java.util.List;

import com.clinic.auth.user.dto.request.UpdateUsersRequet;
import com.clinic.auth.user.dto.response.UserResponse;

public interface UsersService {
    UserResponse getMyProfile();
    UserResponse updateMyProfile(UpdateUsersRequet request);

    List<UserResponse> findAllUsers();
    UserResponse findUserById(Long id);
    void deleteUser(Long id);
    long countAdmins();
}
