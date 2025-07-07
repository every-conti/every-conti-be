package my.everyconti.every_conti.modules.auth.controller;

import my.everyconti.every_conti.constant.redis.EmailVerified;
import my.everyconti.every_conti.modules.mail.service.MailService;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.service.MemberService;
import my.everyconti.every_conti.modules.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("auth")
public class AuthController {
    private final MemberService memberService;
    private final MailService mailService;
    private final RedisService redisService;


    @Autowired
    public AuthController(MemberService memberService, MailService mailService, RedisService redisService) {
        this.memberService = memberService;
        this.mailService = mailService;
        this.redisService = redisService;
    }

    @PostMapping("member")
    @ResponseBody
    public ResponseEntity<Member> register(@RequestBody Map<String, String> request){
        HashMap<String, Object> map = new HashMap<>();
        String email = request.get("email");
        String nickname = request.get("nickname");
        String church = request.get("church");

        if (redisService.getRedisValueByKey(email).equals(EmailVerified.EMAIL_VERIFIED)){
            Member member = new Member(nickname, email, church);

            memberService.createMember(member);
            return ResponseEntity.ok(member);
        }
        return ResponseEntity.badRequest().build();
    }
}
