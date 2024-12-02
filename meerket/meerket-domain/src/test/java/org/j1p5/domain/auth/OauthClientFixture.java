package org.j1p5.domain.auth;

import org.j1p5.domain.auth.dto.OauthProfile;
import org.j1p5.domain.auth.dto.OauthToken;
import org.j1p5.domain.user.entity.Provider;

public class OauthClientFixture implements OauthClient {
    private final Provider provider = Provider.KAKAO;

    @Override
    public Provider getProvider() {
        return provider;
    }

    @Override
    public OauthToken getOauthToken(String code) {
        return null;
    }

    @Override
    public OauthProfile getOauthProfile(String token) {
        return null;
    }
}