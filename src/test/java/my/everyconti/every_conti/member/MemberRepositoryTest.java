package my.everyconti.every_conti.member;


import jakarta.transaction.Transactional;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
@Transactional
//@Rollback(false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
//    @Commit
    public void userSave(){
        Member member = Member.builder()
                .nickname("dhapdhap123")
                .email("dhapdhap123@naver.com")
                .password("11111")
                .church("aaaa교회")
                .activated(true)
                .build();
//                .church("")
        Member result = memberRepository.save(member);
        System.out.println("member: " + Arrays.toString(result.toString().toCharArray()));
    }

    @Test
    public void findUserByEmail(){
        String email = "dhapdhap123@naver.com";
        Optional<Member> member = memberRepository.findByEmail(email);
        // 이메일 가입 기록 없으면 404
        System.out.println("member: " + Arrays.toString(member.stream().toArray()));
    }
}
