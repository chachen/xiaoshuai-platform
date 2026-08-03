package io.github.chachen.platform.auth;

import io.github.chachen.platform.core.auth.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

public class JwtTokenService {
    private final AuthProperties properties;
    private final SecretKey key;

    public JwtTokenService(AuthProperties p) {
        properties = p;
        key = Keys.hmacShaKeyFor(p.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String accessToken(Account a) {
        return create(a, properties.getAccessTtlSeconds(), "access");
    }

    public String refreshToken(Account a) {
        return create(a, properties.getRefreshTtlSeconds(), "refresh");
    }

    private String create(Account a, long seconds, String type) {
        Instant now = Instant.now();
        return Jwts.builder().subject(a.username()).claim("uid", a.id()).claim("type", type).claim("permissions", a.permissions()).issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(seconds))).signWith(key).compact();
    }

    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public boolean isRefresh(Claims claims) {
        return "refresh".equals(claims.get("type", String.class));
    }
}
