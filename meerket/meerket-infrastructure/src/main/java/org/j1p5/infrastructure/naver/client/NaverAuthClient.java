package org.j1p5.infrastructure.naver.client;

import org.j1p5.infrastructure.naver.config.NaverFeignClientConfig;
import org.j1p5.infrastructure.naver.dto.NaverToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "naver-auth-client",
        url = "https://nid.naver.com",
        configuration = NaverFeignClientConfig.class)
public interface NaverAuthClient {

    @PostMapping("/oauth2.0/token")
    NaverToken getToken(
            @RequestParam(name = "grant_type") String grantType,
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "client_secret") String clientSecret,
            @RequestParam(name = "code") String code,
            @RequestParam(name = "state") String state);
}
