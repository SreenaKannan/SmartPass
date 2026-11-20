package com.smartPass.smartpass.repository;

import com.smartPass.smartpass.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    List<Payment> findByCardId(Long cardId);
    List<Payment> findByUserId(Long userId);
    List<Payment> findByUserIdOrderByPaymentIdDesc(Long userId);
    boolean existsByCardIdAndStatus(Long cardId, String status);
    Optional<Payment> findByOrderId(String orderId);
    Optional<Payment> findFirstByCardIdAndPurposeAndStatusOrderByPaymentIdDesc(Long cardId, String purpose, String status);
}
