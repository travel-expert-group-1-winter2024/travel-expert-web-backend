package org.example.travelexpertwebbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.tinylog.Logger;


@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService() {
        this.secretKey = generateSecretKey();
    }

    // generate secret key to sign JWT tokens
    public SecretKey generateSecretKey() {
        // generate a random key
        SecretKey key = Jwts.SIG.HS512.key().build();

        // convert the key to string for printing
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        Logger.debug("Encoded Key: " + encodedKey);

        // convert byte to a key
        return Keys.hmacShaKeyFor(key.getEncoded());
    }

    // generate token
    public String generateToken(UserDetails userDetails) {
        // use hashmap to store claims (user defined key-value
        // pair that are added to playload)
        // claim.put("iss", "oosdwinter2025");
        Map<String, String> claims = new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(60 * 60))) // 1 hr
                .signWith(secretKey)
                .compact();
    }

    public String getUsernameFromToken(String jwt) {
        Claims claims = getPayload(jwt);
        // extract username from the claims
        return claims.getSubject();
    }

    private Claims getPayload(String jwt) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public boolean isTokenNotExpired(String jwt) {
        Claims claims = getPayload(jwt);
        return claims.getExpiration().toInstant().isAfter(Instant.now());
    }
}
