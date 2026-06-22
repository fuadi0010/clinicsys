package com.clinic.patient.service;

import com.clinic.patient.dto.request.RatingRequest;
import com.clinic.patient.dto.response.RatingResponse;
import java.util.List;

public interface RatingService {
    RatingResponse submitRating(RatingRequest request);
    boolean hasRatingBeenSubmitted(Long appointmentId);
    List<RatingResponse> getRatingsByDoctorId(Long doctorId);
    List<RatingResponse> getMyRatings();
}
