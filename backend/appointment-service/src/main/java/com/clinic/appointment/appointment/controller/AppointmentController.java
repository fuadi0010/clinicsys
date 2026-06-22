package com.clinic.appointment.appointment.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.appointment.appointment.dto.request.CreateAppointmentRequest;
import com.clinic.appointment.appointment.dto.request.UpdateAppointmentStatusRequest;
import com.clinic.appointment.appointment.dto.response.AppointmentResponse;
import com.clinic.appointment.appointment.dto.response.TimeSlotResponse;
import com.clinic.appointment.appointment.service.AppointmentService;
import com.clinic.appointment.common.apiresponse.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment created successfully", appointmentService.createAppointment(request)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyAppointmentsAsPatient() {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getMyAppointmentsAsPatient()));
    }

    @GetMapping("/doctor/me")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyAppointmentsAsDoctor() {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getMyAppointmentsAsDoctor()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentForDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getAppointmentForDoctor(id)));
    }

    @GetMapping("/patient/detail/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentForPatient(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getAppointmentForPatient(id)));
    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentByIdInternal(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getAppointmentByIdInternal(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled successfully", appointmentService.cancelAppointment(id)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointmentStatus(@PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Appointment status updated successfully", appointmentService.updateAppointmentStatus(id, request)));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getAppointmentsByPatientId(patientId)));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getAppointmentsByDoctorId(doctorId)));
    }

    @GetMapping("/available-slots")
    public ResponseEntity<ApiResponse<List<TimeSlotResponse>>> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getAvailableSlots(doctorId, date)));
    }

    @GetMapping(value = "/simulate-pay/{id}", produces = org.springframework.http.MediaType.TEXT_HTML_VALUE)
    @org.springframework.web.bind.annotation.ResponseBody
    public String renderSimulatePaymentPage(@PathVariable Long id) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"id\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Pembayaran Klinik</title>\n" +
                "    <link href=\"https://fonts.googleapis.com/css2?family=Outfit:wght@400;600;700&display=swap\" rel=\"stylesheet\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Outfit', sans-serif;\n" +
                "            background-color: #f8fafc;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            height: 100vh;\n" +
                "            margin: 0;\n" +
                "            padding: 20px;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "        .card {\n" +
                "            background: white;\n" +
                "            padding: 40px 30px;\n" +
                "            border-radius: 20px;\n" +
                "            box-shadow: 0 10px 30px rgba(0,0,0,0.05);\n" +
                "            text-align: center;\n" +
                "            max-width: 400px;\n" +
                "            width: 100%;\n" +
                "            border: 1px solid #f1f5f9;\n" +
                "        }\n" +
                "        .icon {\n" +
                "            font-size: 50px;\n" +
                "            color: #2563eb;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        h2 {\n" +
                "            margin: 0 0 10px 0;\n" +
                "            color: #1e293b;\n" +
                "            font-weight: 700;\n" +
                "        }\n" +
                "        p {\n" +
                "            color: #64748b;\n" +
                "            font-size: 14px;\n" +
                "            margin: 0 0 30px 0;\n" +
                "        }\n" +
                "        button {\n" +
                "            width: 100%;\n" +
                "            padding: 16px;\n" +
                "            background-color: #2563eb;\n" +
                "            color: white;\n" +
                "            border: none;\n" +
                "            border-radius: 12px;\n" +
                "            font-size: 16px;\n" +
                "            font-weight: 600;\n" +
                "            cursor: pointer;\n" +
                "            transition: all 0.2s ease;\n" +
                "            box-shadow: 0 4px 12px rgba(37, 99, 235, 0.2);\n" +
                "        }\n" +
                "        button:hover {\n" +
                "            background-color: #1d4ed8;\n" +
                "        }\n" +
                "        button:active {\n" +
                "            transform: scale(0.98);\n" +
                "        }\n" +
                "        .success-state {\n" +
                "            display: none;\n" +
                "        }\n" +
                "        .success-state .icon {\n" +
                "            color: #10b981;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"card\" id=\"main-card\">\n" +
                "        <!-- Input State -->\n" +
                "        <div id=\"payment-state\">\n" +
                "            <div class=\"icon\">💸</div>\n" +
                "            <h2>Simulasi Pembayaran</h2>\n" +
                "            <p>Klik tombol di bawah ini untuk menyelesaikan pembayaran janji temu Anda secara instan.</p>\n" +
                "            <button id=\"pay-btn\" onclick=\"executePayment()\">Selesaikan Pembayaran</button>\n" +
                "        </div>\n" +
                "        \n" +
                "        <!-- Success State -->\n" +
                "        <div id=\"success-state\" class=\"success-state\">\n" +
                "            <div class=\"icon\">✅</div>\n" +
                "            <h2>Pembayaran Sukses!</h2>\n" +
                "            <p>Status janji temu Anda telah diperbarui menjadi PAID. Halaman laptop Anda akan otomatis diperbarui.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <script>\n" +
                "        function executePayment() {\n" +
                "            const payBtn = document.getElementById('pay-btn');\n" +
                "            payBtn.disabled = true;\n" +
                "            payBtn.innerText = 'Memproses...';\n" +
                "\n" +
                "            fetch(window.location.origin + '/api/appointments/simulate-pay/' + " + id + ", {\n" +
                "                method: 'POST',\n" +
                "                headers: {\n" +
                "                    'Content-Type': 'application/json'\n" +
                "                }\n" +
                "            })\n" +
                "            .then(res => {\n" +
                "                if (res.ok) {\n" +
                "                    document.getElementById('payment-state').style.display = 'none';\n" +
                "                    document.getElementById('success-state').style.display = 'block';\n" +
                "                } else {\n" +
                "                    alert('Gagal memproses pembayaran.');\n" +
                "                    payBtn.disabled = false;\n" +
                "                    payBtn.innerText = 'Selesaikan Pembayaran';\n" +
                "                }\n" +
                "            })\n" +
                "            .catch(err => {\n" +
                "                alert('Terjadi kesalahan jaringan.');\n" +
                "                payBtn.disabled = false;\n" +
                "                payBtn.innerText = 'Selesaikan Pembayaran';\n" +
                "            });\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }

    @PostMapping("/simulate-pay/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> processPaymentSimulation(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Payment simulated successfully", appointmentService.simulatePayment(id)));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ApiResponse<java.util.Map<String, String>>> getAppointmentStatus(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.getAppointmentByIdInternal(id);
        java.util.Map<String, String> statusMap = new java.util.HashMap<>();
        statusMap.put("status", appointment.getStatus().name());
        return ResponseEntity.ok(ApiResponse.success(statusMap));
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getAllAppointments()));
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/count/active")
    public ResponseEntity<ApiResponse<Long>> countActiveAppointments() {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.countActiveAppointments()));
    }
}
