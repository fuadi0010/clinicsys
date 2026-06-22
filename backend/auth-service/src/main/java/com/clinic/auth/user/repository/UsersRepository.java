package com.clinic.auth.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.clinic.auth.user.entity.UsersEntity;


@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    public Boolean existsByUsername(String request);
    public Boolean existsByEmail(String request);
    public Optional<UsersEntity> findByUsername(String username);
    public Optional<UsersEntity> findByEmail(String email);
    public long countByRoleRoleName(String roleName);
}
