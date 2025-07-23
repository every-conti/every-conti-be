package my.everyconti.every_conti.modules.member.repository.member;

import my.everyconti.every_conti.modules.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    @Override
    Member save(Member member);

    Optional<Member> findByEmail(String email);

//    Optional<Member> findOneWithRolesByEmail(String email);
}
