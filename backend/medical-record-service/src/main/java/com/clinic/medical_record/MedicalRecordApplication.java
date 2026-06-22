package com.clinic.medical_record;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MedicalRecordApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalRecordApplication.class, args);
	}

}
