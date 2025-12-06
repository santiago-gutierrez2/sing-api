package com.sign.signApi.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HexFormat;


@Component
public class JwtUtil {

    @Value("${jwt.key}")
    private String keyValue;
    private Key singinKey;

    @Value("${jwt.token-duration}")
    private Long jwtTokenDuration;

    @PostConstruct
    public void init() {
        byte[] keyBytes = HexFormat.of().parseHex(keyValue);
        singinKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtTokenDuration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(singinKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(singinKey).build().parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException e) {
            return null;
        }

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(singinKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
