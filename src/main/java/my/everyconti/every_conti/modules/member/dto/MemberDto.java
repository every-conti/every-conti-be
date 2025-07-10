package my.everyconti.every_conti.modules.member.dto;

import lombok.Getter;
import my.everyconti.every_conti.modules.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberDto {
    private String nickname;
    private String email;
    private String church;
    private List<MemberRoleDto> roles;

    public MemberDto(Member member) {
        nickname = member.getNickname();
        email = member.getEmail();
        church = member.getChurch();
        roles = member.getMemberRoles().stream()
                .map(MemberRoleDto::new)
                .collect(Collectors.toList());
    }
}