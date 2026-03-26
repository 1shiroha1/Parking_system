package com.example.parking.controller;

import com.example.parking.common.ApiResponse;
import com.example.parking.dto.EntryExitLogCreateRequest;
import com.example.parking.dto.EntryExitLogDto;
import com.example.parking.security.SecurityUtil;
import com.example.parking.service.EntryExitLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/entry-exit")
public class EntryExitLogController {
    private final EntryExitLogService entryExitLogService;

    public EntryExitLogController(EntryExitLogService entryExitLogService) {
        this.entryExitLogService = entryExitLogService;
    }

    @PostMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EntryExitLogDto>> create(@Valid @RequestBody EntryExitLogCreateRequest req) {
        Long adminUserId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(entryExitLogService.createLog(adminUserId, req)));
    }
}

