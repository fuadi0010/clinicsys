package com.clinic.appointment.appointment.service.serviceImplement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.clinic.appointment.appointment.dto.request.CreateAppointmentRequest;
import com.clinic.appointment.appointment.dto.request.UpdateAppointmentStatusRequest;
import com.clinic.appointment.appointment.dto.response.AppointmentResponse;
import com.clinic.appointment.appointment.dto.response.DoctorInternalResponse;
import com.clinic.appointment.appointment.dto.response.PatientInternalResponse;
import com.clinic.appointment.appointment.dto.response.TimeSlotResponse;
import com.clinic.appointment.appointment.dto.response.UserInternalResponse;
import com.clinic.appointment.appointment.email.EmailService;
import com.clinic.appointment.appointment.entity.AppointmentEntity;
import com.clinic.appointment.appointment.enumType.AppointmentStatus;
import com.clinic.appointment.appointment.mapper.AppointmentMapper;
import com.clinic.appointment.appointment.repository.AppointmentRepository;
import com.clinic.appointment.appointment.service.AppointmentService;
import com.clinic.appointment.client.AuthClient;
import com.clinic.appointment.client.DoctorClient;
import com.clinic.appointment.client.PatientClient;
import com.clinic.appointment.common.SecurityUtils;
import com.clinic.appointment.exception.AppointmentConflictException;
import com.clinic.appointment.exception.AppointmentNotFoundException;
import com.clinic.appointment.exception.AppointmentAlreadyCompletedException;
import com.clinic.appointment.exception.InvalidAppointmentStatusException;
import com.clinic.appointment.common.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientClient patientClient;
    private final DoctorClient doctorClient;
    private final AuthClient authClient;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;
    private final org.springframework.web.client.RestTemplate restTemplate;

    @Override
    public AppointmentResponse createAppointment(
            CreateAppointmentRequest request) {

        Long currentPatientUserId = getCurrentUserId();

        PatientInternalResponse patient = patientClient.findByUserId(
                currentPatientUserId).getData();

        DoctorInternalResponse doctor = doctorClient.findDoctorById(
                request.getDoctorId()).getData();

        LocalTime appTime = request.getAppointmentTime();
        boolean isValidWorkTime = (appTime.equals(LocalTime.of(8, 0)) ||
                                   appTime.equals(LocalTime.of(8, 30)) ||
                                   appTime.equals(LocalTime.of(9, 0)) ||
                                   appTime.equals(LocalTime.of(9, 30)) ||
                                   appTime.equals(LocalTime.of(10, 0)) ||
                                   appTime.equals(LocalTime.of(10, 30)) ||
                                   appTime.equals(LocalTime.of(11, 0)) ||
                                   appTime.equals(LocalTime.of(13, 0)) ||
                                   appTime.equals(LocalTime.of(13, 30)) ||
                                   appTime.equals(LocalTime.of(14, 0)) ||
                                   appTime.equals(LocalTime.of(14, 30)) ||
                                   appTime.equals(LocalTime.of(15, 0)));
        if (!isValidWorkTime) {
            throw new BadRequestException("Jam janji temu harus berada pada jam kerja yang valid (08:00-11:00 atau 13:00-15:00).");
        }

        // Proteksi jam di masa lalu untuk hari yang sama
        if (request.getAppointmentDate().equals(LocalDate.now())) {
            if (appTime.isBefore(LocalTime.now())) {
                throw new BadRequestException("Jam janji temu tidak boleh di masa lalu.");
            }
        }

        if (appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(
                request.getDoctorId(), request.getAppointmentDate(), request.getAppointmentTime(),
                AppointmentStatus.CANCELLED)) {
            throw new AppointmentConflictException(
                    "Time slot already booked for this doctor");
        }

        UserInternalResponse user = authClient.findById(
                patient.getUserId()).getData();

        AppointmentEntity appointment = appointmentMapper.toEntity(
                request);

        appointment.setPatientId(
                patient.getId());

        appointment.setPatientUserId(
                patient.getUserId());

        appointment.setDoctorUserId(
                doctor.getUserId());

        appointment.setPatientEmail(
                user.getEmail());

        appointment.setDoctorName(
                doctor.getFullName());

        // Status awal untuk alur simulasi pembayaran QR HP
        appointment.setStatus(AppointmentStatus.UNPAID);

        appointment = appointmentRepository.save(
                appointment);

        // email reminder dikirim nanti setelah PAID sukses
        AppointmentResponse response = appointmentMapper.toResponse(appointment);
        String paymentUrl = "http://" + getLocalIpAddress() + ":8080/api/appointments/simulate-pay/" + appointment.getId();
        response.setPaymentQrUrl(generateQrCodeBase64(paymentUrl));
        return response;
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEmailAsync(AppointmentEntity appointment) {
        String patientName = "Pasien";
        String doctorName = appointment.getDoctorName() != null ? appointment.getDoctorName() : "Dokter";
        String specialization = "Umum";

        try {
            PatientInternalResponse patient = patientClient.findPatientInternal(appointment.getPatientId()).getData();
            if (patient != null && patient.getFullName() != null) {
                patientName = patient.getFullName();
            }
        } catch (Exception e) {
            System.err.println("Gagal mengambil nama pasien untuk email: " + e.getMessage());
        }

        try {
            DoctorInternalResponse doctor = doctorClient.findDoctorById(appointment.getDoctorId()).getData();
            if (doctor != null) {
                if (doctor.getFullName() != null) {
                    doctorName = doctor.getFullName();
                }
                if (doctor.getSpecialization() != null) {
                    specialization = doctor.getSpecialization();
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal mengambil spesialisasi dokter untuk email: " + e.getMessage());
        }

        emailService.sendReminderEmail(
                appointment.getPatientEmail(),
                patientName,
                doctorName,
                specialization,
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus().name()
        );
    }

    @Override
    public List<AppointmentResponse> getMyAppointmentsAsPatient() {
        Long currentPatientUserId = getCurrentUserId();
        PatientInternalResponse patient = patientClient.findByUserId(currentPatientUserId).getData();
        if (patient == null) {
            throw new BadRequestException("Profil Pasien belum lengkap atau tidak ditemukan");
        }
        List<AppointmentEntity> appointments = appointmentRepository.findAllByPatientId(patient.getId());
        return appointmentMapper.toResponses(appointments);
    }

    @Override
    public List<AppointmentResponse> getMyAppointmentsAsDoctor() {
        Long currentDoctorUserId = getCurrentUserId();
        List<AppointmentEntity> appointments = appointmentRepository.findAllByDoctorUserIdAndStatusIn(
                currentDoctorUserId,
                List.of(AppointmentStatus.PAID, AppointmentStatus.COMPLETED)
        );
        return appointmentMapper.toResponses(appointments);
    }

    @Override
    public AppointmentResponse getAppointmentForDoctor(Long id) {
        Long currentDoctorUserId = getCurrentUserId();
        AppointmentEntity appointment = appointmentRepository.findByIdAndDoctorUserId(id, currentDoctorUserId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        return appointmentMapper.toResponse(appointment);
    }

    @Override
    public AppointmentResponse getAppointmentForPatient(Long id) {
        Long currentPatientUserId = getCurrentUserId();
        AppointmentEntity appointment = appointmentRepository.findByIdAndPatientUserId(id, currentPatientUserId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found or you don't have access to it"));
        AppointmentResponse response = appointmentMapper.toResponse(appointment);
        String paymentUrl = "http://" + getLocalIpAddress() + ":8080/api/appointments/simulate-pay/" + appointment.getId();
        response.setPaymentQrUrl(generateQrCodeBase64(paymentUrl));
        return response;
    }

    @Override
    public AppointmentResponse cancelAppointment(Long id) {
        Long currentPatientUserId = getCurrentUserId();
        AppointmentEntity appointment = appointmentRepository.findByIdAndPatientUserId(id, currentPatientUserId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppointmentAlreadyCompletedException("Cannot cancel completed appointment");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(appointment);
    }

    @Override
    public AppointmentResponse updateAppointmentStatus(Long id, UpdateAppointmentStatusRequest request) {
        Long currentDoctorUserId = getCurrentUserId();
        AppointmentEntity appointment = appointmentRepository.findByIdAndDoctorUserId(id, currentDoctorUserId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        validateStatusTransition(appointment.getStatus(), request.getStatus());
        appointment.setStatus(request.getStatus());
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByPatientId(Long patientId) {
        List<AppointmentEntity> appointments = appointmentRepository.findAllByPatientId(patientId);
        return appointmentMapper.toResponses(appointments);
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId) {
        List<AppointmentEntity> appointments = appointmentRepository.findAllByDoctorIdAndStatusIn(
                doctorId,
                List.of(AppointmentStatus.PAID, AppointmentStatus.COMPLETED)
        );
        return appointmentMapper.toResponses(appointments);
    }

    @Override
    public List<TimeSlotResponse> getAvailableSlots(Long doctorId, LocalDate date) {
        List<AppointmentEntity> bookedAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentDateAndStatusNot(
                        doctorId, date, AppointmentStatus.CANCELLED);

        List<LocalTime> bookedTimes = bookedAppointments.stream()
                .map(AppointmentEntity::getAppointmentTime)
                .toList();

        // Batasi jam kerja: 08:00 - 11:00 dan 13:00 - 15:00 dengan interval 30 menit
        List<LocalTime> workTimes = List.of(
            LocalTime.of(8, 0), LocalTime.of(8, 30),
            LocalTime.of(9, 0), LocalTime.of(9, 30),
            LocalTime.of(10, 0), LocalTime.of(10, 30),
            LocalTime.of(11, 0),
            LocalTime.of(13, 0), LocalTime.of(13, 30),
            LocalTime.of(14, 0), LocalTime.of(14, 30),
            LocalTime.of(15, 0)
        );

        List<TimeSlotResponse> slots = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        for (LocalTime time : workTimes) {
            boolean isBooked = bookedTimes.contains(time);
            boolean isPast = date.equals(today) && time.isBefore(now);
            slots.add(TimeSlotResponse.builder()
                    .time(time)
                    .available(!isBooked && !isPast)
                    .build());
        }

        return slots;
    }

    private void validateStatusTransition(AppointmentStatus current, AppointmentStatus next) {
        if (current == AppointmentStatus.CANCELLED || current == AppointmentStatus.COMPLETED) {
            throw new InvalidAppointmentStatusException("Cannot change status of " + current + " appointment");
        }
    }

    @Override
    public AppointmentResponse getAppointmentByIdInternal(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        AppointmentResponse response = appointmentMapper.toResponse(appointment);
        String paymentUrl = "http://" + getLocalIpAddress() + ":8080/api/appointments/simulate-pay/" + appointment.getId();
        response.setPaymentQrUrl(generateQrCodeBase64(paymentUrl));
        return response;
    }

    private Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    private String getLocalIpAddress() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }

    private String generateQrCodeBase64(String text) {
        try {
            com.google.zxing.qrcode.QRCodeWriter qrCodeWriter = new com.google.zxing.qrcode.QRCodeWriter();
            com.google.zxing.common.BitMatrix bitMatrix = qrCodeWriter.encode(text, com.google.zxing.BarcodeFormat.QR_CODE, 220, 220);
            
            java.io.ByteArrayOutputStream pngOutputStream = new java.io.ByteArrayOutputStream();
            com.google.zxing.client.j2se.MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            
            String base64Image = java.util.Base64.getEncoder().encodeToString(pngData);
            return "data:image/png;base64," + base64Image;
        } catch (Exception e) {
            System.err.println("Gagal generate QR Code Base64 lokal: " + e.getMessage());
            // Fallback ke QRServer jika generator lokal gagal
            try {
                String qrApiUrl = "https://api.qrserver.com/v1/create-qr-code/?size=220x220&data=" + java.net.URLEncoder.encode(text, java.nio.charset.StandardCharsets.UTF_8);
                byte[] imageBytes = restTemplate.getForObject(qrApiUrl, byte[].class);
                if (imageBytes != null) {
                    String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
                    return "data:image/png;base64," + base64Image;
                }
            } catch (Exception ex) {
                System.err.println("Fallback ke QRServer juga gagal: " + ex.getMessage());
            }
            return text;
        }
    }

    @Override
    @Transactional
    public AppointmentResponse simulatePayment(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.UNPAID) {
            throw new BadRequestException("Janji temu sudah terbayar atau tidak valid untuk pembayaran.");
        }

        appointment.setStatus(AppointmentStatus.PAID);
        appointment = appointmentRepository.save(appointment);

        // Kirim email reminder asinkronus setelah pembayaran PAID sukses
        try {
            eventPublisher.publishEvent(appointment);
        } catch (Exception e) {
            System.err.println("Failed to publish event for email: " + e.getMessage());
        }

        AppointmentResponse response = appointmentMapper.toResponse(appointment);
        String paymentUrl = "http://" + getLocalIpAddress() + ":8080/api/appointments/simulate-pay/" + appointment.getId();
        response.setPaymentQrUrl(generateQrCodeBase64(paymentUrl));
        return response;
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {
        List<AppointmentEntity> appointments = appointmentRepository.findAll();
        return appointmentMapper.toResponses(appointments);
    }

    @Override
    public long countActiveAppointments() {
        return appointmentRepository.countByStatusIn(List.of(
                AppointmentStatus.PENDING,
                AppointmentStatus.APPROVED,
                AppointmentStatus.SCHEDULED,
                AppointmentStatus.PAID,
                AppointmentStatus.UNPAID
        ));
    }
}
