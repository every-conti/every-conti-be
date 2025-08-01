package my.everyconti.every_conti.config;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.aop.logging.LoggingAop;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.common.exception.JwtAccessDeniedHandler;
import my.everyconti.every_conti.common.exception.JwtAuthenticationEntryPoint;
import my.everyconti.every_conti.common.filter.JwtFilter;
import my.everyconti.every_conti.config.properties.JwtProperties;
import my.everyconti.every_conti.config.properties.WebProperties;
import my.everyconti.every_conti.constant.role.RoleType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class, WebProperties.class})
public class SpringConfig {

    private final HashIdUtil.JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final WebProperties webProperties;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public HashIdUtil hashIdUtil() { return new HashIdUtil(); }

    @Bean
    public LoggingAop loggingAspect() { return new LoggingAop(); }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(webProperties.getOrigins());
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpBasic -> httpBasic.disable()) // 기본 인증 비활성화
                .formLogin(form -> form.disable())
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

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

                // 요청 인가 설정
                .authorizeHttpRequests(auth -> auth
                            // auth
                            .requestMatchers("/api/auth/**").permitAll()
                            // logging
                            .requestMatchers("/api/logging/**").hasAuthority(RoleType.ROLE_ADMIN.toString())
                            // mail
                            .requestMatchers("/api/mail/**").permitAll()
                            // member
                            .requestMatchers("/api/member/**").permitAll()
                            // song
                            .requestMatchers("/api/song/lists", "api/song/properties", "/api/song/search/**").permitAll()
                            // conti
                            .requestMatchers("/api/conti/{contiId}", "/api/praise-teams/last-conti").permitAll()
                            // bible
                            .requestMatchers("/api/bible/**").permitAll()


                            // dev(html)
                             .requestMatchers("**").permitAll()

                            // preflight 허용
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            // etc
                            .anyRequest().hasAnyAuthority(RoleType.ROLE_ADMIN.toString(), RoleType.ROLE_USER.toString())
                )

                // JWT 필터 등록
                .addFilterBefore(new JwtFilter(jwtTokenProvider, jwtProperties), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
