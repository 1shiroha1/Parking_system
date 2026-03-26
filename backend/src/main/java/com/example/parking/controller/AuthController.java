package com.example.parking.controller;

import com.example.parking.common.ApiResponse;
import com.example.parking.dto.AuthResponse;
import com.example.parking.dto.LoginRequest;
import com.example.parking.dto.RegisterRequest;
import com.example.parking.security.AuthUserPrincipal;
import com.example.parking.security.SecurityUtil;
import com.example.parking.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(req)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Object>> me() {
        AuthUserPrincipal p = SecurityUtil.getCurrentPrincipal();
        return ResponseEntity.ok(ApiResponse.success(new Object() {
            public Long userId = p == null ? null : p.getUserId();
            public String username = p == null ? null : p.getUsername();
            public String roleAuthority = p == null ? null : (p.getAuthorities().isEmpty() ? null : p.getAuthorities().iterator().next().getAuthority());
        }));
    }
}

