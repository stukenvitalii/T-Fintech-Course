package edu.tbank.hw5.service.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${app.secret}")
    private String jwtSecret;
    @Getter
    private final int jwtExpirationMinutes = 10;
    @Getter
    private final int jwtExpirationDays = 30;

    public String generateToken(UserDetails userDetails, boolean rememberMe) {
        int expirationTime = rememberMe ? jwtExpirationDays : jwtExpirationMinutes;
        Date expiration = new Date(System.currentTimeMillis() + expirationTime * 60 * 1000);
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .expiration(expiration)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateToken(String username, boolean rememberMe) {
        int expirationTime = rememberMe ? jwtExpirationDays : jwtExpirationMinutes;
        Date expiration = new Date(System.currentTimeMillis() + expirationTime * 60 * 1000);
        return Jwts.builder()
                .subject(username)
                .expiration(expiration)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .build().parseSignedClaims(token)
                .getBody()
                .getSubject();
    }
}
