package com.clinic.appointment.appointment.mapper.mapperImplement;

import java.util.List;

import com.clinic.appointment.appointment.dto.request.CreateAppointmentRequest;
import com.clinic.appointment.appointment.dto.response.AppointmentResponse;
import com.clinic.appointment.appointment.entity.AppointmentEntity;
import com.clinic.appointment.appointment.mapper.AppointmentMapper;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapperImpl implements AppointmentMapper {

    public AppointmentEntity toEntity(CreateAppointmentRequest request) {
        return AppointmentEntity.builder().doctorId(request.getDoctorId()).appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime()).notes(request.getNotes()).build();
    }

    private String getLocalIpAddress() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }

    public AppointmentResponse toResponse(AppointmentEntity entity) {
        String qrUrl = null;
        if (entity.getStatus() == com.clinic.appointment.appointment.enumType.AppointmentStatus.UNPAID) {
            qrUrl = "http://" + getLocalIpAddress() + ":8080/api/appointments/simulate-pay/" + entity.getId();
        }
        return AppointmentResponse.builder().id(entity.getId()).patientId(entity.getPatientId())
                .patientUserId(entity.getPatientUserId()).doctorId(entity.getDoctorId())
                .doctorUserId(entity.getDoctorUserId()).appointmentDate(entity.getAppointmentDate())
                .appointmentTime(entity.getAppointmentTime()).status(entity.getStatus()).notes(entity.getNotes())
                .createdAt(entity.getCreatedAt()).paymentQrUrl(qrUrl)
                .hasMedicalRecord(entity.getStatus() == com.clinic.appointment.appointment.enumType.AppointmentStatus.COMPLETED)
                .build();
    }

    public List<AppointmentResponse> toResponses(List<AppointmentEntity> entities) {
        return entities.stream().map(this::toResponse).toList();
    }
}
