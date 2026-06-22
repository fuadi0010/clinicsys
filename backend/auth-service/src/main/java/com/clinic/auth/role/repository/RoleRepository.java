package com.clinic.auth.role.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.clinic.auth.role.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    public Optional<RoleEntity> findByRoleName(String roleName);
}
