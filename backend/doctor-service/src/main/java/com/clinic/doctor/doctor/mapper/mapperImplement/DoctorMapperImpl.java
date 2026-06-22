package com.clinic.doctor.doctor.mapper.mapperImplement;

import java.util.List;

import org.springframework.stereotype.Component;

import com.clinic.doctor.doctor.dto.request.CreateDoctorRequest;
import com.clinic.doctor.doctor.dto.request.UpdateDoctorRequest;
import com.clinic.doctor.doctor.dto.response.CreateDoctorResponse;
import com.clinic.doctor.doctor.dto.response.DoctorDetailResponse;
import com.clinic.doctor.doctor.dto.response.DoctorListResponse;
import com.clinic.doctor.doctor.entity.DoctorEntity;
import com.clinic.doctor.doctor.mapper.DoctorMapper;

@Component
public class DoctorMapperImpl implements DoctorMapper {

    @Override
    public DoctorEntity toEntity(CreateDoctorRequest request) {
        return DoctorEntity
                .builder()
                .fullName(request.getFullName())
                .specialization(request.getSpecialization())
                .strNumber(request.getStrNumber())
                .phoneNumber(request.getPhoneNumber())
                .consultationFee(request.getConsultationFee())
                .bio(request.getBio())
                .build();
    }

    @Override
    public CreateDoctorResponse toResponse(DoctorEntity doctor) {
        return CreateDoctorResponse
                .builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .consultationFee(doctor.getConsultationFee())
                .build();
    }

    @Override
    public DoctorDetailResponse toDetailResponse(DoctorEntity doctor) {
        return DoctorDetailResponse
                .builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .strNumber(doctor.getStrNumber())
                .phoneNumber(doctor.getPhoneNumber())
                .bio(doctor.getBio())
                .consultationFee(doctor.getConsultationFee())
                .profileImageUrl(doctor.getProfileImageUrl())
                .averageRating(doctor.getAverageRating())
                .build();
    }

    @Override
    public DoctorListResponse toListResponse(DoctorEntity doctor) {
        return DoctorListResponse
                .builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .profileImageUrl(doctor.getProfileImageUrl())
                .consultationFee(doctor.getConsultationFee())
                .averageRating(doctor.getAverageRating())
                .build();
    }

    @Override
    public List<DoctorListResponse> toListResponses(List<DoctorEntity> doctors) {
        return doctors.stream()
                .map(this::toListResponse)
                .toList();
    }

    @Override
    public void updateEntity(DoctorEntity doctor, UpdateDoctorRequest request) {
        if (request.getFullName() != null) {
            doctor.setFullName(request.getFullName());
        }
        if (request.getSpecialization() != null) {
            doctor.setSpecialization(request.getSpecialization());
        }
        if (request.getPhoneNumber() != null) {
            doctor.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getBio() != null) {
            doctor.setBio(request.getBio());
        }
        if (request.getConsultationFee() != null) {
            doctor.setConsultationFee(request.getConsultationFee());
        }
    }
}
