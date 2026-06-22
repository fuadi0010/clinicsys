package com.clinic.medical_record.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clinic.medical_record.security.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // INTERNAL ENDPOINT
                        .requestMatchers(
                                "/medical-records/internal/**")
                        .permitAll()

                        // PATIENT (must be before generic DOCTOR rules)
                        .requestMatchers(
                                HttpMethod.GET,
                                "/medical-records/me")
                        .hasRole("PATIENT")

                        // DOCTOR
                        .requestMatchers(
                                HttpMethod.POST,
                                "/medical-records/create")
                        .hasRole("DOCTOR")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/medical-records/**")
                        .hasRole("DOCTOR")

                        .requestMatchers(
                                "/medical-records/doctor/**")
                        .hasRole("DOCTOR")

                        .requestMatchers(
                                "/medical-records/patient/**")
                        .hasRole("DOCTOR")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/medical-records/*")
                        .hasRole("DOCTOR")

                        .anyRequest()
                        .authenticated())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
