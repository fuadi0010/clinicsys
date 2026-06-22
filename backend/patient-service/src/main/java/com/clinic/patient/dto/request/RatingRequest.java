package com.clinic.patient.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RatingRequest {
    @NotNull(message = "ID janji temu wajib diisi")
    private Long appointmentId;

    @NotNull(message = "ID dokter wajib diisi")
    private Long doctorId;

    @NotNull(message = "Rating wajib diisi")
    @Min(value = 1, message = "Rating minimal 1 bintang")
    @Max(value = 5, message = "Rating maksimal 5 bintang")
    private Integer rating;

    @Size(max = 500, message = "Ulasan maksimal 500 karakter")
    private String review;
}
