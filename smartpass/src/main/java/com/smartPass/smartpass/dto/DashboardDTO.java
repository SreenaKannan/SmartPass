package com.smartPass.smartpass.dto;

import lombok.Data;

@Data
public class DashboardDTO {

    private long totalUsers;

    private long totalCards;

    private long activeCards;

    private long renewedCards;

    private long blockedCards;

    private long totalPayments;

}