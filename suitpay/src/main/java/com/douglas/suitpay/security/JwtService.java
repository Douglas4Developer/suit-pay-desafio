// com/douglas/suitpay/security/JwtService.java
package com.douglas.suitpay.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final Key key;
    private final String issuer;
    private final Duration accessTtl;
    private final Duration refreshTtl;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.issuer:suitpay}") String issuer,
            @Value("${security.jwt.access-token-ttl:PT15M}") Duration accessTtl,
            @Value("${security.jwt.refresh-token-ttl:P7D}") Duration refreshTtl
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.accessTtl = accessTtl;
        this.refreshTtl = refreshTtl;
    }

    public String generateAccessToken(UserDetails user) {
        return buildToken(user.getUsername(), rolesOf(user), accessTtl, "access");
    }

    public String generateRefreshToken(UserDetails user) {
        return buildToken(user.getUsername(), rolesOf(user), refreshTtl, "refresh");
    }

    private String buildToken(String username, List<String> roles, Duration ttl, String type) {
        Instant now = Instant.now();
        Instant exp = now.plus(ttl);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("type", type);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .requireIssuer(issuer)
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public boolean isValid(String token, String expectedType) {
        try {
            Claims c = parse(token).getBody();
            return expectedType.equals(c.get("type", String.class))
                    && c.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String username(String token) {
        return parse(token).getBody().getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> roles(String token) {
        Object r = parse(token).getBody().get("roles");
        if (r instanceof List<?>) {
            return ((List<?>) r).stream().map(String::valueOf).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private static List<String> rolesOf(UserDetails user) {
        return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    public long accessTtlSeconds() { return accessTtl.toSeconds(); }
    public long refreshTtlSeconds() { return refreshTtl.toSeconds(); }
}