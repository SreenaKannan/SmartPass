package com.smartPass.smartpass.dto;

import lombok.Data;

import java.time.LocalDateTime;

/** What the client sees about a payment. Never contains card numbers, CVV or OTP. */
@Data
public class PaymentOrderResponse {
    private Long paymentId;
    private String orderId;
    private String transactionId;
    private String status;
    private String purpose;
    private Long cardId;
    private double amount;
    private String passType;
    private String transportType;
    private String route;
    private String cardExpiryDate;
    private String paymentMethod;
    private String methodDetail;
    private String paymentDate;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private Long secondsRemaining; // checkout session time left (only while INITIATED)
    private String failureReason;
}
