package com.clinic.medical_record.exception;

import com.clinic.medical_record.common.exception.BusinessException;

public class DiagnosisRequiredException extends BusinessException {
    public DiagnosisRequiredException() {
        super("Diagnosis required");
    }
    public DiagnosisRequiredException(String message) {
        super(message);
    }
}
