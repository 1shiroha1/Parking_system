package com.example.parking.controller;

import com.example.parking.common.ApiResponse;
import com.example.parking.dto.RepairTicketCreateRequest;
import com.example.parking.dto.RepairTicketDto;
import com.example.parking.dto.RepairTicketUpdateRequest;
import com.example.parking.dto.RepairWorkerCreateRequest;
import com.example.parking.security.SecurityUtil;
import com.example.parking.service.RepairService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RepairController {
    private final RepairService repairService;

    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @PostMapping("/api/admin/repair-workers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> createWorker(@Valid @RequestBody RepairWorkerCreateRequest req) {
        repairService.createRepairWorker(req);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/api/admin/repair-tickets")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RepairTicketDto>> createTicket(@Valid @RequestBody RepairTicketCreateRequest req) {
        Long reporterUserId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(repairService.createTicket(reporterUserId, req)));
    }

    @GetMapping("/api/admin/repair-tickets")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RepairTicketDto>>> listAll() {
        return ResponseEntity.ok(ApiResponse.success(repairService.listAll()));
    }

    @GetMapping("/api/repair-tickets/my")
    @PreAuthorize("hasRole('REPAIR')")
    public ResponseEntity<ApiResponse<List<RepairTicketDto>>> listMy() {
        Long workerUserId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(repairService.listMyAssigned(workerUserId)));
    }

    @PatchMapping("/api/repair-tickets/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','REPAIR')")
    public ResponseEntity<ApiResponse<RepairTicketDto>> update(@PathVariable("id") Long ticketId,
                                                                  @Valid @RequestBody RepairTicketUpdateRequest req) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        String role = SecurityUtil.getCurrentRoleAuthority();
        boolean isAdmin = role != null && role.contains("ADMIN");
        return ResponseEntity.ok(ApiResponse.success(repairService.updateTicket(currentUserId, isAdmin, ticketId, req)));
    }
}

