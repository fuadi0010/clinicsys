package com.clinic.appointment.appointment.email;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendReminderEmail(
            String patientEmail,
            String patientName,
            String doctorName,
            String specialization,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            String status) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(patientEmail);

        message.setSubject("Informasi Janji Temu - Klinik Kesehatan");

        message.setText(
                """
                Yth. Bapak/Ibu %s,

                Berikut adalah rincian informasi janji temu Anda di Klinik Kesehatan:

                --------------------------------------------------
                Detail Janji Temu:
                - Nama Pasien      : %s
                - Nama Dokter      : %s
                - Spesialisasi     : %s
                - Tanggal          : %s
                - Waktu/Jam        : %s WIB
                - Status Akhir     : %s
                --------------------------------------------------

                Mohon hadir 15 menit sebelum waktu yang dijadwalkan.
                Terima kasih atas kepercayaan Anda pada layanan kami.

                Salam Sehat,
                Tim Klinik Kesehatan
                """
                .formatted(
                        patientName,
                        patientName,
                        doctorName,
                        specialization,
                        appointmentDate,
                        appointmentTime,
                        status));

        mailSender.send(message);
    }

}
