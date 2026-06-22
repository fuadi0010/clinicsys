package com.clinic.appointment.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clinic.appointment.security.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/appointments/internal/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/appointments/available-slots").permitAll()
                        .requestMatchers("/appointments/simulate-pay/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/appointments/create").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/appointments/me").hasRole("PATIENT")
                        .requestMatchers("/appointments/*/cancel").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/appointments/doctor/**").hasRole("DOCTOR")
                        .requestMatchers("/appointments/*/status").hasAnyRole("DOCTOR", "PATIENT")
                        .requestMatchers(HttpMethod.GET, "/appointments/*").hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.GET, "/appointments/patient/*").hasAnyRole("ADMIN", "RECEPTIONIST")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
