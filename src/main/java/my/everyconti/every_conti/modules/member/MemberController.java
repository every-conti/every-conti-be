package my.everyconti.every_conti.modules.member;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.modules.member.dto.MemberDto;
import my.everyconti.every_conti.modules.member.dto.SignUpDto;
import my.everyconti.every_conti.modules.member.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<MemberDto> signUp(@Valid @RequestBody SignUpDto signUpDto){
        return ResponseEntity.ok(memberService.signUp(signUpDto));
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MemberDto> getMyUserInfo(@PathVariable String email) {
        return ResponseEntity.ok(memberService.getMyUserWithRoles(email));
    }
}
