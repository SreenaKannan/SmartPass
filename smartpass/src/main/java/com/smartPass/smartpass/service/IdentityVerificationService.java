package com.smartPass.smartpass.service;
import com.smartPass.smartpass.entity.*; import com.smartPass.smartpass.repository.IdentityVerificationRepository;
import org.springframework.beans.factory.annotation.Value; import org.springframework.core.io.*; import org.springframework.http.*; import org.springframework.stereotype.Service; import org.springframework.web.multipart.MultipartFile;
import java.io.IOException; import java.nio.file.*; import java.time.LocalDateTime; import java.util.*;
@Service public class IdentityVerificationService {
 private final IdentityVerificationRepository repo; private final Path directory;
 public IdentityVerificationService(IdentityVerificationRepository repo,@Value("${smartpass.upload-dir:uploads}") String uploadDir){this.repo=repo;this.directory=Paths.get(uploadDir).toAbsolutePath().normalize();}
 public IdentityVerification upload(User user,String type,String name,String address,MultipartFile file)throws IOException{
  if(file==null||file.isEmpty()) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST,"Choose an identity document.");
  if(file.getSize()>5*1024*1024) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST,"Document must be 5 MB or smaller.");
  String content=file.getContentType(); if(!List.of("application/pdf","image/jpeg","image/png").contains(content)) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST,"Only PDF, JPG, and PNG documents are accepted.");
  Files.createDirectories(directory); String safeName=UUID.randomUUID()+"-"+file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]","_"); Files.copy(file.getInputStream(),directory.resolve(safeName),StandardCopyOption.REPLACE_EXISTING);
  IdentityVerification v=repo.findByUserId(user.getId()).orElseGet(IdentityVerification::new); v.setUserId(user.getId());v.setDocumentType(type);v.setDeclaredName(name);v.setDeclaredAddress(address);v.setOriginalFileName(file.getOriginalFilename());v.setStoredFileName(safeName);v.setStatus("PENDING");v.setAdminNote(null);v.setUploadedAt(LocalDateTime.now());return repo.save(v);
 }
 public IdentityVerification mine(User user){return repo.findByUserId(user.getId()).orElse(null);} public List<IdentityVerification> all(){return repo.findAll();}
 public IdentityVerification review(Long id,String status,String note,User admin){if(!List.of("VERIFIED","REJECTED").contains(status))throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid verification decision.");IdentityVerification v=repo.findById(id).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND,"Verification not found."));v.setStatus(status);v.setAdminNote(note);v.setReviewedAt(LocalDateTime.now());v.setReviewedBy(admin.getId());return repo.save(v);}
 public ResponseEntity<Resource> document(Long id){IdentityVerification v=repo.findById(id).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND,"Verification not found."));Path p=directory.resolve(v.getStoredFileName()).normalize();if(!p.startsWith(directory)||!Files.exists(p))throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND,"Document is unavailable.");Resource r=new FileSystemResource(p);return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\""+v.getOriginalFileName()+"\"").body(r);}
}
