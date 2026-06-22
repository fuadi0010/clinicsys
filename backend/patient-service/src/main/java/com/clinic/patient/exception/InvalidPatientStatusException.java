package com.clinic.patient.exception;

import com.clinic.patient.common.exception.BusinessException;

public class InvalidPatientStatusException extends BusinessException {
    public InvalidPatientStatusException() {
        super("Invalid patient status");
    }
    public InvalidPatientStatusException(String message) {
        super(message);
    }
}
