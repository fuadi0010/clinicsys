package com.clinic.medical_record.exception;

import com.clinic.medical_record.common.exception.ForbiddenException;

public class MedicalRecordAccessDeniedException extends ForbiddenException {
    public MedicalRecordAccessDeniedException() {
        super("Medical record access denied");
    }
    public MedicalRecordAccessDeniedException(String message) {
        super(message);
    }
}
