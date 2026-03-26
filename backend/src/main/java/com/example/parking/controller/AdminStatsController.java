package com.example.parking.controller;

import com.example.parking.common.ApiResponse;
import com.example.parking.dto.AdminStatsDto;
import com.example.parking.security.SecurityUtil;
import com.example.parking.service.AdminStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {
    private final AdminStatsService adminStatsService;

    public AdminStatsController(AdminStatsService adminStatsService) {
        this.adminStatsService = adminStatsService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminStatsDto>> stats(@RequestParam(name = "days", required = false) Integer days) {
        int d = days == null ? 7 : days;
        AdminStatsDto dto = adminStatsService.getAdminStats(d);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}

