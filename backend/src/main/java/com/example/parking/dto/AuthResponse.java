package com.example.parking.dto;

public class AuthResponse {
    private String token;
    private Long userId;
    private String username;
    private String roleAuthority;

    public AuthResponse() {}

    public AuthResponse(String token, Long userId, String username, String roleAuthority) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.roleAuthority = roleAuthority;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleAuthority() {
        return roleAuthority;
    }

    public void setRoleAuthority(String roleAuthority) {
        this.roleAuthority = roleAuthority;
    }
}

