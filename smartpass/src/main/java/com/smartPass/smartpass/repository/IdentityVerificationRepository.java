package com.smartPass.smartpass.repository;
import com.smartPass.smartpass.entity.IdentityVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface IdentityVerificationRepository extends JpaRepository<IdentityVerification,Long>{ Optional<IdentityVerification> findByUserId(Long userId); List<IdentityVerification> findByStatus(String status); }
