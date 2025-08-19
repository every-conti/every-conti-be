package my.everyconti.every_conti.modules.member.dto;

import lombok.Getter;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.song.dto.response.PraiseTeamDto;

@Getter
public class MemberSimpleDto {
    private String id;
    private String nickname;
    private String profileImage;
    private PraiseTeamDto praiseTeam;

    public MemberSimpleDto(Member member, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(member.getId());
        nickname = member.getNickname();
        profileImage = member.getProfileImage();
        praiseTeam = member.getPraiseTeam() != null ? new PraiseTeamDto(member.getPraiseTeam(), hashIdUtil) : null;
    }

    @Override
    public String toString() {
        return "MemberDto{" +
                "nickname='" + nickname + '\'' +
                '}';
    }
}