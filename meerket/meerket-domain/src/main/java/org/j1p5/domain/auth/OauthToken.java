package org.j1p5.domain.auth;

public interface OauthToken {
    String getAccessToken();
    String getTokenType();
    String getRefreshToken();
}
