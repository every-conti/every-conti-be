package com.everyconti.every_conti.modules.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenDto {
    private String accessToken;
}