package com.clinic.patient.service.serviceImplement;

import com.clinic.patient.common.SecurityUtils;
import com.clinic.patient.dto.request.RatingRequest;
import com.clinic.patient.dto.response.RatingResponse;
import com.clinic.patient.entity.PatientEntity;
import com.clinic.patient.entity.RatingEntity;
import com.clinic.patient.exception.PatientNotFoundException;
import com.clinic.patient.common.exception.BadRequestException;
import com.clinic.patient.repository.PatientRepository;
import com.clinic.patient.repository.RatingRepository;
import com.clinic.patient.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final PatientRepository patientRepository;
    private final org.springframework.web.client.RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Value("${internal.security.token:SecretInternalToken123}")
    private String internalToken;

    @Override
    public RatingResponse submitRating(RatingRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        PatientEntity patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new PatientNotFoundException("Profil pasien belum diisi"));

        if (ratingRepository.existsByAppointmentId(request.getAppointmentId())) {
            throw new BadRequestException("Anda sudah memberikan rating untuk janji temu ini.");
        }

        RatingEntity rating = RatingEntity.builder()
                .appointmentId(request.getAppointmentId())
                .patientId(patient.getId())
                .doctorId(request.getDoctorId())
                .rating(request.getRating())
                .review(request.getReview())
                .build();

        rating = ratingRepository.save(rating);

        // Hitung rating rata-rata untuk dokter terkait
        Double avgRating = ratingRepository.findAverageRatingByDoctorId(request.getDoctorId());
        if (avgRating == null) {
            avgRating = 0.0;
        }

        // Kirim update rating rata-rata ke doctor-service
        try {
            String doctorServiceUrl = "http://localhost:8083/doctors/internal/" + request.getDoctorId() + "/rating?averageRating=" + avgRating;
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("X-Internal-Token", internalToken);
            org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);
            restTemplate.exchange(doctorServiceUrl, org.springframework.http.HttpMethod.PUT, entity, Void.class);
        } catch (Exception e) {
            System.err.println("Gagal sinkronisasi rating ke doctor-service: " + e.getMessage());
        }

        return RatingResponse.builder()
                .id(rating.getId())
                .appointmentId(rating.getAppointmentId())
                .patientId(rating.getPatientId())
                .doctorId(rating.getDoctorId())
                .rating(rating.getRating())
                .review(rating.getReview())
                .createdAt(rating.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasRatingBeenSubmitted(Long appointmentId) {
        return ratingRepository.existsByAppointmentId(appointmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingResponse> getRatingsByDoctorId(Long doctorId) {
        return ratingRepository.findAllByDoctorId(doctorId).stream()
                .map(r -> RatingResponse.builder()
                        .id(r.getId())
                        .appointmentId(r.getAppointmentId())
                        .patientId(r.getPatientId())
                        .doctorId(r.getDoctorId())
                        .rating(r.getRating())
                        .review(r.getReview())
                        .createdAt(r.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingResponse> getMyRatings() {
        Long userId = SecurityUtils.getCurrentUserId();
        PatientEntity patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new PatientNotFoundException("Profil pasien belum diisi"));
        return ratingRepository.findAllByPatientId(patient.getId()).stream()
                .map(r -> RatingResponse.builder()
                        .id(r.getId())
                        .appointmentId(r.getAppointmentId())
                        .patientId(r.getPatientId())
                        .doctorId(r.getDoctorId())
                        .rating(r.getRating())
                        .review(r.getReview())
                        .createdAt(r.getCreatedAt())
                        .build())
                .toList();
    }
}
