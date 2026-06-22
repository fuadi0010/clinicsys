package com.clinic.patient.exception;

import com.clinic.patient.common.exception.ResourceNotFoundException;

public class PatientNotFoundException extends ResourceNotFoundException {
    public PatientNotFoundException() {
        super("Patient not found");
    }
    public PatientNotFoundException(String message) {
        super(message);
    }
}
