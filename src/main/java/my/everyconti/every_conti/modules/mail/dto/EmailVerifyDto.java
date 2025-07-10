package my.everyconti.every_conti.modules.mail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerifyDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min=6, max=6)
    private String userCode;
}