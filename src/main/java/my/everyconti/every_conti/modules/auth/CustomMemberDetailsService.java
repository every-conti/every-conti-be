package my.everyconti.every_conti.modules.auth;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    // 로그인시에 DB에서 유저정보와 권한정보를 가져와서 해당 정보를 기반으로 userdetails.User 객체를 생성해 리턴
    public UserDetails loadUserByUsername(final String email) {

        return memberRepository.findOneWithRolesByEmail(email)
                .map(member -> createUser(email, member))
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String email, Member member) {
        if (!member.isActivated()) {
            throw new RuntimeException(email + " -> 활성화되어 있지 않습니다.");
        }

        List<GrantedAuthority> grantedAuthorities = member.getMemberRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getRoleName().toString()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(member.getEmail(),
                member.getPassword(),
                grantedAuthorities);
    }
}
