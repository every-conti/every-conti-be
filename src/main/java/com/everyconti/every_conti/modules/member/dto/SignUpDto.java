package com.everyconti.every_conti.modules.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @NotBlank
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Pattern(
            regexp = "^\\S{6,}$",
            message = "비밀번호는 공백 없이 6자 이상이어야 합니다."
    )
    private String password;

    @NotBlank
    @Size(min = 3, max = 15)
    private String name;

    @NotBlank
    @Size(min = 3, max = 15, message = "사용자 이름은 15글자 이하로 입력해야 합니다.")
    private String nickname;

    @Size(max = 50)
    private String church;

    @Override
    public String toString() {
        return "signUpDto{" +
                "nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", church='" + church + '\'' +
                '}';
    }
}