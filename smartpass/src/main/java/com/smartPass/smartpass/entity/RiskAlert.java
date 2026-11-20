package com.smartPass.smartpass.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Data
public class RiskAlert {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Long cardId;
    private Integer riskScore;
    private String riskLevel;
    @Column(length = 1500) private String reason;
    private String investigationStatus = "PENDING";
    @CreationTimestamp private LocalDateTime createdAt;
}
