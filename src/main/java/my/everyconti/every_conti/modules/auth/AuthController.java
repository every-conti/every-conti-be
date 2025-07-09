package my.everyconti.every_conti.modules.auth;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.exception.UnAuthorizationException;
import my.everyconti.every_conti.common.jwt.JwtMode;
import my.everyconti.every_conti.common.jwt.JwtTokenProvider;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.constant.jwt.JwtTimeout;
import my.everyconti.every_conti.modules.auth.dto.AccessTokenDto;
import my.everyconti.every_conti.modules.auth.dto.LoginDto;
import my.everyconti.every_conti.modules.auth.dto.LoginTokenDto;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    @ResponseBody
    public ResponseEntity<AccessTokenDto> login(@RequestBody LoginDto loginDto){
        LoginTokenDto loginTokenDto = authService.login(loginDto);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", loginTokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(JwtTimeout.REFRESH_TOKEN_TIMEOUT)  // 7일
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new AccessTokenDto(loginTokenDto.getAccessToken()));
    }

    @GetMapping("accessToken")
    @ResponseBody
    public ResponseEntity<AccessTokenDto> getNewAccessToken(@CookieValue(value = "refreshToken", required = false) String token) {
        if (token == null || token.isBlank()) {
            throw new UnAuthorizationException(ResponseMessage.UN_AUTHORIZED);
        }
        return ResponseEntity.ok(authService.parseTokenAndGetNewToken(token));
    }
}
