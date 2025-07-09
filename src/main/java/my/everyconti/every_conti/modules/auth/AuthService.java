package my.everyconti.every_conti.modules.auth;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.exception.InvalidRequestException;
import my.everyconti.every_conti.common.exception.NotFoundException;
import my.everyconti.every_conti.common.exception.UnAuthorizationException;
import my.everyconti.every_conti.common.jwt.JwtMode;
import my.everyconti.every_conti.common.jwt.JwtTokenProvider;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.modules.auth.dto.AccessTokenDto;
import my.everyconti.every_conti.modules.auth.dto.LoginDto;
import my.everyconti.every_conti.modules.auth.dto.LoginTokenDto;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public LoginTokenDto login(LoginDto loginDto){
        String email = loginDto.getEmail();
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) throw new NotFoundException(ResponseMessage.USER_NOT_EXIST);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginDto.getPassword(), member.get().getPassword())){
            throw new InvalidRequestException(ResponseMessage.PASSWORD_INCONSISTENCY);
        }

        String accessToken = getNewAccessToken(email);
        String refreshToken = getNewRefreshToken(email);
        return (new LoginTokenDto(accessToken, refreshToken));
    }

    public AccessTokenDto parseTokenAndGetNewToken(String token){
        boolean isValid = jwtTokenProvider.validateToken(JwtMode.ACCESS, token);
        if (!isValid) {
            throw new UnAuthorizationException(ResponseMessage.UN_AUTHORIZED);
        }

        String email = jwtTokenProvider.extractSubject(JwtMode.ACCESS, token);
        return new AccessTokenDto(getNewAccessToken(email));
    }

    private String getNewAccessToken(String email){
        return jwtTokenProvider.createToken(JwtMode.ACCESS, email);
    }

    private String getNewRefreshToken(String email){
        return jwtTokenProvider.createToken(JwtMode.REFRESH, email);
    }
}
