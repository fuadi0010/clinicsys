package com.clinic.patient.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.clinic.patient.dto.request.ContactMessageRequest;
import com.clinic.patient.service.ContactMessageService;
import com.clinic.patient.common.apiresponse.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/contact-messages")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createContactMessage(@Valid @RequestBody ContactMessageRequest request) {
        contactMessageService.saveMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pesan Anda berhasil terkirim. Terima kasih!", null));
    }
}
