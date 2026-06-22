package com.clinic.auth.exception;

import com.clinic.auth.common.exception.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {
    public InvalidTokenException() {
        super("Invalid token");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
