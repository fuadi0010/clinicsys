package com.clinic.patient.service;

import com.clinic.patient.dto.request.ContactMessageRequest;

public interface ContactMessageService {
    void saveMessage(ContactMessageRequest request);
}
