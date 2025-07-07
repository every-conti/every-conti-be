package my.everyconti.every_conti.modules.member.repository;

import my.everyconti.every_conti.modules.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    public Member save(Member member);
    public Optional<Member> findByEmail(String email);
}
