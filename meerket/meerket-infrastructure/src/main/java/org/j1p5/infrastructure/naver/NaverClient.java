package org.j1p5.infrastructure.naver;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.auth.OauthClient;
import org.j1p5.domain.auth.OauthProfile;
import org.j1p5.domain.auth.OauthToken;
import org.j1p5.domain.user.entity.Provider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverClient implements OauthClient {

    private final NaverAuthClient naverAuthClient;
    private final NaverProfileClient naverProfileClient;

    @Value("${spring.oauth2.client.naver.grant_type}")
    private String grantType;

    @Value("${spring.oauth2.client.naver.client_id}")
    private String clientId;

    @Value("${spring.oauth2.client.naver.client_secret}")
    private String clientSecret;

    @Value("${spring.oauth2.client.naver.state}")
    private String state;

    @Override
    public Provider getProvider() {
        return Provider.NAVER;
    }

    @Override
    public OauthToken getOauthToken(String code) {
        return naverAuthClient.getToken(clientId, clientSecret, grantType, code, state);
    }

    @Override
    public OauthProfile getOauthProfile(String token) {
        return naverProfileClient.getProfile(token);
    }
}
