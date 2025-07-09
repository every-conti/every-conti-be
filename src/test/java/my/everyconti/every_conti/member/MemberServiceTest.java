package my.everyconti.every_conti.member;


import jakarta.transaction.Transactional;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
//@Rollback(false)
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

//    @Test
//    @Commit
//    public void userSave(){
//
//        // 최초 생성
//        memberService.signUp(member);
//        // 이미 존재
//        memberService.signUp(member);
//    }
}
