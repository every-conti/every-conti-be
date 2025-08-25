package com.everyconti.every_conti.modules.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNicknameDto {
    @NotBlank
    @Size(min = 3, max = 15, message = "사용자 이름은 15글자 이하로 입력해야 합니다.")
    private String nickname;
}