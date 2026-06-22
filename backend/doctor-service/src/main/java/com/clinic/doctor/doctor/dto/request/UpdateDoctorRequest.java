package com.clinic.doctor.doctor.dto.request;

import java.math.BigDecimal;

import com.clinic.doctor.doctor.enumType.DoctorSpecialization;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateDoctorRequest {
    @Size(max = 100, message = "Nama lengkap maksimal 100 karakter")
    @Pattern(regexp = "^[a-zA-Z\\s',.-]+$", message = "Nama lengkap hanya boleh berisi huruf dan karakter umum nama (spasi, ', -, ., ,)")
    private String fullName;

    private DoctorSpecialization specialization;

    @Pattern(regexp = "^(08[0-9]{8,11}|8[0-9]{9,12})$", message = "Nomor HP harus diawali '08' atau '8' dengan panjang 10-13 digit")
    private String phoneNumber;

    @Size(max = 1000, message = "Biografi maksimal 1000 karakter")
    private String bio;

    @DecimalMin("0.0")
    private BigDecimal consultationFee;
}
