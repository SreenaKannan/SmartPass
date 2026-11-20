package com.smartPass.smartpass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmartAlert {
    private String priority; // "HIGH", "MEDIUM", "LOW"
    private String message;
    private String actionUrl;
    private LocalDateTime createdAt;
    private String category; // "EXPIRY", "PAYMENT", "APPLICATION", "RENEWAL"
}
