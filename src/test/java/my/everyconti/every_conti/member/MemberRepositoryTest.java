package my.everyconti.every_conti.member;


import jakarta.transaction.Transactional;
import my.everyconti.every_conti.member.domain.Member;
import my.everyconti.every_conti.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Transactional
//@Rollback(false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
//    @Commit
    public void userSave(){
        Member member = new Member("이영찬", "dhapdhap123@naver.com", "아바교회");
        Member result = memberRepository.save(member);
        System.out.println("result = " + result);
    }
}
