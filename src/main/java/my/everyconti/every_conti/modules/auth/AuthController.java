package my.everyconti.every_conti.modules.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.exception.UnAuthorizationException;
import my.everyconti.every_conti.common.jwt.JwtTokenProvider;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.constant.jwt.JwtTimeout;
import my.everyconti.every_conti.modules.auth.dto.AccessTokenDto;
import my.everyconti.every_conti.modules.auth.dto.LoginDto;
import my.everyconti.every_conti.modules.auth.dto.LoginTokenDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

//    @PostMapping("/authenticate")
//    public ResponseEntity<AccessTokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
//
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
//
//        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        // 해당 객체를 SecurityContextHolder에 저장하고
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
//        String jwt = tokenProvider.createToken(JwtMode.ACCESS, authentication);
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        // response header에 jwt token에 넣어줌
//        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
//
//        // tokenDto를 이용해 response body에도 넣어서 리턴
//        return new ResponseEntity<>(new AccessTokenDto(jwt), httpHeaders, HttpStatus.OK);
//    }

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
    public ResponseEntity<AccessTokenDto> getNewAccessToken(@CookieValue(value = "refreshToken") String token) {
        if (token == null || token.isBlank()) {
            throw new UnAuthorizationException(ResponseMessage.UN_AUTHORIZED);
        }
        return ResponseEntity.ok(authService.parseTokenAndGetNewToken(token));
    }
}
