package my.everyconti.every_conti.member;


import jakarta.transaction.Transactional;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
//@Rollback(false)
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
//    @Commit
    public void userSave(){
        Member member = new Member("이영찬", "dhapdhap123@naver.com", "아바교회", "1111");
        // 최초 생성
        memberService.createMember(member);
        // 이미 존재
        memberService.createMember(member);
    }
}
