package my.everyconti.every_conti.modules.auth.controller;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.util.jwt.JwtMode;
import my.everyconti.every_conti.common.util.jwt.JwtUtil;
import my.everyconti.every_conti.constant.jwt.JwtTimeout;
import my.everyconti.every_conti.constant.redis.EmailVerified;
import my.everyconti.every_conti.modules.auth.service.AuthService;
import my.everyconti.every_conti.modules.mail.service.MailService;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import my.everyconti.every_conti.modules.member.service.MemberService;
import my.everyconti.every_conti.modules.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final MailService mailService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final JwtUtil jwtUtil;


    @PostMapping("member")
    @ResponseBody
    public ResponseEntity<Member> register(@RequestBody Map<String, String> request){
        HashMap<String, Object> map = new HashMap<>();
        String email = request.get("email");
        String nickname = request.get("nickname");
        String church = request.get("church");
        String password = request.get("church");

        if (redisService.getRedisValueByKey(email).equals(EmailVerified.EMAIL_VERIFIED)){
            String encodedPassword = passwordEncoder.encode(password);
            Member member = new Member(nickname, email, church, encodedPassword);

            memberService.createMember(member);
            return ResponseEntity.ok(member);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> request){
        String email = request.get("email");
        String password = request.get("password");

        Optional<Member> member = memberRepository.findByEmail(email);
        // 이메일 가입 기록 없으면 404
        System.out.println("member: " + Arrays.toString(member.stream().toArray()));
        if (member.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가입된 사용자가 없습니다.");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(password, member.get().getPassword())){
           // jwt 토큰 발급
        }

        HashMap<String, Object> responseData = new HashMap<>();
        String accessToken = authService.getAccessToken(email);
        String refreshToken = authService.getAccessToken(email);
        responseData.put("accessToken", accessToken);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(JwtTimeout.REFRESH_TOKEN_TIMEOUT)  // 7일
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(responseData);
    }

    @GetMapping("accessToken")
    @ResponseBody
    public ResponseEntity<?> getNewAccessToken(@CookieValue(value = "refreshToken", required = false) String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body("쿠키에 refreshToken이 없습니다.");
        }
//        public ResponseEntity<?> getNewAccessToken(@RequestHeader("Authorization") String authorizationHeader) {
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            return ResponseEntity.badRequest().body("Authorization 헤더가 유효하지 않습니다.");
//        }
//        String token = authorizationHeader.substring(7); // "Bearer " 이후 토큰 부분만 추출
        System.out.println("token = " + token);

        boolean isValid = jwtUtil.validateToken(JwtMode.ACCESS, token);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        // 이메일 추출
        String subject = jwtUtil.extractSubject(JwtMode.ACCESS, token);
        String newAccessToken = jwtUtil.createToken(JwtMode.ACCESS, subject);
        return ResponseEntity.ok().body(Map.of("accessToken", newAccessToken));
    }
}
