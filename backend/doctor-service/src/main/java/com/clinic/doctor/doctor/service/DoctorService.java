package com.clinic.doctor.doctor.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.clinic.doctor.doctor.dto.request.CreateDoctorRequest;
import com.clinic.doctor.doctor.dto.request.UpdateDoctorRequest;
import com.clinic.doctor.doctor.dto.response.CreateDoctorResponse;
import com.clinic.doctor.doctor.dto.response.DoctorDetailResponse;
import com.clinic.doctor.doctor.dto.response.DoctorInternalResponse;
import com.clinic.doctor.doctor.dto.response.DoctorListResponse;

public interface DoctorService {
    CreateDoctorResponse createDoctor(CreateDoctorRequest request);
    DoctorDetailResponse getMyProfile();
    DoctorDetailResponse getDoctorById(Long id);
    DoctorDetailResponse updateMyProfile(UpdateDoctorRequest request);
    List<DoctorListResponse>getAllDoctors();
    DoctorInternalResponse findDoctorInternal( Long doctorId );
    DoctorInternalResponse findByUserId( Long userId );
    String uploadProfileImage( MultipartFile file ) throws IOException;
    DoctorDetailResponse createDoctorByAdmin(com.clinic.doctor.doctor.dto.request.AdminCreateDoctorRequest request, MultipartFile file) throws IOException;
    long countDoctors();
    void updateDoctorRating(Long id, Double averageRating);
}
