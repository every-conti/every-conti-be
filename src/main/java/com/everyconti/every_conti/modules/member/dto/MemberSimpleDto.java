package com.everyconti.every_conti.modules.member.dto;

import lombok.Getter;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.member.domain.Member;
import com.everyconti.every_conti.modules.song.dto.response.PraiseTeamDto;

@Getter
public class MemberSimpleDto {
    private String id;
    private String name;
    private String nickname;
    private String profileImage;
    private PraiseTeamDto praiseTeam;

    public MemberSimpleDto(Member member, HashIdUtil hashIdUtil) {
        id = hashIdUtil.encode(member.getId());
        name = member.getName();
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