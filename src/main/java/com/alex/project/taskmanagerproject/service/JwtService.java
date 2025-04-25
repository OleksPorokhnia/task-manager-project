package com.alex.project.taskmanagerproject.service;

import com.alex.project.taskmanagerproject.util.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String generateToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, CustomUserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    public String extractUsername(String token){
        System.out.println(extractAllClaims(token).getSubject());
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] decodedKey = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}
