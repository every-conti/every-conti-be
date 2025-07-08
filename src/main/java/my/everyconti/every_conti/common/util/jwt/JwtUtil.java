package my.everyconti.every_conti.common.util.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import my.everyconti.every_conti.constant.jwt.JwtTimeout;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String createToken(JwtMode jwtMode, String subject) {
        int timeout;
        switch (jwtMode){
            case ACCESS -> timeout = JwtTimeout.ACCESS_TOKEN_TIMEOUT;
            case REFRESH -> timeout = JwtTimeout.REFRESH_TOKEN_TIMEOUT;
            default -> timeout = JwtTimeout.ACCESS_TOKEN_TIMEOUT;
        }

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
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

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}