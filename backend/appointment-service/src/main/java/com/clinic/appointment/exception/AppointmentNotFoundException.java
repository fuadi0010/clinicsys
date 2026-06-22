package com.clinic.appointment.exception;

import com.clinic.appointment.common.exception.ResourceNotFoundException;

public class AppointmentNotFoundException extends ResourceNotFoundException {
    public AppointmentNotFoundException() {
        super("Appointment not found");
    }
    public AppointmentNotFoundException(String message) {
        super(message);
    }
}
