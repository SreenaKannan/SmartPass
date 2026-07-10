package com.smartPass.smartpass.service;

import com.smartPass.smartpass.entity.Payment;
import com.smartPass.smartpass.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public String makePayment(Payment payment) {

        payment.setPaymentStatus("Success");

        paymentRepository.save(payment);

        return "Payment Successful";
    }
    public List<Payment> getAllPayments() {

        return paymentRepository.findAll();

    }

    public List<Payment> getPaymentsByCardId(Long cardId) {

    return paymentRepository.findByCardId(cardId);
}

    
}