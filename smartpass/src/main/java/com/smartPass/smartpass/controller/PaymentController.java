package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.entity.Payment;
import com.smartPass.smartpass.service.PaymentService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public String makePayment(
            @RequestBody Payment payment) {

        return paymentService.makePayment(payment);
    }

    @GetMapping
    public List<Payment> getAllPayments() {

        return paymentService.getAllPayments();

    }

    @GetMapping("/card/{cardId}")
public List<Payment> getPaymentsByCardId(
        @PathVariable Long cardId) {

    return paymentService.getPaymentsByCardId(cardId);
}
}