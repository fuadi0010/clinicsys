package com.clinic.appointment.exception;

import com.clinic.appointment.common.exception.BusinessException;

public class AppointmentAlreadyCompletedException extends BusinessException {
    public AppointmentAlreadyCompletedException() {
        super("Appointment already completed");
    }
    public AppointmentAlreadyCompletedException(String message) {
        super(message);
    }
}
