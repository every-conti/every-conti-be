package my.everyconti.every_conti.common.util.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import my.everyconti.every_conti.constant.jwt.JwtTimeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${spring.jwt.access-secret-key}")
    private String accessSecret;
    @Value("${spring.jwt.refresh-secret-key}")
    private String refreshSecret;

    private Key accessSecretKey;
    private Key refreshSecretKey;

    @PostConstruct
    public void init() {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(JwtMode jwtMode, String subject) {
        int timeout = getTimeout(jwtMode);
        Key key = getKey(jwtMode);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .signWith(key)
                .compact();
    }

    public String extractSubject(JwtMode jwtMode, String token) {
        Key key = getKey(jwtMode);

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public boolean validateToken(JwtMode jwtMode, String token) {
        Key key = getKey(jwtMode);

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Key getKey(JwtMode jwtMode){
        Key key;
        switch (jwtMode){
            case ACCESS -> key = accessSecretKey;
            case REFRESH -> key = refreshSecretKey;
            default -> key = accessSecretKey;
        }
        return key;
    }
    private int getTimeout(JwtMode jwtMode){
        int timeout;
        switch (jwtMode){
            case ACCESS ->
                timeout = JwtTimeout.ACCESS_TOKEN_TIMEOUT;
            case REFRESH -> timeout = JwtTimeout.REFRESH_TOKEN_TIMEOUT;
            default -> timeout = JwtTimeout.ACCESS_TOKEN_TIMEOUT;
        }
        return timeout;
    }
}