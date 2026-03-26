package com.example.parking.controller;

import com.example.parking.common.ApiResponse;
import com.example.parking.dto.ReservationCancelRequest;
import com.example.parking.dto.ReservationCreateRequest;
import com.example.parking.dto.ReservationDto;
import com.example.parking.entity.Reservation;
import com.example.parking.security.SecurityUtil;
import com.example.parking.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<ReservationDto>> create(@Valid @RequestBody ReservationCreateRequest req) {
        Long userId = SecurityUtil.getCurrentUserId();
        Reservation r = reservationService.createReservation(userId, req);
        return ResponseEntity.ok(ApiResponse.success(toDto(r)));
    }

    @GetMapping("/reservations/my")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<ReservationDto>>> my() {
        Long userId = SecurityUtil.getCurrentUserId();
        String role = SecurityUtil.getCurrentRoleAuthority();
        boolean isAdmin = role != null && role.contains("ADMIN");
        List<Reservation> list = isAdmin ? reservationService.listAll() : reservationService.listMy(userId);
        return ResponseEntity.ok(ApiResponse.success(list.stream().map(this::toDto).collect(Collectors.toList())));
    }

    @PostMapping("/reservations/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<Object>> cancel(@PathVariable("id") Long reservationId,
                                                       @Valid @RequestBody ReservationCancelRequest req) {
        Long userId = SecurityUtil.getCurrentUserId();
        reservationService.cancelReservation(userId, reservationId, req.getCancelReason());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/admin/reservations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ReservationDto>>> all() {
        return ResponseEntity.ok(ApiResponse.success(
                reservationService.listAll().stream().map(this::toDto).collect(Collectors.toList())
        ));
    }

    private ReservationDto toDto(Reservation r) {
        ReservationDto dto = new ReservationDto();
        dto.setId(r.getId());
        dto.setUserId(r.getUserId());
        dto.setSpaceId(r.getSpaceId());
        dto.setStartTime(r.getStartTime());
        dto.setEndTime(r.getEndTime());
        dto.setStatus(r.getStatus().name());
        dto.setCancelReason(r.getCancelReason());
        return dto;
    }
}

