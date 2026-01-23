package com.subString.Auth.Auth_app.Security;


import com.subString.Auth.Auth_app.entities.Role;
import com.subString.Auth.Auth_app.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Service
public class JWTService {

    private final SecretKey secretKey;
    private final long accessTokenValiditySeconds;
    private final long refreshTokenValiditySeconds;
    private final String issuer;

    public JWTService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-ttl-seconds}") long accessTokenValiditySeconds,
            @Value("${security.jwt.refresh-ttl-seconds}") long refreshTokenValiditySeconds,
            @Value("${security.jwt.issuer}") String issuer) {

        if (secret == null || secret.length() < 32) { // ✅ HS256 needs 32+
            throw new IllegalArgumentException("JWT secret key must be at least 32 characters");
        }

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.issuer = issuer;
    }

    // ACCESS TOKEN
    public String generateToken(User user) {
        Instant now = Instant.now();

        List<String> roles = user.getRoles() == null ? List.of() :
                user.getRoles().stream()
                        .map(Role::getName)
                        .toList();

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getEmail())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTokenValiditySeconds)))
                .claim("email", user.getEmail())
                .claim("roles", roles) // ✅ FIXED
                .claim("typ", "access")
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // REFRESH TOKEN
    public String generateRefreshToken(User user, String jti) {
        Instant now = Instant.now();

        return Jwts.builder()
                .id(jti)
                .subject(user.getEmail())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshTokenValiditySeconds)))
                .claim("typ", "refresh")
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }

    public boolean isAccessToken(String token) {
        return "access".equals(parse(token).getPayload().get("typ"));
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(parse(token).getPayload().get("typ"));
    }

    public String getEmail(String token) {
        return parse(token).getPayload().getSubject(); // ✅ simple
    }

    public List<String> getRoles(String token) {
        return (List<String>) parse(token).getPayload().get("roles");
    }

    public String getJti(String token) {
        return parse(token).getPayload().getId();
    }
}
