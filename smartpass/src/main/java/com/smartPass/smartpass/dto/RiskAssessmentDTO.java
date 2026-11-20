package com.smartPass.smartpass.dto;
import lombok.*; import java.util.List;
@Data @AllArgsConstructor public class RiskAssessmentDTO { private int riskScore; private String riskLevel; private List<String> flags; private String explanation; }
