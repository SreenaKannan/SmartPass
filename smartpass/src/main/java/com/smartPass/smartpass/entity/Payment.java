package com.smartPass.smartpass.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long cardId;

    private Long userId;

    private double amount;

    private String paymentMethod;

    private String paymentDate;

    // INITIATED -> SUCCESS | FAILED | CANCELLED | EXPIRED
    private String status = "PENDING";

    // --- Checkout / gateway fields ---
    private String orderId;          // created when checkout starts
    private String transactionId;    // issued only when payment succeeds
    private String purpose;          // "application" or "renewal"
    private String methodDetail;     // masked only, e.g. "Visa **** 1111" - never a full card number
    private String failureReason;
    private Integer attempts = 0;    // wrong-OTP attempts
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}
