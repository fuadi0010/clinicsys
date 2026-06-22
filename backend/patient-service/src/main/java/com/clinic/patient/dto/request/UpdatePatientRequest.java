package com.clinic.patient.dto.request;

import java.time.LocalDate;
import com.clinic.patient.entity.BloodType;
import com.clinic.patient.entity.Gender;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePatientRequest {
    @Size(max = 100, message = "Nama lengkap maksimal 100 karakter")
    @Pattern(regexp = "^[a-zA-Z\\s',.-]+$", message = "Nama lengkap hanya boleh berisi huruf dan karakter umum nama (spasi, ', -, ., ,)")
    private String fullName;

    private BloodType bloodType;

    @Pattern(regexp = "^(08[0-9]{8,11}|8[0-9]{9,12})$", message = "Nomor HP harus diawali '08' atau '8' dengan panjang 10-13 digit")
    private String phoneNumber;

    @Size(max = 255, message = "Alamat maksimal 255 karakter")
    private String address;

    @Size(max = 50, message = "Nomor asuransi maksimal 50 karakter")
    private String insuranceNumber;

    @Size(max = 1000, message = "Catatan alergi maksimal 1000 karakter")
    private String allergyNotes;

    @Size(max = 1000, message = "Catatan medis maksimal 1000 karakter")
    private String medicalNotes;

    private LocalDate birthDate;
    private Gender gender;
}
