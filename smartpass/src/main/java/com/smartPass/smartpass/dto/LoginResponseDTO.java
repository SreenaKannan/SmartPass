package com.smartPass.smartpass.dto;
import lombok.AllArgsConstructor; import lombok.Data;
@Data @AllArgsConstructor public class LoginResponseDTO { private String token; private Long userId; private String name; private String role; }
