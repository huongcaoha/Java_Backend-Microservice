package org.example.jwt_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JWTProvider {
    @Value("${jwt.secret}")
    private String secret;



    public boolean validateToken(String token) {
        return getClaimsFromToken(token) != null ;
    }

    public Claims getClaimsFromToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
