package com.smartPass.smartpass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationItem {
    private String title;
    private String description;
    private String type; // "RULE_BASED" or "AI_ASSISTED"
    private String actionUrl;
    private int priority; // 1 = high, 5 = low
}
