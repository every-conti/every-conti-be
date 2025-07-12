package my.everyconti.every_conti.config;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.aop.logging.LoggingAop;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.common.utils.jwt.error.JwtAccessDeniedHandler;
import my.everyconti.every_conti.common.utils.jwt.error.JwtAuthenticationEntryPoint;
import my.everyconti.every_conti.common.utils.jwt.JwtFilter;
import my.everyconti.every_conti.common.utils.jwt.JwtTokenProvider;
import my.everyconti.every_conti.config.properties.JwtProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class SpringConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoggingAop loggingAspect() { return new LoggingAop(); }

    @Bean
    public HashIdUtil hashIdUtil() { return new HashIdUtil(); }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpBasic -> httpBasic.disable()) // 기본 인증 비활성화
                .formLogin(form -> form.disable())
                .csrf(csrf -> csrf.disable())

                // 예외처리
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                // H2 Console 프레임 허용
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                // 세션 정책 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 요청 인증 설정
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/member/{email}").hasAuthority(RoleType.ROLE_ADMIN.toString())
                        .requestMatchers("/api/auth/login", "/api/song/lists").permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated()
                )

                // JWT 필터 등록
                .addFilterBefore(new JwtFilter(jwtTokenProvider, jwtProperties), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
