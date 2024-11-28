package org.j1p5.infrastructure.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.j1p5.domain.auth.dto.OauthToken;

@Getter
public class KakaoToken implements OauthToken {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;
}
