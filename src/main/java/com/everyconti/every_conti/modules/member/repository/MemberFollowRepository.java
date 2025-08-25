package com.everyconti.every_conti.modules.member.repository;

import com.everyconti.every_conti.modules.member.domain.Member;
import com.everyconti.every_conti.modules.member.domain.MemberFollow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberFollowRepository extends JpaRepository<MemberFollow, Long> {

    @Override
    MemberFollow save(MemberFollow memberFollow);

    List<MemberFollow> findMemberFollowByFollowing(Member following);
}
