package my.everyconti.every_conti.modules.auth;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.exception.types.NotFoundException;
import my.everyconti.every_conti.common.exception.types.custom.CustomAuthException;
import my.everyconti.every_conti.common.jwt.JwtTokenProvider;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.constant.jwt.JwtMode;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.modules.auth.dto.AccessTokenDto;
import my.everyconti.every_conti.modules.auth.dto.LoginDto;
import my.everyconti.every_conti.modules.auth.dto.LoginTokenDto;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.member.MemberRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public LoginTokenDto login(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createAccessToken(JwtMode.ACCESS, authentication);
            String refreshToken = jwtTokenProvider.createRefreshToken(JwtMode.REFRESH, loginDto.getEmail());

            return new LoginTokenDto(accessToken, refreshToken);
        } catch (BadCredentialsException e) {
            throw new CustomAuthException("이메일 또는 비밀번호가 잘못되었습니다.");
        } catch (DisabledException e) {
            throw new CustomAuthException("계정이 비활성화되었습니다.");
        } catch (AuthenticationException e) {
            throw new CustomAuthException("인증에 실패했습니다.");
        }
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
