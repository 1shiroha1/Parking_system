package com.example.parking.service;

import com.example.parking.common.BusinessException;
import com.example.parking.common.ErrorCode;
import com.example.parking.dto.AuthResponse;
import com.example.parking.dto.LoginRequest;
import com.example.parking.dto.RegisterRequest;
import com.example.parking.entity.SysUser;
import com.example.parking.model.UserRole;
import com.example.parking.repository.SysUserRepository;
import com.example.parking.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(SysUserRepository sysUserRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil) {
        this.sysUserRepository = sysUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest req) {
        sysUserRepository.findByUsername(req.getUsername()).ifPresent(u -> {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "用户名已存在");
        });

        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(UserRole.USER);
        user.setDisplayName(req.getDisplayName() == null ? req.getUsername() : req.getDisplayName());
        sysUserRepository.save(user);
    }

    public AuthResponse login(LoginRequest req) {
        SysUser user = sysUserRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST, "用户名或密码错误"));
        if (!user.isEnabled()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "账号被禁用");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "用户名或密码错误");
        }

        return new AuthResponse(
                jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().authority()),
                user.getId(),
                user.getUsername(),
                user.getRole().authority()
        );
    }
}

