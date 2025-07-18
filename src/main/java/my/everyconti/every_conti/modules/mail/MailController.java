package my.everyconti.every_conti.modules.mail;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.dto.response.CommonResponseDto;
import my.everyconti.every_conti.modules.mail.dto.EmailDto;
import my.everyconti.every_conti.modules.mail.dto.EmailVerifyDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/code")
    public ResponseEntity<CommonResponseDto> sendVerificationMail(@Valid @RequestBody EmailDto emailDto) {
        return mailService.sendVerificationMail(emailDto);
    }
    // 인증번호 일치여부 확인
    @GetMapping("/code/verify")
    public ResponseEntity<CommonResponseDto> verifyCode(@Valid @ModelAttribute EmailVerifyDto emailVerifyDto) {
        System.out.println("emailVerifyDto = " + emailVerifyDto.toString());
        return mailService.verifyCode(emailVerifyDto);
    }
}
