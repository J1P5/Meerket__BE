package org.j1p5.infrastructure.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.j1p5.domain.auth.dto.OauthProfile;

public class NaverProfile implements OauthProfile {

    @JsonProperty("response")
    private Response response;

    @Getter
    public static class Response {
        @JsonProperty("id")
        private String id;
    }

    @Override
    public String getId() {
        return response.getId();
    }
}
