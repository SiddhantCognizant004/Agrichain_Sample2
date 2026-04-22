package com.cts.Registration_Service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Generates a token with just the subject (email).
     */
    public String generateToken(String email) {
        return generateToken(new HashMap<>(), email);
    }

    /**
     * Generates a token including the user's role.
     */
    public String generateToken(String email, String role) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role);
        return generateToken(extraClaims, email);
    }

    /**
     * Core logic to build the JWT.
     */
    public String generateToken(Map<String, Object> extraClaims, String email) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 Hours
                .signWith(getSignKey())
                .compact();
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractUserName(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}