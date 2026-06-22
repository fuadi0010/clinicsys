package com.clinic.appointment.appointment.dto.request;

import com.clinic.appointment.appointment.enumType.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAppointmentStatusRequest {
    
    @NotNull private AppointmentStatus status;
}
