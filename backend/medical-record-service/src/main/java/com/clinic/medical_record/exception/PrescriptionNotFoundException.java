package com.clinic.medical_record.exception;

import com.clinic.medical_record.common.exception.ResourceNotFoundException;

public class PrescriptionNotFoundException extends ResourceNotFoundException {
    public PrescriptionNotFoundException() {
        super("Prescription not found");
    }
    public PrescriptionNotFoundException(String message) {
        super(message);
    }
}
