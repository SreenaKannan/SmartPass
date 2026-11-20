package com.smartPass.smartpass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationReviewRequest {
    private String sourceStation;
    private String destinationStation;
    private String passType;
    private String transportType;
}
