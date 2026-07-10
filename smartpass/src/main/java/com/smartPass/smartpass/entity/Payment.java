package com.smartPass.smartpass.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long cardId;

    private double amount;

    private String paymentMethod;

    private String paymentDate;

    private String paymentStatus;
}