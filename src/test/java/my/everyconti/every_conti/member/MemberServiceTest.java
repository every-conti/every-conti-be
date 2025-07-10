package my.everyconti.every_conti.member;


import jakarta.transaction.Transactional;
import my.everyconti.every_conti.modules.member.MemberService;
import my.everyconti.every_conti.modules.member.dto.MemberDto;
import my.everyconti.every_conti.modules.member.dto.MemberRoleDto;
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
    public void getMyUserWithRoles(){
        MemberDto result = memberService.getMyUserWithRoles("dhapdhap123@naver.com");
        System.out.println("이메일: " + result.getEmail());
        System.out.println("닉네임: " + result.getNickname());
        for (MemberRoleDto role : result.getRoles()){
            System.out.println("role = " + role.getRoleName());
        }
    }
}
