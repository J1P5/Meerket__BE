package org.j1p5.api.auth;

import org.j1p5.domain.auth.dto.OauthProfile;

public class AuthFixture {

    public static OauthProfile create() {
        return new OauthProfileFixture();
    }

    private static class OauthProfileFixture implements OauthProfile {

        private final String id;

        private OauthProfileFixture() {
            this.id = "1234";
        }

        @Override
        public String getId() {
            return id;
        }
    }
}
