package com.clinic.doctor.exception;

import com.clinic.doctor.common.exception.ResourceNotFoundException;

public class DoctorNotFoundException extends ResourceNotFoundException {
    public DoctorNotFoundException() {
        super("Doctor not found");
    }
    public DoctorNotFoundException(String message) {
        super(message);
    }
}
