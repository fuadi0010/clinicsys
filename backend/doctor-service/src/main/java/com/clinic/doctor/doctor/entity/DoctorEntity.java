package com.clinic.doctor.doctor.entity;

import com.clinic.doctor.doctor.enumType.DoctorSpecialization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctors")
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,
            unique = true)
    private Long userId;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoctorSpecialization specialization;

    @Column(nullable = false,
            unique = true)
    private String strNumber;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false,
            precision = 12,
            scale = 2)
    private BigDecimal consultationFee;

    @Column(length = 1000)
    private String bio;

    private String profileImageUrl;

    @Builder.Default
    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
