package com.clinic.appointment.appointment.mapper;

import com.clinic.appointment.appointment.dto.response.AppointmentResponse;
import com.clinic.appointment.appointment.entity.AppointmentEntity;

import java.util.List;

import org.springframework.stereotype.Component;

import com.clinic.appointment.appointment.dto.request.CreateAppointmentRequest;

@Component
public interface AppointmentMapper {
    AppointmentEntity toEntity( CreateAppointmentRequest request);
    AppointmentResponse toResponse( AppointmentEntity entity);
    List<AppointmentResponse> toResponses( List<AppointmentEntity> entities);
}
