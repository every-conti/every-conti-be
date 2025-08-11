package my.everyconti.every_conti.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import my.everyconti.every_conti.constant.jwt.JwtMode;
import my.everyconti.every_conti.constant.jwt.JwtTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String AUTHORITIES_KEY = "roles";

    @Value("${security.jwt.access-secret-key}")
    private String accessSecret;
    @Value("${security.jwt.refresh-secret-key}")
    private String refreshSecret;

    private Key accessSecretKey;
    private Key refreshSecretKey;

    @Override
    public void afterPropertiesSet() {
        byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecret);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecret);
        this.accessSecretKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public String createAccessToken(JwtMode jwtMode, Authentication authentication) {
        long timeout = getTimeoutByMode(jwtMode);
        Key key = getKeyByMode(jwtMode);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    public String createRefreshToken(JwtMode jwtMode, String subject) {
        long timeout = getTimeoutByMode(jwtMode);
        Key key = getKeyByMode(jwtMode);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractSubject(JwtMode jwtMode, String token) {
        Key key = getKeyByMode(jwtMode);

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
        Key key = getKeyByMode(jwtMode);

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.debug("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.debug("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.debug("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.debug("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Key key = getKeyByMode(JwtMode.ACCESS);

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private Key getKeyByMode(JwtMode jwtMode){
        Key key;
        switch (jwtMode){
            case ACCESS -> key = accessSecretKey;
            case REFRESH -> key = refreshSecretKey;
            default -> key = accessSecretKey;
        }
        return key;
    }
    private long getTimeoutByMode(JwtMode jwtMode){
        long timeout;
        switch (jwtMode){
            case ACCESS ->
                    timeout = JwtTimeout.ACCESS_TOKEN_TIMEOUT;
            case REFRESH -> timeout = JwtTimeout.REFRESH_TOKEN_TIMEOUT;
            default -> timeout = JwtTimeout.ACCESS_TOKEN_TIMEOUT;
        }
        return timeout;
    }
}