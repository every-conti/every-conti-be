package my.everyconti.every_conti.modules.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.constant.redis.EmailVerified;
import my.everyconti.every_conti.constant.redis.RedisTimeout;
import my.everyconti.every_conti.modules.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;
    @Value("${spring.mail.username}")
    private String senderEmail;

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
            // 로그 추가
            e.printStackTrace();
        }
        return message;
    }

    public ResponseEntity<?> sendVerificationMail(String email){
        int code = createRandomCode();
        try {
            MimeMessage message = CreateMail(code, email);
            redisService.setRedisKeyValue(email, String.valueOf(code), RedisTimeout.EMAIL_VERIFICATION_TIMEOUT);
            javaMailSender.send(message);
        } catch (Exception e){
            // 로그 추가
            System.out.println("실패실패");
            e.printStackTrace();
        }
        return ResponseEntity.ok(true);
    }

    // 인증번호 일치여부 확인
    public boolean verifyCode(String email, String userCode) {
        String numFromRedis = redisService.getRedisValueByKey(email);
        System.out.println("numFromRedis = " + numFromRedis);

        boolean isMatch = userCode.equals(String.valueOf(numFromRedis));
        if (isMatch) redisService.setRedisKeyValue(email, EmailVerified.EMAIL_VERIFIED, RedisTimeout.EMAIL_VERIFICATION_TIMEOUT);
        return isMatch;
    }
}