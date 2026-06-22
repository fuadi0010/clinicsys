package com.clinic.auth.exception;

import com.clinic.auth.common.exception.UnauthorizedException;

public class InvalidPasswordException extends UnauthorizedException {
    public InvalidPasswordException() {
        super("Invalid password");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
