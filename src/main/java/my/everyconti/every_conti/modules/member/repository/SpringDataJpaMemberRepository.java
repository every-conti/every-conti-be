package my.everyconti.every_conti.modules.member.repository;

import my.everyconti.every_conti.modules.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

}
