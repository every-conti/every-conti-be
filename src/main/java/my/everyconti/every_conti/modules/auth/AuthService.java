package my.everyconti.every_conti.modules.auth;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.exception.types.NotFoundException;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.jwt.JwtMode;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.modules.auth.dto.AccessTokenDto;
import my.everyconti.every_conti.modules.auth.dto.LoginDto;
import my.everyconti.every_conti.modules.auth.dto.LoginTokenDto;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.member.MemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final HashIdUtil.JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public LoginTokenDto login(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.createAccessToken(JwtMode.ACCESS, authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(JwtMode.REFRESH, loginDto.getEmail());
        return new LoginTokenDto(accessToken, refreshToken);
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
