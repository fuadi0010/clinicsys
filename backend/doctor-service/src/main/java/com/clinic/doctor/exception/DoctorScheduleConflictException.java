package com.clinic.doctor.exception;

import com.clinic.doctor.common.exception.BusinessException;

public class DoctorScheduleConflictException extends BusinessException {
    public DoctorScheduleConflictException() {
        super("Doctor schedule conflict");
    }
    public DoctorScheduleConflictException(String message) {
        super(message);
    }
}
