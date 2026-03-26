package com.example.parking.controller;

import com.example.parking.common.ApiResponse;
import com.example.parking.dto.ParkingLotCreateRequest;
import com.example.parking.dto.ParkingLotDto;
import com.example.parking.dto.ParkingSpaceCreateRequest;
import com.example.parking.dto.ParkingSpaceDto;
import com.example.parking.security.SecurityUtil;
import com.example.parking.service.ParkingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/lots")
    @PreAuthorize("hasAnyRole('ADMIN','USER','REPAIR')")
    public ResponseEntity<ApiResponse<List<ParkingLotDto>>> lots() {
        return ResponseEntity.ok(ApiResponse.success(parkingService.listLots()));
    }

    @GetMapping("/spaces/{lotId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','REPAIR')")
    public ResponseEntity<ApiResponse<List<ParkingSpaceDto>>> spaces(@PathVariable Long lotId) {
        List<ParkingSpaceDto> list = parkingService.listSpaces(lotId);
        String role = SecurityUtil.getCurrentRoleAuthority();
        boolean isAdmin = role != null && role.contains("ADMIN");
        if (!isAdmin) {
            list = list.stream()
                    .filter(s -> !"REPAIR".equalsIgnoreCase(s.getStatusNow()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @PostMapping("/admin/lots")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> createLot(@Valid @RequestBody ParkingLotCreateRequest req) {
        parkingService.createLot(req);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/admin/spaces")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> createSpace(@Valid @RequestBody ParkingSpaceCreateRequest req) {
        parkingService.createSpace(req);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/admin/spaces/{spaceId}/repair-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> updateRepairStatus(@PathVariable Long spaceId,
                                                                  @RequestParam String status) {
        parkingService.updateSpaceRepairStatus(spaceId, status);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

