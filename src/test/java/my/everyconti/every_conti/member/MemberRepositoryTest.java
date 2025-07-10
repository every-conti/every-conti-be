package my.everyconti.every_conti.member;


import jakarta.transaction.Transactional;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
//@Rollback(false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
//    @Autowired
//    private MemberCustomRepository memberCustomRepository;
//
//    @Test
//    public void getMemberWithRoles(){
//        Optional<Member> result = memberCustomRepository.findOneWithRolesByEmail("dhapdhap123@naver.com");
//        if (result.isPresent()) {
//            Member member = result.get();
//            System.out.println("이메일: " + member.getEmail());
//            System.out.println("닉네임: " + member.getNickname());
//            System.out.println("권한 목록: " + member.getMemberRoles());
//        } else {
//            System.out.println("사용자를 찾을 수 없습니다.");
//        }
//    }

//    @Test
////    @Commit
//    public void userSave(){
//        Member member = Member.builder()
//                .nickname("dhapdhap123")
//                .email("dhapdhap123@naver.com")
//                .password("11111")
//                .church("aaaa교회")
//                .activated(true)
//                .build();
////                .church("")
//        Member result = memberRepository.save(member);
//        System.out.println("member: " + Arrays.toString(result.toString().toCharArray()));
//    }
//
//    @Test
//    public void findUserByEmail(){
//        String email = "dhapdhap123@naver.com";
//        Optional<Member> member = memberRepository.findByEmail(email);
//        // 이메일 가입 기록 없으면 404
//        System.out.println("member: " + Arrays.toString(member.stream().toArray()));
//    }
}
