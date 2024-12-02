package org.j1p5.domain.auth;

import org.j1p5.domain.auth.dto.OauthToken;

public class OauthTokenFixture {

    public static OauthToken create() {
        return new KakaoTokenFixture();
    }

    private static class KakaoTokenFixture implements OauthToken {

        private final String accessToken;
        private final String refreshToken;

        private KakaoTokenFixture() {
            this.accessToken = "access token";
            this.refreshToken = "refresh token";
        }

        @Override
        public String getAccessToken() {
            return accessToken;
        }

        @Override
        public String getTokenType() {
            return "KAKAO";
        }

        @Override
        public String getRefreshToken() {
            return "";
        }
    }
}
