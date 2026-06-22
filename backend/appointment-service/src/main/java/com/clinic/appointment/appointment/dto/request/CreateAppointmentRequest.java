package com.clinic.appointment.appointment.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAppointmentRequest {

    @NotNull
    private Long doctorId;
    @NotNull
    @FutureOrPresent
    private LocalDate appointmentDate;
    @NotNull
    private LocalTime appointmentTime;
    @jakarta.validation.constraints.Size(max = 1000, message = "Catatan janji temu maksimal 1000 karakter")
    private String notes;
}
