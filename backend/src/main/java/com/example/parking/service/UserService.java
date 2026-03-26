package com.example.parking.service;

import com.example.parking.common.BusinessException;
import com.example.parking.common.ErrorCode;
import com.example.parking.dto.UserDto;
import com.example.parking.entity.SysUser;
import com.example.parking.repository.SysUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final SysUserRepository sysUserRepository;

    public UserService(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    public List<UserDto> listAll() {
        return sysUserRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void setEnabled(Long userId, boolean enabled) {
        SysUser u = sysUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "用户不存在"));
        u.setEnabled(enabled);
        sysUserRepository.save(u);
    }

    private UserDto toDto(SysUser u) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setRole(u.getRole());
        dto.setEnabled(u.isEnabled());
        dto.setDisplayName(u.getDisplayName());
        dto.setCreatedAt(u.getCreatedAt());
        return dto;
    }
}

