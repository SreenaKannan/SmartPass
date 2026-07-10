package com.smartPass.smartpass.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RenewalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long renewalId;

    private Long cardId;

    private String userName;

    private String renewalDate;

    private String oldExpiryDate;

    private String newExpiryDate;

    private double amountPaid;
}