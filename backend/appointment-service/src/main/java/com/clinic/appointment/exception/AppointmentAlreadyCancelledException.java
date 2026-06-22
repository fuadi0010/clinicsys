package com.clinic.appointment.exception;

import com.clinic.appointment.common.exception.BusinessException;

public class AppointmentAlreadyCancelledException extends BusinessException {
    public AppointmentAlreadyCancelledException() {
        super("Appointment already cancelled");
    }
    public AppointmentAlreadyCancelledException(String message) {
        super(message);
    }
}
