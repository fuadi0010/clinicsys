package com.clinic.appointment.appointment.service;

import java.time.LocalDate;
import java.util.List;

import com.clinic.appointment.appointment.dto.request.CreateAppointmentRequest;
import com.clinic.appointment.appointment.dto.request.UpdateAppointmentStatusRequest;
import com.clinic.appointment.appointment.dto.response.AppointmentResponse;
import com.clinic.appointment.appointment.dto.response.TimeSlotResponse;

public interface AppointmentService {
    AppointmentResponse createAppointment(CreateAppointmentRequest request);
    AppointmentResponse getAppointmentForDoctor(Long id);
    AppointmentResponse getAppointmentForPatient(Long id);
    List<AppointmentResponse> getMyAppointmentsAsPatient();
    List<AppointmentResponse> getMyAppointmentsAsDoctor();
    AppointmentResponse cancelAppointment(Long id);
    AppointmentResponse updateAppointmentStatus(Long id, UpdateAppointmentStatusRequest request);
    List<AppointmentResponse> getAppointmentsByPatientId(Long patientId);
    List<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId);
    List<TimeSlotResponse> getAvailableSlots(Long doctorId, LocalDate date);
    AppointmentResponse getAppointmentByIdInternal(Long id);
    AppointmentResponse simulatePayment(Long id);
    List<AppointmentResponse> getAllAppointments();
    long countActiveAppointments();
}
