package org.j1p5.infrastructure.kakao;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.auth.OauthClient;
import org.j1p5.domain.auth.OauthProfile;
import org.j1p5.domain.auth.OauthToken;
import org.j1p5.domain.user.entity.Provider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoClient implements OauthClient {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoProfileClient kakaoProfileClient;

    @Value("${spring.oauth2.client.kakao.grant_type}")
    private String grantType;

    @Value("${spring.oauth2.client.kakao.client_id}")
    private String clientId;

    @Value("${spring.oauth2.client.kakao.redirect_uri}")
    private String redirectUri;


    @Override
    public Provider getProvider() {
        return Provider.KAKAO;
    }

    @Override
    public OauthToken getOauthToken(String code) {
        return kakaoAuthClient.getToken(grantType, clientId, redirectUri, code);
    }

    @Override
    public OauthProfile getOauthProfile(String token) {
        return kakaoProfileClient.getProfile(token, "application/x-www-form-urlencoded;charset=utf-8");
    }
}
