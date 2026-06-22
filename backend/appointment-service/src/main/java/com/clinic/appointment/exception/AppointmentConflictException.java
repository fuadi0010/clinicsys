package com.clinic.appointment.exception;

import com.clinic.appointment.common.exception.BusinessException;

public class AppointmentConflictException extends BusinessException {
    public AppointmentConflictException() {
        super("Appointment conflict");
    }
    public AppointmentConflictException(String message) {
        super(message);
    }
}
