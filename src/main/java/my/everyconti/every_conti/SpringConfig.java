package my.everyconti.every_conti;

import my.everyconti.every_conti.aop.TimeTraceAop;
import my.everyconti.every_conti.mail.service.MailService;
import my.everyconti.every_conti.member.repository.MemberRepository;
import my.everyconti.every_conti.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class SpringConfig {
    private MemberRepository memberRepository;
    private JavaMailSender jms;

    @Autowired
    public SpringConfig(MemberRepository memberRepository, JavaMailSender jms) {
        this.memberRepository = memberRepository;
        this.jms = jms;
    }

    @Bean
    public MemberService userService(){
        return new MemberService(memberRepository);
    }

    @Bean
    public MailService mailService(JavaMailSender jms){ return new MailService(jms); }

//    @Bean
//    public TimeTraceAop timeTraceAop(){
//        return this.timeTraceAop();
//    }
}
