package my.everyconti.every_conti.auth.controller;

import my.everyconti.every_conti.mail.service.MailService;
import my.everyconti.every_conti.member.domain.Member;
import my.everyconti.every_conti.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller("auth")
public class AuthController {
    private final MemberService memberService;
    private final MailService mailService;

    @Autowired
    public AuthController(MemberService memberService, MailService mailService) {
        this.memberService = memberService;
        this.mailService = mailService;
    }

    @PostMapping("member")
    @ResponseBody
    public void register(@RequestBody Map<String, String> request){
        HashMap<String, Object> map = new HashMap<>();
        String email = request.get("email");
        String nickname = request.get("nickname");
        String church = request.get("church");

        Member member = new Member(nickname, email, church);
        //        memberService.createMember(member);
    }
}
