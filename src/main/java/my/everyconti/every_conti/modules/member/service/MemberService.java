package my.everyconti.every_conti.modules.member.service;

import jakarta.persistence.EntityExistsException;
import my.everyconti.every_conti.modules.member.domain.Member;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void createMember(Member member) throws EntityExistsException {
        String email = member.getEmail();
        Optional<Member> existingMember = memberRepository.findByEmail(email);
        if (existingMember.isPresent()){
            throw new EntityExistsException();
        } else {
            System.out.println("없음, 생성");
            memberRepository.save(member);
        }

    }
}
