package my.everyconti.every_conti.modules.auth.service;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.util.jwt.JwtMode;
import my.everyconti.every_conti.common.util.jwt.JwtUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    public String getAccessToken(String email){
        return jwtUtil.createToken(JwtMode.ACCESS, email);
    }

    public String getRefreshToken(String email){
        return jwtUtil.createToken(JwtMode.REFRESH, email);
    }
}
