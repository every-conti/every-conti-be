package my.everyconti.every_conti.modules.auth;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.exception.types.UnAuthorizationException;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.constant.jwt.JwtTimeout;
import my.everyconti.every_conti.modules.auth.dto.AccessTokenDto;
import my.everyconti.every_conti.modules.auth.dto.LoginDto;
import my.everyconti.every_conti.modules.auth.dto.LoginTokenDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<AccessTokenDto> login(@RequestBody LoginDto loginDto){
        LoginTokenDto loginTokenDto = authService.login(loginDto);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", loginTokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(JwtTimeout.REFRESH_TOKEN_TIMEOUT)  // 30일
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new AccessTokenDto(loginTokenDto.getAccessToken()));
    }

    @GetMapping("token")
    public ResponseEntity<AccessTokenDto> getNewAccessToken(@CookieValue(value = "refreshToken") String token) {
        if (token == null || token.isBlank()) {
            throw new UnAuthorizationException(ResponseMessage.UN_AUTHORIZED);
        }
        return ResponseEntity.ok(authService.parseTokenAndGetNewToken(token));
    }
}
