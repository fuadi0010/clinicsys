package com.clinic.auth.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.clinic.auth.security.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {

        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(
                        auth -> auth

                                .requestMatchers(
                                        "/auth/register",
                                        "/auth/login",
                                        "/auth/verify-otp",
                                        "/auth/resend-otp",
                                        "/auth/refresh",
                                        "/auth/logout",
                                        "/auth/internal/**",
                                        "/error")
                                .permitAll()

                                .anyRequest()
                                .authenticated())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
