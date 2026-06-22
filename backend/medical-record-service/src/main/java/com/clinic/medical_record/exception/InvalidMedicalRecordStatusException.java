package com.clinic.medical_record.exception;

import com.clinic.medical_record.common.exception.BusinessException;

public class InvalidMedicalRecordStatusException extends BusinessException {
    public InvalidMedicalRecordStatusException() {
        super("Invalid medical record status");
    }
    public InvalidMedicalRecordStatusException(String message) {
        super(message);
    }
}
