package com.everyconti.every_conti.modules.member;


import com.everyconti.every_conti.modules.member.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.everyconti.every_conti.common.dto.response.CommonResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("")
    public ResponseEntity<CommonResponseDto<MemberDto>> signUp(@Valid @RequestBody SignUpDto signUpDto){
        return ResponseEntity.ok(new CommonResponseDto(true, memberService.signUp(signUpDto)));
    }

    @DeleteMapping("/{memberId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommonResponseDto<String>> deleteMember(@PathVariable String memberId){
        return ResponseEntity.ok(memberService.deleteMember(memberId));
    }

    @PatchMapping("/{memberId}/password")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommonResponseDto<String>> updatePassword(@PathVariable String memberId, @RequestBody UpdatePasswordDto updatePasswordDto){
        return ResponseEntity.ok(memberService.updatePassword(memberId, updatePasswordDto));
    }
    @PatchMapping("/{memberId}/nickname")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CommonResponseDto<String>> updateNickname(@PathVariable String memberId, @Valid @RequestBody UpdateNicknameDto updateNicknameDto){
        return ResponseEntity.ok(memberService.updateNickname(memberId, updateNicknameDto));
    }


    @GetMapping("/me")
    public ResponseEntity<MemberDto> getMemberWithRoles() {
        return ResponseEntity.ok(memberService.getMemberWithRoles());
    }

    @PostMapping("/{memberId}/follow")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<MemberFollowDto> followMember(@PathVariable String memberId){
        return ResponseEntity.ok(memberService.followMember(memberId));
    }

    @GetMapping("/{memberId}/following")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<MemberFollowDto>> followMember(){
        return ResponseEntity.ok(memberService.getFollowingMembers());
    }
}
