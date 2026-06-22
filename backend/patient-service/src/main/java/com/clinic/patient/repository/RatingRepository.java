package com.clinic.patient.repository;

import com.clinic.patient.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    Optional<RatingEntity> findByAppointmentId(Long appointmentId);
    boolean existsByAppointmentId(Long appointmentId);
    List<RatingEntity> findAllByDoctorId(Long doctorId);
    List<RatingEntity> findAllByPatientId(Long patientId);

    @org.springframework.data.jpa.repository.Query("SELECT AVG(r.rating) FROM RatingEntity r WHERE r.doctorId = :doctorId")
    Double findAverageRatingByDoctorId(@org.springframework.data.repository.query.Param("doctorId") Long doctorId);
}
