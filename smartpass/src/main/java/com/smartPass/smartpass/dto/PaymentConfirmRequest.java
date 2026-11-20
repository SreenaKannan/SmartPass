package com.smartPass.smartpass.dto;

import lombok.Data;

@Data
public class PaymentConfirmRequest {
    private String method;      // UPI | CARD | NETBANKING
    private String upiId;
    private String cardNumber;
    private String cardHolder;
    private String expiry;      // MM/YY
    private String cvv;
    private String bank;
    private String otp;
}
