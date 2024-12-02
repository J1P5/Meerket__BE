package org.j1p5.infrastructure.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.j1p5.domain.auth.dto.OauthToken;

@Getter
@NoArgsConstructor
public class NaverToken implements OauthToken {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;
}
