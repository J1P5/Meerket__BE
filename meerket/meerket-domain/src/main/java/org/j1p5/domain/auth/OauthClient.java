package org.j1p5.domain.auth;

import org.j1p5.domain.user.entity.Provider;

public interface OauthClient {
    Provider getProvider();
    OauthToken getOauthToken(String code);
    OauthProfile getOauthProfile(String token);
}
