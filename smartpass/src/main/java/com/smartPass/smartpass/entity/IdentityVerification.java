package com.smartPass.smartpass.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Entity @Data public class IdentityVerification {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(unique=true) private Long userId;
 private String documentType;
 private String originalFileName;
 @JsonIgnore private String storedFileName;
 private String declaredName;
 @Column(length=1000) private String declaredAddress;
 private String status="PENDING";
 @Column(length=1000) private String adminNote;
 private LocalDateTime uploadedAt=LocalDateTime.now();
 private LocalDateTime reviewedAt;
 private Long reviewedBy;
}
