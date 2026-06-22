package com.clinic.doctor.exception;

import com.clinic.doctor.common.exception.DuplicateResourceException;

public class DoctorAlreadyExistsException extends DuplicateResourceException {
    public DoctorAlreadyExistsException() {
        super("Doctor already exists");
    }
    public DoctorAlreadyExistsException(String message) {
        super(message);
    }
}
