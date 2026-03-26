package com.example.parking.controller;

import com.example.parking.common.ApiResponse;
import com.example.parking.dto.VehicleCreateRequest;
import com.example.parking.dto.VehicleDto;
import com.example.parking.security.SecurityUtil;
import com.example.parking.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<VehicleDto>> create(@Valid @RequestBody VehicleCreateRequest req) {
        Long userId = SecurityUtil.getCurrentUserId();
        VehicleDto dto = vehicleService.createVehicle(userId, req);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<VehicleDto>>> list() {
        Long userId = SecurityUtil.getCurrentUserId();
        String role = SecurityUtil.getCurrentRoleAuthority();
        boolean isAdmin = role != null && role.contains("ADMIN");
        return ResponseEntity.ok(ApiResponse.success(vehicleService.listVehicles(userId, isAdmin)));
    }
}

