package com.smartPass.smartpass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertsResponse {
    private List<SmartAlert> alerts;
}
