package com.clinic.auth.exception;

import com.clinic.auth.common.exception.DuplicateResourceException;

public class UserAlreadyExistsException extends DuplicateResourceException {
    public UserAlreadyExistsException() {
        super("User already exists");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
