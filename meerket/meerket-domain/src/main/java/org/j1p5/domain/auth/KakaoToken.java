package org.j1p5.domain.auth;

import lombok.Getter;

@Getter
public class KakaoToken implements OauthToken{
    private String accessToken;
    private String refreshToken;
}
