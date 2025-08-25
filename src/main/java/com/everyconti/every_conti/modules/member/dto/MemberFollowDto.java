package com.everyconti.every_conti.modules.member.dto;

import lombok.*;
import com.everyconti.every_conti.common.utils.HashIdUtil;
import com.everyconti.every_conti.modules.member.domain.MemberFollow;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberFollowDto {
    private String memberFollowId;
    private String followingMemberId;
    private String followedMemberId;

    public MemberFollowDto(MemberFollow memberFollow, HashIdUtil hashIdUtil) {
        this.memberFollowId = hashIdUtil.encode(memberFollow.getId());
        this.followingMemberId = hashIdUtil.encode(memberFollow.getFollowing().getId());
        this.followedMemberId = hashIdUtil.encode(memberFollow.getFollowed().getId());
    }

    @Override
    public String toString() {
        return "MemberFollowDto{" +
                "memberFollowId='" + memberFollowId + '\'' +
                ", followingMemberId='" + followingMemberId + '\'' +
                ", followedMemberId='" + followedMemberId + '\'' +
                '}';
    }
}
