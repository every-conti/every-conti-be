package my.everyconti.every_conti.mail.controller;

import my.everyconti.every_conti.mail.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller("mail")
public class MailController {
    private final MailService mailService;
    private int number;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/mailSend")
    @ResponseBody
    public HashMap<String, Object> mailSend(@RequestBody Map<String, String> request) {
        HashMap<String, Object> map = new HashMap<>();
        String mail = request.get("mail");

        try {
            number = mailService.sendMail(mail);
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    // 인증번호 일치여부 확인
    @GetMapping("/mailCheck")
    @ResponseBody
    public ResponseEntity<?> mailCheck(@RequestParam String userNumber) {

        boolean isMatch = userNumber.equals(String.valueOf(number));

        return ResponseEntity.ok(isMatch);
    }
}
