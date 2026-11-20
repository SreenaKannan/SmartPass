package com.smartPass.smartpass.dto;

import lombok.Data;

/** The client only says WHICH card and WHY - the amount is always decided by the server. */
@Data
public class PaymentInitRequest {
    private Long cardId;
    private String purpose; // "application" or "renewal"
}
