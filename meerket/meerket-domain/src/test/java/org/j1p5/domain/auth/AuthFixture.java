package org.j1p5.domain.auth;

import org.j1p5.domain.auth.dto.OauthProfile;

public class AuthFixture {

    public static OauthProfile create() {
        return new OauthProfileFixture();
    }

    private static class OauthProfileFixture implements OauthProfile {

        private final String id;
        private final String email;

        private OauthProfileFixture() {
            this.id = "1234";
            this.email = "test@test.com";
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getEmail() {
            return email;
        }
    }
}
