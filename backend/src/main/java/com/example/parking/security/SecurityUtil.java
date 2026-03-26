package com.example.parking.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static AuthUserPrincipal getCurrentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof AuthUserPrincipal) {
            return (AuthUserPrincipal) principal;
        }
        return null;
    }

    public static Long getCurrentUserId() {
        AuthUserPrincipal p = getCurrentPrincipal();
        return p == null ? null : p.getUserId();
    }

    public static String getCurrentRoleAuthority() {
        AuthUserPrincipal p = getCurrentPrincipal();
        return p == null ? null : (p.getAuthorities().isEmpty() ? null : p.getAuthorities().iterator().next().getAuthority());
    }
}

