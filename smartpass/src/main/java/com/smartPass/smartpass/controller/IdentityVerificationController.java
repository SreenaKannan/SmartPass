package com.smartPass.smartpass.controller;
import com.smartPass.smartpass.entity.*; import com.smartPass.smartpass.service.IdentityVerificationService; import org.springframework.core.io.Resource; import org.springframework.http.ResponseEntity; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.web.bind.annotation.*; import org.springframework.web.multipart.MultipartFile; import java.io.IOException; import java.util.*;
@RestController public class IdentityVerificationController {
 private final IdentityVerificationService service; public IdentityVerificationController(IdentityVerificationService service){this.service=service;}
 @PostMapping("/api/identity-verification") public IdentityVerification upload(@AuthenticationPrincipal User user,@RequestParam String documentType,@RequestParam String declaredName,@RequestParam String declaredAddress,@RequestParam MultipartFile document)throws IOException{return service.upload(user,documentType,declaredName,declaredAddress,document);}
 @GetMapping("/api/identity-verification/me") public IdentityVerification mine(@AuthenticationPrincipal User user){return service.mine(user);}
 @GetMapping("/api/admin/identity-verifications") public List<IdentityVerification> all(){return service.all();}
 @PutMapping("/api/admin/identity-verifications/{id}") public IdentityVerification review(@PathVariable Long id,@RequestParam String decision,@RequestParam(required=false) String note,@AuthenticationPrincipal User admin){return service.review(id,decision,note,admin);}
 @GetMapping("/api/admin/identity-verifications/{id}/document") public ResponseEntity<Resource> document(@PathVariable Long id){return service.document(id);}
}
