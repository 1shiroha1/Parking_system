package com.example.parking.controller;

import com.example.parking.common.ApiResponse;
import com.example.parking.dto.EnabledRequest;
import com.example.parking.dto.UserDto;
import com.example.parking.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> list() {
        return ResponseEntity.ok(ApiResponse.success(userService.listAll()));
    }

    @PatchMapping("/api/admin/users/{id}/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> setEnabled(@PathVariable("id") Long userId,
                                                            @Valid @RequestBody EnabledRequest req) {
        userService.setEnabled(userId, req.getEnabled());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

