package my.everyconti.every_conti.modules.member.dto;

import lombok.Getter;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberDto {
    private String id;
    private String nickname;
    private String email;
    private String church;
    private List<MemberRoleDto> roles;

    public MemberDto(Member member, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(member.getId());
        nickname = member.getNickname();
        email = member.getEmail();
        church = member.getChurch();
        roles = member.getMemberRoles().stream()
                .map(MemberRoleDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "MemberDto{" +
                "nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", church='" + church + '\'' +
                ", roles='" + roles.stream().map(MemberRoleDto::toString).collect(Collectors.joining(", ")) + '\'' +
                '}';
    }
}