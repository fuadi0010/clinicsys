package com.clinic.patient.exception;

import com.clinic.patient.common.exception.DuplicateResourceException;

public class PatientAlreadyExistsException extends DuplicateResourceException {
    public PatientAlreadyExistsException() {
        super("Patient already exists");
    }
    public PatientAlreadyExistsException(String message) {
        super(message);
    }
}
