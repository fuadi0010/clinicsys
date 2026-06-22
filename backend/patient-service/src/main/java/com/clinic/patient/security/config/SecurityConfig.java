package com.clinic.patient.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clinic.patient.security.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public org.springframework.web.client.RestTemplate restTemplate() {
        return new org.springframework.web.client.RestTemplate();
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/patients/internal/**")
                        .permitAll()
                        .requestMatchers("/patients/uploads/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/contact-messages")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST,"/patients/create").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.POST,"/patients/profile-image").hasRole("PATIENT")
                        .requestMatchers("/patients/me").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/patients").hasAnyRole("ADMIN","RECEPTIONIST")
                        .requestMatchers(HttpMethod.GET,"/patients/rm/**").hasAnyRole("ADMIN","RECEPTIONIST","DOCTOR")
                        .requestMatchers(HttpMethod.GET,"/patients/*").hasAnyRole("ADMIN","RECEPTIONIST","DOCTOR")
                        .requestMatchers(HttpMethod.DELETE,"/patients/*").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
