package org.j1p5.infrastructure.naver;

import org.j1p5.domain.auth.dto.NaverProfile;
import org.j1p5.infrastructure.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naver-profile-client", url = "https://openapi.naver.com", configuration = FeignClientConfig.class)
public interface NaverProfileClient {

    @GetMapping("/v1/nid/me")
    NaverProfile getProfile(
            @RequestHeader("Authorization") String token
    );

}
