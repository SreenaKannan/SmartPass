package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.dto.DashboardDTO;
import com.smartPass.smartpass.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardDTO getDashboardStats() {

        return dashboardService.getDashboardStats();

    }
}