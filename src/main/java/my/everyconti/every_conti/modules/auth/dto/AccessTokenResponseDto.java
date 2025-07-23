package my.everyconti.every_conti.modules.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenResponseDto {
    private String accessToken;
    private boolean authenticated; // or "isGuest", "isLoggedIn"

    public AccessTokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
        this.authenticated = (accessToken != null && !accessToken.isBlank());
    }
}