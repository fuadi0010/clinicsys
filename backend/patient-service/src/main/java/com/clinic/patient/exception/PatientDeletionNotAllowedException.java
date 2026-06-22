package com.clinic.patient.exception;

import com.clinic.patient.common.exception.BusinessException;

public class PatientDeletionNotAllowedException extends BusinessException {
    public PatientDeletionNotAllowedException() {
        super("Patient deletion not allowed");
    }
    public PatientDeletionNotAllowedException(String message) {
        super(message);
    }
}
