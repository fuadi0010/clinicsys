package com.clinic.doctor.exception;

import com.clinic.doctor.common.exception.BusinessException;

public class InvalidDoctorStatusException extends BusinessException {
    public InvalidDoctorStatusException() {
        super("Invalid doctor status");
    }
    public InvalidDoctorStatusException(String message) {
        super(message);
    }
}
