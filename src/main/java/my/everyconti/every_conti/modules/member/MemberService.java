package my.everyconti.every_conti.modules.member;

import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.exception.AlreadyExistsInDBException;
import my.everyconti.every_conti.common.exception.EmailNotVerifiedException;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.constant.redis.EmailVerified;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.dto.MemberDto;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import my.everyconti.every_conti.modules.redis.RedisService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    public Member signUp(MemberDto memberDto){
        String email = memberDto.getEmail();
        if (!redisService.getRedisValueByKey(email).equals(EmailVerified.EMAIL_VERIFIED)){
            throw new EmailNotVerifiedException(ResponseMessage.UN_AUTHORIZED);
        }
        if (memberRepository.findByEmail(memberDto.getEmail()).isPresent()){
            throw new AlreadyExistsInDBException(ResponseMessage.CONFLICT);
        }

        Member member = Member.builder()
                .nickname(memberDto.getNickname())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .email(memberDto.getEmail())
                .church(memberDto.getChurch())
                .activated(true)
                .build();

        return memberRepository.save(member);
    }
}
