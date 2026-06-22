package com.clinic.patient.service.serviceImplement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.clinic.patient.dto.request.ContactMessageRequest;
import com.clinic.patient.entity.ContactMessage;
import com.clinic.patient.repository.ContactMessageRepository;
import com.clinic.patient.service.ContactMessageService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    @Override
    public void saveMessage(ContactMessageRequest request) {
        ContactMessage message = ContactMessage.builder()
                .senderName(request.getSenderName())
                .senderEmail(request.getSenderEmail())
                .subject(request.getSubject())
                .messageContent(request.getMessageContent())
                .build();
        
        contactMessageRepository.save(message);
    }
}
