package com.example.parking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secret;
    private final long expireSeconds;

    public JwtUtil(@Value("${parking.jwt.secret}") String secret,
                    @Value("${parking.jwt.expire-seconds}") long expireSeconds) {
        this.secret = secret;
        this.expireSeconds = expireSeconds;
    }

    public String generateToken(Long userId, String username, String roleAuthority) {
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + expireSeconds * 1000);
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", roleAuthority)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}

