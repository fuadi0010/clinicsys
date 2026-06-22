package com.clinic.auth.user.service.servicImplement;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.clinic.auth.exception.UserNotFoundException;
import com.clinic.auth.exception.UserAlreadyExistsException;
import com.clinic.auth.user.dto.request.UpdateUsersRequet;
import com.clinic.auth.user.dto.response.UserResponse;
import com.clinic.auth.user.entity.UsersEntity;
import com.clinic.auth.user.mapper.UserMapper;
import com.clinic.auth.user.repository.UsersRepository;
import com.clinic.auth.user.service.UsersService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getMyProfile() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        UsersEntity user = (UsersEntity) authentication.getPrincipal();

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateMyProfile(UpdateUsersRequet request) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        UsersEntity user = (UsersEntity) authentication.getPrincipal();

        // Validasi tidak ada user lain yang pakai username/email yang sama
        if (!user.getUsername().equals(request.getUsername())
                && usersRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        if (request.getEmail() != null && !user.getEmail().equals(request.getEmail())
                && usersRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        user.setUsername(request.getUsername());

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }

        user = usersRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> findAllUsers() {

        List<UsersEntity> users = usersRepository.findAll();

        return users.stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse findUserById(Long id) {

        UsersEntity user = usersRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {

        UsersEntity user = usersRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        usersRepository.delete(user);
    }

    @Override
    public long countAdmins() {
        return usersRepository.countByRoleRoleName("ADMIN");
    }
}
