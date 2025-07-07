package my.everyconti.every_conti.member.repository;

import my.everyconti.every_conti.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    public Member save(Member member);
    public Optional<Member> findByEmail(String email);
}
