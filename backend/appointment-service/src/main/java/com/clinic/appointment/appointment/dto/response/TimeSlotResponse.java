package com.clinic.appointment.appointment.dto.response;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotResponse {
    private LocalTime time;
    private boolean available;
}
