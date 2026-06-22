package com.clinic.appointment.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {}

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof Long) {
            return (Long) principal;
        }
        throw new SecurityException("Unexpected principal type: " + principal.getClass().getName());
    }
}
