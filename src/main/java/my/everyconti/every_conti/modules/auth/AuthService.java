package my.everyconti.every_conti.modules.auth;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.exception.InvalidRequestException;
import my.everyconti.every_conti.common.exception.NotFoundException;
import my.everyconti.every_conti.constant.jwt.JwtMode;
import my.everyconti.every_conti.modules.jwt.JwtTokenProvider;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.modules.auth.dto.AccessTokenDto;
import my.everyconti.every_conti.modules.auth.dto.LoginDto;
import my.everyconti.every_conti.modules.auth.dto.LoginTokenDto;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public LoginTokenDto login(LoginDto loginDto){
        String email = loginDto.getEmail();
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) throw new NotFoundException(ResponseMessage.USER_NOT_EXIST);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginDto.getPassword(), member.get().getPassword())){
            throw new InvalidRequestException(ResponseMessage.PASSWORD_INCONSISTENCY);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 해당 객체를 SecurityContextHolder에 저장하고
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.createAccessToken(JwtMode.ACCESS, authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(JwtMode.REFRESH, email);
        return (new LoginTokenDto(accessToken, refreshToken));
    }

    public AccessTokenDto parseTokenAndGetNewToken(String token){
        String email = jwtTokenProvider.extractSubject(JwtMode.REFRESH, token);

        // 사용자 권한이 필요하다면 여기서 로드
        Member member = memberRepository.findOneWithRolesByEmail(email)
                .orElseThrow(() -> new NotFoundException(ResponseMessage.USER_NOT_EXIST));

        // Authentication 객체를 만들기 위한 코드

        List<GrantedAuthority> roles = member.getMemberRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getRoleName().toString()))
                .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, roles);

        return new AccessTokenDto(jwtTokenProvider.createAccessToken(JwtMode.ACCESS, authentication));
    }
}
