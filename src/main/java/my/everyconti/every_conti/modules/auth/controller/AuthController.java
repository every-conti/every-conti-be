package my.everyconti.every_conti.modules.auth.controller;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.constant.redis.EmailVerified;
import my.everyconti.every_conti.modules.auth.service.AuthService;
import my.everyconti.every_conti.modules.mail.service.MailService;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import my.everyconti.every_conti.modules.member.service.MemberService;
import my.everyconti.every_conti.modules.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        responseData.put("refreshToken", refreshToken);
        return ResponseEntity.ok(responseData);
    }

//    @GetMapping("accessToken")
//    public ResponseEntity<?> getAccessToken(){
//
//    }
}
