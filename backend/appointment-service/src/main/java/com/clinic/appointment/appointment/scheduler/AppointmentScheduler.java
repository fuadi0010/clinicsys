package com.clinic.appointment.appointment.scheduler;

import com.clinic.appointment.appointment.enumType.AppointmentStatus;
import com.clinic.appointment.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentScheduler {

    private final AppointmentRepository appointmentRepository;

    @Scheduled(cron = "0 0 0 * * *") // Berjalan setiap hari pukul 00:00 tengah malam
    public void autoUpdatePastAppointments() {
        LocalDate today = LocalDate.now();
        
        // 1. PAID atau APPROVED yang sudah lewat hari -> COMPLETED
        int completedCount = appointmentRepository.updateStatusForPastAppointments(
                AppointmentStatus.COMPLETED,
                today,
                List.of(AppointmentStatus.PAID, AppointmentStatus.APPROVED)
        );

        // 2. UNPAID, PENDING, atau SCHEDULED yang sudah lewat hari -> EXPIRED
        int expiredCount = appointmentRepository.updateStatusForPastAppointments(
                AppointmentStatus.EXPIRED,
                today,
                List.of(AppointmentStatus.UNPAID, AppointmentStatus.PENDING, AppointmentStatus.SCHEDULED)
        );

        if (completedCount > 0 || expiredCount > 0) {
            System.out.println("[AppointmentScheduler] Auto-updated past appointments: " + 
                    completedCount + " set to COMPLETED, " + expiredCount + " set to EXPIRED");
        }
    }
}
