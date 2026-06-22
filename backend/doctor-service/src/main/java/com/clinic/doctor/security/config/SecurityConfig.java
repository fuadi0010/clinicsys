package com.clinic.doctor.security.config;

import com.clinic.doctor.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
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
                                "/doctors/internal/**")
                        .permitAll()

                        .requestMatchers(
                                "/doctors/uploads/**")
                        .permitAll()

                        // PUBLIC ENDPOINTS
                        .requestMatchers(
                                HttpMethod.GET,
                                "/doctors")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/doctors/*")
                        .permitAll()

                        // DOCTOR ENDPOINT
                        .requestMatchers(
                                HttpMethod.POST,
                                "/doctors/profile-image")
                        .hasRole("DOCTOR")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/doctors/create")
                        .hasRole("DOCTOR")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/doctors/me")
                        .hasRole("DOCTOR")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/doctors/me")
                        .hasRole("DOCTOR")

                        .anyRequest()
                        .authenticated())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
