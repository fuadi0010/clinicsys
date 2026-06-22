package com.clinic.doctor.doctor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clinic.doctor.doctor.entity.DoctorEntity;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long>{
    public Boolean existsByUserId(Long request);
    public Boolean existsByStrNumber(String request);
    Optional<DoctorEntity>findByUserId(Long userId);
    
}
