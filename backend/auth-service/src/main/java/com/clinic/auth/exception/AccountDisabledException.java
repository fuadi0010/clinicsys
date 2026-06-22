package com.clinic.auth.exception;

import com.clinic.auth.common.exception.ForbiddenException;

public class AccountDisabledException extends ForbiddenException {
    public AccountDisabledException() {
        super("Account is disabled");
    }

    public AccountDisabledException(String message) {
        super(message);
    }
}
