package com.example.parking.controller;

import com.example.parking.common.ApiResponse;
import com.example.parking.dto.BillDto;
import com.example.parking.dto.PaymentDto;
import com.example.parking.security.SecurityUtil;
import com.example.parking.service.BillService;
import com.example.parking.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BillController {
    private final BillService billService;
    private final PaymentService paymentService;

    public BillController(BillService billService, PaymentService paymentService) {
        this.billService = billService;
        this.paymentService = paymentService;
    }

    @GetMapping("/api/bills/my")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<BillDto>>> my() {
        Long userId = SecurityUtil.getCurrentUserId();
        String role = SecurityUtil.getCurrentRoleAuthority();
        boolean isAdmin = role != null && role.contains("ADMIN");
        return ResponseEntity.ok(ApiResponse.success(billService.listBills(userId, isAdmin)));
    }

    @GetMapping("/api/admin/bills")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BillDto>>> all() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(billService.listBills(userId, true)));
    }

    @PostMapping("/api/bills/{billId}/pay")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<PaymentDto>> pay(@PathVariable Long billId) {
        Long userId = SecurityUtil.getCurrentUserId();
        String role = SecurityUtil.getCurrentRoleAuthority();
        boolean isAdmin = role != null && role.contains("ADMIN");
        return ResponseEntity.ok(ApiResponse.success(paymentService.mockPay(userId, isAdmin, billId)));
    }
}

