package com.clinic.appointment.exception;

import com.clinic.appointment.common.exception.BusinessException;

public class AppointmentTimeExpiredException extends BusinessException {
    public AppointmentTimeExpiredException() {
        super("Appointment time expired");
    }
    public AppointmentTimeExpiredException(String message) {
        super(message);
    }
}
