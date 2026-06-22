package com.clinic.doctor.doctor.dto.request;

import com.clinic.doctor.doctor.enumType.DoctorSpecialization;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCreateDoctorRequest {

    @NotBlank(message = "Username wajib diisi")
    @Size(min = 5, max = 40, message = "Username minimal 5 karakter dan maksimal 40 karakter")
    private String username;

    @NotBlank(message = "Email wajib diisi")
    @Email(message = "Format email tidak valid")
    @Size(max = 100, message = "Email maksimal 100 karakter")
    private String email;

    @NotBlank(message = "Password wajib diisi")
    @Size(min = 8, max = 100, message = "Password minimal 8 karakter")
    private String password;

    @NotBlank(message = "Nama lengkap wajib diisi")
    @Size(max = 100, message = "Nama lengkap maksimal 100 karakter")
    @Pattern(regexp = "^[a-zA-Z\\s',.-]+$", message = "Nama lengkap hanya boleh berisi huruf dan karakter umum nama (spasi, ', -, ., ,)")
    private String fullName;

    @NotNull(message = "Spesialisasi wajib diisi")
    private DoctorSpecialization specialization;

    @NotBlank(message = "Nomor STR wajib diisi")
    @Pattern(regexp = "^STR-[0-9]{8,10}$", message = "Format nomor STR tidak valid. Harus diawali dengan prefix 'STR-' dan diikuti oleh 8 sampai 10 digit angka saja (contoh: STR-12345678).")
    @Size(max = 50, message = "Nomor STR maksimal 50 karakter")
    private String strNumber;

    @NotBlank(message = "Nomor telepon wajib diisi")
    @Pattern(regexp = "^(08[0-9]{8,11}|8[0-9]{9,12})$", message = "Nomor HP harus diawali '08' atau '8' dengan panjang 10-13 digit")
    private String phoneNumber;

    @NotNull(message = "Tarif konsultasi wajib diisi")
    private BigDecimal consultationFee;

    @Size(max = 1000, message = "Biografi maksimal 1000 karakter")
    private String bio;
}
