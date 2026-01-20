package com.subString.Auth.Auth_app.Security;


import com.subString.Auth.Auth_app.entities.Role;
import com.subString.Auth.Auth_app.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Service
public class JWTService {
    private final SecretKey secretKey;
    private final long accessTokenValiditySeconds;
    private final long refreshTokenValiditySeconds;
    private final String issuer;

    public JWTService(
            @Value("${security.jwt.secret}") String secretKey,
            @Value("${security.jwt.access-ttl-seconds}") long accessTokenValiditySeconds, @Value("${security.jwt.refresh-ttl-seconds}") long refreshTokenValiditySeconds, @Value("${security.jwt.issuer}") String issuer) {

        if (secretKey == null || secretKey.length() < 64) {
            throw new IllegalArgumentException("Invalid secret key");
        }
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.issuer = issuer;
    }

    // generating the token
    public String generateToken(User user) {
        Instant now = Instant.now();
        List<String> roles = user.getRoles() == null ? List.of() :
                user.getRoles().stream().map(Role::getName).toList();

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getUserId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTokenValiditySeconds)))
                .claims(
                        Map.of(
                                "email", user.getEmail(),
                                "roles", user.getRoles(),
                                "typ", "access"
                        )
                ).signWith(secretKey, SignatureAlgorithm.HS256).compact();


    }

    // generating the refresh Token

    public String generateRefreshToken(User user, String jid) {
        Instant now = Instant.now();
        List<String> roles = user.getRoles() == null ? List.of() :
                user.getRoles().stream().map(Role::getName).toList();

        return Jwts.builder()
                .id(jid)
                .subject(user.getUserId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTokenValiditySeconds)))
                .claim("typ", "refresh")
                .signWith(secretKey, SignatureAlgorithm.HS256).compact();

    }

    // parsing the token
    public Jws<Claims> parse(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isAccessToken(String token) {
        Claims c = parse(token).getPayload();
        return "access".equals(c.get("typ"));
    }

    public boolean isRefreshToken(String token) {
        Claims c = parse(token).getPayload();
        return "refresh".equals(c.get("typ"));
    }

    public UUID getUserId(String token) {
        Claims c = parse(token).getPayload();
        return UUID.fromString(c.getSubject());
    }

    public String getJti(String token) {
        return parse(token).getPayload().getId();
     }


}
