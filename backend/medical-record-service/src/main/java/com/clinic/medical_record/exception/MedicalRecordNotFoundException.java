package com.clinic.medical_record.exception;

import com.clinic.medical_record.common.exception.ResourceNotFoundException;

public class MedicalRecordNotFoundException extends ResourceNotFoundException {
    public MedicalRecordNotFoundException() {
        super("Medical record not found");
    }
    public MedicalRecordNotFoundException(String message) {
        super(message);
    }
}
