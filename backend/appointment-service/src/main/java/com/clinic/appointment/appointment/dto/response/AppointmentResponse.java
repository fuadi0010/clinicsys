package com.clinic.appointment.appointment.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.clinic.appointment.appointment.enumType.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private Long patientUserId;
    private Long doctorId;
    private Long doctorUserId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private String paymentQrUrl;
    private Boolean hasMedicalRecord;
}
