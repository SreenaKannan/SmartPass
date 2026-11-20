package com.smartPass.smartpass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewApplicationResponse {
    private List<String> issues;
    private List<String> suggestions;
    private List<String> warnings;
    private boolean canProceed;
}
