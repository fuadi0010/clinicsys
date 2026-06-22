package com.clinic.patient.controller;

import com.clinic.patient.common.apiresponse.ApiResponse;
import com.clinic.patient.dto.request.RatingRequest;
import com.clinic.patient.dto.response.RatingResponse;
import com.clinic.patient.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/patients/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<ApiResponse<RatingResponse>> submitRating(@Valid @RequestBody RatingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Rating berhasil dikirim", ratingService.submitRating(request)));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<ApiResponse<Boolean>> checkRating(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(ApiResponse.success(ratingService.hasRatingBeenSubmitted(appointmentId)));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> getRatingsByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponse.success(ratingService.getRatingsByDoctorId(doctorId)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> getMyRatings() {
        return ResponseEntity.ok(ApiResponse.success(ratingService.getMyRatings()));
    }
}
