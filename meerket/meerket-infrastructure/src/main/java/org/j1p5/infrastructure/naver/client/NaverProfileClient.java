package org.j1p5.infrastructure.naver.client;

import org.j1p5.infrastructure.naver.config.NaverFeignClientConfig;
import org.j1p5.infrastructure.naver.dto.NaverProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "naver-profile-client",
        url = "https://openapi.naver.com",
        configuration = NaverFeignClientConfig.class)
public interface NaverProfileClient {

    @GetMapping("/v1/nid/me")
    NaverProfile getProfile(@RequestHeader("Authorization") String token);
}
