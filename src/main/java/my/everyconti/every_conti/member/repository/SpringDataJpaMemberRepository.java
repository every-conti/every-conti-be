package my.everyconti.every_conti.member.repository;

import jakarta.persistence.EntityManager;
import my.everyconti.every_conti.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

}
