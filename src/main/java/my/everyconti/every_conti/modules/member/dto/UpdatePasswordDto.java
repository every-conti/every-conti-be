package my.everyconti.every_conti.modules.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDto {
    private String currentPassword;
    @NotBlank
    @Pattern(
            regexp = "^\\S{6,}$",
            message = "비밀번호는 공백 없이 6자 이상이어야 합니다."
    )
    private String newPassword;
}