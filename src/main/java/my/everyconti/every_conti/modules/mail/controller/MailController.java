package my.everyconti.every_conti.modules.mail.controller;

import my.everyconti.every_conti.modules.mail.service.MailService;
import my.everyconti.every_conti.modules.redis.service.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("mail")
public class MailController {
    private final MailService mailService;
    private final RedisService redisService;

    public MailController(MailService mailService, RedisService redisService) {
        this.mailService = mailService;
        this.redisService = redisService;
    }

    @PostMapping("/code")
    @ResponseBody
    public ResponseEntity<?> sendVerificationMail(@RequestBody Map<String, String> request) {
        HashMap<String, Object> map = new HashMap<>();

        String email = request.get("email");
        return mailService.sendVerificationMail(email);
    }
    // 인증번호 일치여부 확인
    @GetMapping("/code/verify")
    @ResponseBody
    public ResponseEntity<?> verifyCode(@RequestParam String email, String userCode) {
        boolean isMatch = mailService.verifyCode(email, userCode);
        if (isMatch) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }
}
