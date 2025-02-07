package com.example.twitter.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.expiration}00")
    private Long EXPIRATION_TIME;
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String generateToken(String username) {
        JwtBuilder builder = Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .subject(username)
                .signWith(getSignKey());

        if (EXPIRATION_TIME > 0) {
            builder.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        }

        return builder.compact();
    }


    public boolean verifyToken(String token) {
        // claim => Jwt'deki her bir özellik.
        Claims claims = Jwts
                .parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        // Eğer ilgili secret key ile doğrulanırsa jwt içindeki bilgileri verir.
        return claims.getExpiration().after(new Date());
    }


    public String extractUsername(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    // Boilerplate code
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
