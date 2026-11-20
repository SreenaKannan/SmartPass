package com.smartPass.smartpass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsightsResponse {
    private int totalCards;
    private int activeCards;
    private int completedRenewals;
    private int pendingApplications;
    private String applicationStatus;
    private String nextImportantAction;
    private LocalDate nextExpiryDate;
    private int totalSuccessfulPayments;
}
