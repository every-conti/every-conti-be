package com.everyconti.every_conti.modules.member.dto;

import lombok.Getter;
import com.everyconti.every_conti.modules.member.domain.Member;

@Getter
public class MemberNicknameDto {
    private String nickname;

    public MemberNicknameDto(Member member) {
        nickname = member.getNickname();
    }

    @Override
    public String toString() {
        return "MemberNicknameDto{" +
                "nickname='" + nickname + '\'' +
                '}';
    }
}
