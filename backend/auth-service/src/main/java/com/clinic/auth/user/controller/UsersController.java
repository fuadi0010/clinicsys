package com.clinic.auth.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.auth.user.dto.request.UpdateUsersRequet;
import com.clinic.auth.user.dto.response.UserResponse;
import com.clinic.auth.user.service.UsersService;
import com.clinic.auth.common.apiresponse.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile() {
        return ResponseEntity.ok(ApiResponse.success(userService.getMyProfile()));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyProfile(@Valid @RequestBody UpdateUsersRequet request) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", userService.updateMyProfile(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.findAllUsers()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.findUserById(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/count/admins")
    public ResponseEntity<ApiResponse<Long>> countAdmins() {
        return ResponseEntity.ok(ApiResponse.success(userService.countAdmins()));
    }
}
