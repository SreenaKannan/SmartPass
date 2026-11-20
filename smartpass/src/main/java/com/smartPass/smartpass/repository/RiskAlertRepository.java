package com.smartPass.smartpass.repository;
import com.smartPass.smartpass.entity.RiskAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RiskAlertRepository extends JpaRepository<RiskAlert, Long> { List<RiskAlert> findByInvestigationStatus(String status); }
