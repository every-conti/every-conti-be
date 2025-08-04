package my.everyconti.every_conti.modules.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import my.everyconti.every_conti.common.dto.response.CommonResponseDto;
import my.everyconti.every_conti.constant.jwt.JwtTimeout;
import my.everyconti.every_conti.modules.auth.dto.AccessTokenDto;
import my.everyconti.every_conti.modules.auth.dto.AccessTokenResponseDto;
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
    public ResponseEntity<CommonResponseDto> login(@RequestBody LoginDto loginDto){
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
                .body(new CommonResponseDto(true, loginTokenDto.getAccessToken()));
    }

    @GetMapping("token")
    public ResponseEntity<AccessTokenResponseDto> getNewAccessToken(@CookieValue(value = "refreshToken", required = false) String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.ok(new AccessTokenResponseDto(null, false));
        }
        AccessTokenDto accessTokenDto = authService.parseTokenAndGetNewToken(token);

        return ResponseEntity.ok(new AccessTokenResponseDto(accessTokenDto.getAccessToken(), true));
    }

    @PostMapping("logout")
    public ResponseEntity<CommonResponseDto> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true) // HTTPS 환경일 경우 true
                .path("/")    // 쿠키의 경로 설정, 설정했던 경로와 일치해야 삭제됨
                .maxAge(0)    // 즉시 만료
                .sameSite("Strict") // 필요시 Lax, None 등으로 조정
                .build();

        // 응답 헤더에 Set-Cookie 추가
        response.addHeader("Set-Cookie", deleteCookie.toString());

        return ResponseEntity.ok(new CommonResponseDto(true, "로그아웃 완료"));
    }
}
