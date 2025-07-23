package my.everyconti.every_conti.modules.member.repository.member;

import my.everyconti.every_conti.modules.member.domain.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findOneWithRolesByEmail(String email);
}
