package my.everyconti.every_conti.modules.member.dto;

import lombok.Getter;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.song.dto.response.PraiseTeamDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberDto extends MemberSimpleDto {
    private List<MemberRoleDto> roles;
    private String email;
    private String church;
//    private PraiseTeamDto praiseTeam;

    public MemberDto(Member member, HashIdUtil hashIdUtil) {
        super(member, hashIdUtil);
        email = member.getEmail();
        church = member.getChurch();
        roles = member.getMemberRoles().stream()
                .map(MemberRoleDto::new)
                .collect(Collectors.toList());
//        praiseTeam = member.getPraiseTeam() != null ? new PraiseTeamDto(member.getPraiseTeam(), hashIdUtil) : null;
    }

    @Override
    public String toString() {
        return "MemberDto{" +
                ", roles='" + roles.stream().map(MemberRoleDto::toString).collect(Collectors.joining(", ")) + '\'' +
                '}';
    }
}