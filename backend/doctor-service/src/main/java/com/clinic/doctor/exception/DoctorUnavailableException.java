package com.clinic.doctor.exception;

import com.clinic.doctor.common.exception.BusinessException;

public class DoctorUnavailableException extends BusinessException {
    public DoctorUnavailableException() {
        super("Doctor is unavailable");
    }
    public DoctorUnavailableException(String message) {
        super(message);
    }
}
