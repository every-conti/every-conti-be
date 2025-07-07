package my.everyconti.every_conti.member.service;

import jakarta.persistence.EntityExistsException;
import my.everyconti.every_conti.member.domain.Member;
import my.everyconti.every_conti.member.repository.SpringDataJpaMemberRepository;
import my.everyconti.every_conti.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

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
