package com.clinic.auth.auth.refreshToken.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.auth.auth.refreshToken.entity.RefreshTokenEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByTokenAndRevokedFalse(String token);
    java.util.List<RefreshTokenEntity> findByUserId(Long userId);
}
