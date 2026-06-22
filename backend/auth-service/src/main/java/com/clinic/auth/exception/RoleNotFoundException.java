package com.clinic.auth.exception;

import com.clinic.auth.common.exception.ResourceNotFoundException;

public class RoleNotFoundException extends ResourceNotFoundException {
    public RoleNotFoundException() {
        super("Role not found");
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
