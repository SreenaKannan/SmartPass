package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.entity.User;
import com.smartPass.smartpass.entity.CommuterCard;
import com.smartPass.smartpass.entity.Payment;
import com.smartPass.smartpass.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.smartPass.smartpass.entity.RiskAlert;
import com.smartPass.smartpass.repository.RiskAlertRepository;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final RiskAlertRepository alerts;

    public AdminController(AdminService adminService, RiskAlertRepository alerts) {
        this.adminService = adminService;
        this.alerts = alerts;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/cards")
    public List<CommuterCard> getAllCards() {
        return adminService.getAllCards();
    }

    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return adminService.getAllPayments();
    }
    @GetMapping("/risk-alerts") public List<RiskAlert> riskAlerts(){ return alerts.findAll(); }
    @GetMapping("/dashboard") public Map<String,Object> dashboard(){ List<CommuterCard> cards=adminService.getAllCards(); List<RiskAlert> all=alerts.findAll(); return Map.of("totalUsers",adminService.getAllUsers().size(),"activeCards",cards.stream().filter(c->"Approved".equals(c.getStatus())).count(),"suspendedCards",cards.stream().filter(c->"Suspended".equals(c.getStatus())).count(),"totalTransactions",adminService.getAllPayments().size(),"highRiskAlerts",all.stream().filter(a->"HIGH".equals(a.getRiskLevel())).count(),"mediumRiskAlerts",all.stream().filter(a->"MEDIUM".equals(a.getRiskLevel())).count(),"lowRiskAlerts",all.stream().filter(a->"LOW".equals(a.getRiskLevel())).count()); }
    @PutMapping("/risk-alerts/{id}/{state}") public RiskAlert updateAlert(@PathVariable Long id,@PathVariable String state){ RiskAlert a=alerts.findById(id).orElseThrow(); if(!List.of("PENDING","INVESTIGATING","SAFE").contains(state.toUpperCase())) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST); a.setInvestigationStatus(state.toUpperCase()); return alerts.save(a); }
    @PutMapping("/cards/{id}/{action}") public String cardAction(@PathVariable Long id,@PathVariable String action){ return switch(action.toLowerCase()){case "approve"->adminService.approveCard(id); case "reject"->adminService.rejectCard(id); case "suspend"->adminService.suspendCard(id); case "block"->adminService.blockCard(id); default->throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST);}; }
}
