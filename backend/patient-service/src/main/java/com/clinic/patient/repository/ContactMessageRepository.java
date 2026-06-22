package com.clinic.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.clinic.patient.entity.ContactMessage;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
}
