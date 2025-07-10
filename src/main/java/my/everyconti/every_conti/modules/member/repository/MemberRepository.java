package my.everyconti.every_conti.modules.member.repository;

import my.everyconti.every_conti.modules.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Override
    Member save(Member member);

    Optional<Member> findByEmail(String email);

    @EntityGraph
    Optional<Member> findOneWithRolesByEmail(String email);
}
