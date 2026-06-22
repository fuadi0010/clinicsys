package com.clinic.patient.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessageRequest {
    @NotBlank(message = "Nama pengirim wajib diisi")
    @Size(max = 100, message = "Nama pengirim maksimal 100 karakter")
    private String senderName;

    @NotBlank(message = "Email pengirim wajib diisi")
    @Email(message = "Format email tidak valid")
    @Size(max = 100, message = "Email pengirim maksimal 100 karakter")
    private String senderEmail;

    @NotBlank(message = "Subjek pesan wajib diisi")
    @Size(max = 150, message = "Subjek pesan maksimal 150 karakter")
    private String subject;

    @NotBlank(message = "Isi pesan wajib diisi")
    @Size(max = 5000, message = "Isi pesan maksimal 5000 karakter")
    private String messageContent;
}
