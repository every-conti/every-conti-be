package my.everyconti.every_conti.modules.member;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.everyconti.every_conti.modules.member.dto.MemberDto;
import my.everyconti.every_conti.modules.member.dto.SignUpDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/")
    public ResponseEntity<MemberDto> signUp(@Valid @RequestBody SignUpDto signUpDto){
        return ResponseEntity.ok(memberService.signUp(signUpDto));
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MemberDto> getMyUserInfo(@PathVariable String email) {
        return ResponseEntity.ok(memberService.getMyUserWithRoles(email));
    }
}
