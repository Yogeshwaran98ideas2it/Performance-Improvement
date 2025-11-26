package com.acme.healthcare.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Issues and validates JWT tokens used across services.
 */
@Component
public class JwtTokenProvider {

    private final JwtTokenProperties properties;
    private final SecretKey secretKey;

    /**
     * Creates the provider.
     *
     * @param properties token configuration
     */
    public JwtTokenProvider(final JwtTokenProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates an access token for the supplied principal and authorities.
     *
     * @param username the principal username
     * @param authorities the authorities granted to the principal
     * @return signed JWT token
     */
    public String generateAccessToken(final String username, final Set<String> authorities) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(properties.getAccessTokenTtlMinutes() * 60);
        return Jwts.builder()
            .setIssuer(properties.getIssuer())
            .setSubject(username)
            .claim("authorities", authorities)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .signWith(secretKey)
            .compact();
    }

    /**
     * Generates a refresh token.
     *
     * @return refresh JWT token
     */
    public String generateRefreshToken() {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(properties.getRefreshTokenTtlHours() * 3600);
        return Jwts.builder()
            .setIssuer(properties.getIssuer())
            .setSubject("refresh")
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiry))
            .signWith(secretKey)
            .compact();
    }

    /**
     * Validates that the token belongs to the provided user and is not expired.
     *
     * @param token JWT token
     * @param userDetails user details to validate against
     * @return true if token is valid
     */
    public boolean validateToken(final String token, final UserDetails userDetails) {
        String username = getUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    /**
     * Extracts the username from token claims.
     *
     * @param token JWT token
     * @return username
     */
    public String getUsername(final String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extracts the authorities claim as a set of strings.
     *
     * @param token JWT token
     * @return authorities set
     */
    public Set<String> getAuthorities(final String token) {
        Claims claims = parseClaims(token);
        Object authorities = claims.get("authorities");
        if (authorities instanceof Iterable<?> iterable) {
            Set<String> result = new HashSet<>();
            for (Object value : iterable) {
                result.add(String.valueOf(value));
            }
            return result;
        }
        return Set.of();
    }

    private boolean isExpired(final String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims parseClaims(final String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}











