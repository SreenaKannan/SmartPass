package com.smartPass.smartpass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusExplanationResponse {
    private String status;
    private String explanation;
    private String nextSteps;
    private List<String> reasons;      // specific, real factors behind the current status
    private List<String> actionItems;  // concrete things the user can do right now
    private Integer queuePosition;     // how many pending applications came before this one (null if not applicable)
}
