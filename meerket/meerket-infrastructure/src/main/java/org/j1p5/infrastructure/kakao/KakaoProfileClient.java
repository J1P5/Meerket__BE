package org.j1p5.infrastructure.kakao;

import org.j1p5.domain.auth.dto.KakaoProfile;
import org.j1p5.infrastructure.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-profile-client", url = "https://kapi.kakao.com", configuration = FeignClientConfig.class)
public interface KakaoProfileClient {

    @GetMapping("/v2/user/me")
    KakaoProfile getProfile(
            @RequestHeader("Authorization") String token,
            @RequestHeader("Content-Type") String contentType
    );
}
