package my.everyconti.every_conti;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AndRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
//@EnableWebSecurity
@RequiredArgsConstructor
public class SpringConfig {
//
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
////        http.csrf().disable();
//           http.authorizeHttpRequests().requestMatchers(new AndRequestMatcher("/*")).permitAll();
////        return http.build();
//    }
    //    private MemberRepository memberRepository;
//    private JavaMailSender jms;
//
//    @Autowired
//    public SpringConfig(MemberRepository memberRepository, JavaMailSender jms) {
//        this.memberRepository = memberRepository;
//        this.jms = jms;
//    }



//    @Bean
//    public TimeTraceAop timeTraceAop(){
//        return this.timeTraceAop();
//    }
}
