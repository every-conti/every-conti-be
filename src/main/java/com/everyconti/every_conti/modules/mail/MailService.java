package com.everyconti.every_conti.modules.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import com.everyconti.every_conti.common.dto.response.CommonResponseDto;
import com.everyconti.every_conti.constant.redis.EmailVerified;
import com.everyconti.every_conti.constant.redis.RedisTimeout;
import com.everyconti.every_conti.common.filter.JwtFilter;
import com.everyconti.every_conti.modules.mail.dto.EmailDto;
import com.everyconti.every_conti.modules.mail.dto.EmailVerifyDto;
import com.everyconti.every_conti.modules.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;
    @Value("${spring.mail.username}")
    private String senderEmail;
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);


    private static int createRandomCode(){
        return (int)(Math.random() * (90000)) + 100000;
    }

    private MimeMessage CreateMail(int code, String mail){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("[에브리콘티] 이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + code + "</h1>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e){
            log.info("이메일 발송 실패");
        }
        return message;
    }

    public CommonResponseDto sendVerificationMail(EmailDto emailDto){
        int code = createRandomCode();
        String email = emailDto.getEmail();
        try {
            MimeMessage message = CreateMail(code, email);
            redisService.setRedisKeyValue(email, String.valueOf(code), RedisTimeout.EMAIL_VERIFICATION_TIMEOUT);
            javaMailSender.send(message);
        } catch (Exception e){
            throw new RuntimeException("이메일 발송 실패");
        }
        return new CommonResponseDto(true, "이메일 발송 완료");
    }

    public CommonResponseDto verifyCode(EmailVerifyDto emailVerifyDto) {
        String email = emailVerifyDto.getEmail();
        String numFromRedis = redisService.getRedisValueByKey(email);

        boolean isMatch = emailVerifyDto.getUserCode().equals(String.valueOf(numFromRedis));

        if (!isMatch) return new CommonResponseDto(false, "이메일 인증 실패");
        redisService.setRedisKeyValue(email, EmailVerified.EMAIL_VERIFIED, RedisTimeout.EMAIL_VERIFICATION_TIMEOUT);

        return new CommonResponseDto(true, String.format("이메일 인증 성공. %d분 간 유효합니다", (int) (RedisTimeout.EMAIL_VERIFICATION_TIMEOUT / 60)));
    }
}