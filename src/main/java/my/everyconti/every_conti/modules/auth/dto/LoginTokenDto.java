package my.everyconti.every_conti.modules.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenDto {
    private String accessToken;
    private String refreshToken;
}