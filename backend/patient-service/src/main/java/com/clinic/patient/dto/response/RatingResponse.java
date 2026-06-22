package com.clinic.patient.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class RatingResponse {
    private Long id;
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private Integer rating;
    private String review;
    private LocalDateTime createdAt;
}
