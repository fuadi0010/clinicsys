package com.clinic.appointment.exception;

import com.clinic.appointment.common.exception.BusinessException;

public class InvalidAppointmentStatusException extends BusinessException {
    public InvalidAppointmentStatusException() {
        super("Invalid appointment status");
    }
    public InvalidAppointmentStatusException(String message) {
        super(message);
    }
}
