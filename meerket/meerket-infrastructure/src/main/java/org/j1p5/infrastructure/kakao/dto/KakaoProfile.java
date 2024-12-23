package org.j1p5.infrastructure.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.j1p5.domain.auth.dto.OauthProfile;

public class KakaoProfile implements OauthProfile {

    @JsonProperty("id")
    private String id;

    @Override
    public String getId() {
        return this.id;
    }
}
