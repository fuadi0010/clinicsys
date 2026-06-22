package com.clinic.auth.auth.otp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.clinic.auth.auth.otp.entity.OtpEntity;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findTopByEmailAndUsedFalseOrderByIdDesc(String email);

    List<OtpEntity> findByEmailAndUsedFalse(String email);
}
