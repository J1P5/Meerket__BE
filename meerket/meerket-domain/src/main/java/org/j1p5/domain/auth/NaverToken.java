package org.j1p5.domain.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverToken implements OauthToken {
    private String accessToken;
    private String refreshToken;
    private String token_type;
    private String expiresIn;
    private String error;
    private String errorDescription;
}
