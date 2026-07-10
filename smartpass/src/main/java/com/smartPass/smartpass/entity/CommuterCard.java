package com.smartPass.smartpass.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class CommuterCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    private String userName;

    private String sourceStation;

    private String destinationStation;

    private String passType;

    private String transportType;
    
    private String issueDate;

    private LocalDate expiryDate;

    private double fareAmount;

    private String status;
}