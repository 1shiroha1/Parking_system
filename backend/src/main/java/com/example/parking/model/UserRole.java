package com.example.parking.model;

public enum UserRole {
    ADMIN,
    USER,
    REPAIR;

    public String authority() {
        return "ROLE_" + name();
    }
}

