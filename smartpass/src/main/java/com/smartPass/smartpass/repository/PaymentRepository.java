package com.smartPass.smartpass.repository;

import com.smartPass.smartpass.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    List<Payment> findByCardId(Long cardId);
}