package org.j1p5.domain.auth.dto;

public interface OauthToken {
    String getAccessToken();
    String getTokenType();
    String getRefreshToken();
}
