package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.entity.User;
import com.smartPass.smartpass.entity.CommuterCard;
import com.smartPass.smartpass.entity.Payment;
import com.smartPass.smartpass.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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
}