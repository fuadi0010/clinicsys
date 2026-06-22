package com.clinic.doctor.doctor.mapper;

import java.util.List;

import com.clinic.doctor.doctor.dto.request.CreateDoctorRequest;
import com.clinic.doctor.doctor.dto.request.UpdateDoctorRequest;
import com.clinic.doctor.doctor.dto.response.CreateDoctorResponse;
import com.clinic.doctor.doctor.dto.response.DoctorDetailResponse;
import com.clinic.doctor.doctor.dto.response.DoctorListResponse;
import com.clinic.doctor.doctor.entity.DoctorEntity;

public interface DoctorMapper {
    DoctorEntity toEntity(CreateDoctorRequest request);
    CreateDoctorResponse toResponse(DoctorEntity doctor);
    DoctorDetailResponse toDetailResponse(DoctorEntity doctor);
    DoctorListResponse toListResponse(DoctorEntity doctor);
    List<DoctorListResponse>toListResponses(List<DoctorEntity> doctors);
    void updateEntity(DoctorEntity doctor,UpdateDoctorRequest request);
}
