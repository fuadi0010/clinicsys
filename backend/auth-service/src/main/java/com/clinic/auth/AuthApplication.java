package com.clinic.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.clinic.auth.role.entity.RoleEntity;
import com.clinic.auth.role.repository.RoleRepository;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
	CommandLineRunner initializeRoles(RoleRepository roleRepository) {
		return args -> {
		for (String roleName : new String[] { "PATIENT", "DOCTOR", "ADMIN", "RECEPTIONIST" }) {
				if (roleRepository.findByRoleName(roleName).isEmpty()) {
					RoleEntity role = new RoleEntity();
					role.setRoleName(roleName);
					roleRepository.save(role);
				}
			}
		};
	}
}
