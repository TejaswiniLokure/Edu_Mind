package com.nimblix.SchoolPEPProject.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 🔹 Generate Token
    public String generateToken(UserDetails email) {
        return Jwts.builder()
                .setSubject(email.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)
                ) // 7 days
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 Extract Username (email)
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // 🔹 Validate Token (ONLY JWT LEVEL)
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 🔹 Check Expiration
    public boolean isTokenExpired(String token) {
        return parseClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
