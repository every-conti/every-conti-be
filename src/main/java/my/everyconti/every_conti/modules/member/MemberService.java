package my.everyconti.every_conti.modules.member;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.exception.types.*;
import my.everyconti.every_conti.common.exception.types.custom.EmailNotVerifiedException;
import my.everyconti.every_conti.common.utils.SecurityUtil;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.constant.redis.EmailVerified;
import my.everyconti.every_conti.constant.role.RoleType;
import my.everyconti.every_conti.modules.auth.domain.Role;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.domain.MemberRole;
import my.everyconti.every_conti.modules.member.dto.MemberDto;
import my.everyconti.every_conti.modules.member.dto.SignUpDto;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import my.everyconti.every_conti.modules.redis.RedisService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    public MemberDto signUp(SignUpDto signUpDto){
        String email = signUpDto.getEmail();
        if (!redisService.getRedisValueByKey(email).equals(EmailVerified.EMAIL_VERIFIED)){
            throw new EmailNotVerifiedException(ResponseMessage.UN_AUTHORIZED);
        }
        if (memberRepository.findByEmail(signUpDto.getEmail()).isPresent()){
            throw new AlreadyExistElementException(ResponseMessage.CONFLICT);
        }
        Member member = Member.builder()
                .nickname(signUpDto.getNickname())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .email(signUpDto.getEmail())
                .church(signUpDto.getChurch())
                .activated(true)
                .build();

        Role role = Role.builder()
                .roleName(RoleType.ROLE_USER)
                .build();
        MemberRole memberRole = new MemberRole();
        memberRole.setRole(role);
        memberRole.setMember(member);

        member.setMemberRoles(Collections.singleton(memberRole));

        Member newMember = memberRepository.save(member);
        return new MemberDto(newMember);
    }

    @Transactional(readOnly = true)
    public MemberDto getMyUserWithRoles(String email) {
        // 본인/admin인지 확인
        SecurityUtil.userMatchOrAdmin(email);

        Optional<Member> member = memberRepository.findOneWithRolesByEmail(email);
        if (member.isEmpty()) throw new NotFoundException(ResponseMessage.NOT_FOUND);

        return new MemberDto(member.get());
    }
}
