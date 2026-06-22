package com.clinic.patient.dto.request;

import java.time.LocalDate;

import com.clinic.patient.entity.BloodType;
import com.clinic.patient.entity.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePatientRequest {
    @NotBlank(message = "Nama lengkap wajib diisi")
    @Size(max = 100, message = "Nama lengkap maksimal 100 karakter")
    @Pattern(regexp = "^[a-zA-Z\\s',.-]+$", message = "Nama lengkap hanya boleh berisi huruf dan karakter umum nama (spasi, ', -, ., ,)")
    private String fullName;

    @NotNull(message = "Jenis kelamin wajib diisi")
    private Gender gender;

    @NotNull(message = "Tanggal lahir wajib diisi")
    private LocalDate birthDate;

    private BloodType bloodType;

    @NotBlank(message = "Nomor HP wajib diisi")
    @Pattern(regexp = "^(08[0-9]{8,11}|8[0-9]{9,12})$", message = "Nomor HP harus diawali '08' atau '8' dengan panjang 10-13 digit")
    private String phoneNumber;

    @NotBlank(message = "Alamat wajib diisi")
    @Size(max = 255, message = "Alamat maksimal 255 karakter")
    private String address;

    @NotBlank(message = "NIK wajib diisi")
    @Pattern(regexp = "^[0-9]{16}$", message = "NIK harus terdiri dari 16 digit angka")
    private String identityNumber;

    @Size(max = 50, message = "Nomor asuransi maksimal 50 karakter")
    private String insuranceNumber;

    @Size(max = 1000, message = "Catatan alergi maksimal 1000 karakter")
    private String allergyNotes;

    @Size(max = 1000, message = "Catatan medis maksimal 1000 karakter")
    private String medicalNotes;
}
