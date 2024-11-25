package org.j1p5.domain.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class NaverProfile implements OauthProfile {

    @JsonProperty("response")
    private Response response;

    @Getter
    public static class Response {
        @JsonProperty("id")
        private String id;

        @JsonProperty("email")
        private String email;
    }

    @Override
    public String getId() {
        return response.getId();
    }

    @Override
    public String getEmail() {
        return response.getEmail();
    }
}
