package my.everyconti.every_conti.modules.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.common.dto.response.CommonResponseDto;
import my.everyconti.every_conti.common.exception.types.*;
import my.everyconti.every_conti.common.exception.types.custom.EmailNotVerifiedException;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.common.utils.SecurityUtil;
import my.everyconti.every_conti.constant.ResponseMessage;
import my.everyconti.every_conti.constant.redis.EmailVerified;
import my.everyconti.every_conti.constant.role.RoleType;
import my.everyconti.every_conti.modules.auth.domain.Role;
import my.everyconti.every_conti.modules.conti.repository.conti.ContiRepository;
import my.everyconti.every_conti.modules.conti.repository.contiSong.ContiSongRepository;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.domain.MemberFollow;
import my.everyconti.every_conti.modules.member.domain.MemberRole;
import my.everyconti.every_conti.modules.member.dto.MemberDto;
import my.everyconti.every_conti.modules.member.dto.MemberFollowDto;
import my.everyconti.every_conti.modules.member.dto.SignUpDto;
import my.everyconti.every_conti.modules.member.repository.MemberFollowRepository;
import my.everyconti.every_conti.modules.member.repository.member.MemberRepository;
import my.everyconti.every_conti.modules.redis.RedisService;
import my.everyconti.every_conti.modules.song.repository.song.SongRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final HashIdUtil hashIdUtil;
    private final MemberFollowRepository memberFollowRepository;
    private final SongRepository songRepository;
    private final ContiSongRepository contiSongRepository;
    private final ContiRepository contiRepository;
    @PersistenceContext
    private final EntityManager em;

    public MemberDto signUp(SignUpDto signUpDto){
        String email = signUpDto.getEmail();
        if (!redisService.getRedisValueByKey(email).equals(EmailVerified.EMAIL_VERIFIED)){
            throw new EmailNotVerifiedException(ResponseMessage.UN_AUTHORIZED);
        }
        if (memberRepository.findByEmail(signUpDto.getEmail()).isPresent()){
            throw new AlreadyExistElementException(ResponseMessage.CONFLICT);
        }
        Member member = Member.builder()
                .name(signUpDto.getName())
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
        return new MemberDto(newMember, hashIdUtil);
    }

    public MemberDto getMemberWithRoles() {
        Optional<String> email = SecurityUtil.getCurrentUsername();
        if (email.isEmpty() || email.get().equals("anonymousUser")) {
            return null;
        }

        Member member = memberRepository.findOneWithRolesByEmail(email.get()).orElseThrow();

        return new MemberDto(member, hashIdUtil);
    }

    public MemberFollowDto followMember(String followedMemberId){
        String email = SecurityUtil.getCurrentUsername().orElseThrow();

        Member followingMember = memberRepository.findByEmail(email).orElseThrow();
        Member followedMember = memberRepository.findById(hashIdUtil.decode(followedMemberId)).orElseThrow();

        if (followingMember.getEmail().equals(followedMember.getEmail())) throw new IllegalArgumentException("본인은 팔로우할 수 없습니다.");

        MemberFollow followRelationship = MemberFollow.builder()
                .following(followingMember)
                .followed(followedMember)
                .build();

        return new MemberFollowDto(memberFollowRepository.save(followRelationship), hashIdUtil);
    }

    public List<MemberFollowDto> getFollowingMembers(){
        String email = SecurityUtil.getCurrentUsername().orElseThrow();

        Member member = memberRepository.findByEmail(email).orElseThrow();
        return memberFollowRepository.findMemberFollowByFollowing(member).stream().map(f -> new MemberFollowDto(f, hashIdUtil)).toList();
    }

    @Transactional
    public CommonResponseDto<String> deleteMember(String memberId){
        Long innerMemberId = hashIdUtil.decode(memberId);
        Member member = memberRepository.findById(innerMemberId).orElseThrow();

        // 본인인지 확인
        SecurityUtil.userMatchOrAdmin(member.getEmail());

        // 등록한 노래 fk null로 만들기
        int affected = songRepository.detachMemberFromSongs(innerMemberId);
        em.flush();
        em.clear();

        // 등록한 콘티송 삭제
        int csDeleted = contiSongRepository.deleteAllByMemberId(innerMemberId);
        em.flush();
        em.clear();

        // 등록한 콘티 삭제
        int contiDeleted = contiRepository.deleteAllByMemberId(innerMemberId);
        em.flush();
        em.clear();

        // 멤버 삭제
        memberRepository.delete(member);

        return new CommonResponseDto<>(true,  ResponseMessage.SUCCESS);
    }
}
