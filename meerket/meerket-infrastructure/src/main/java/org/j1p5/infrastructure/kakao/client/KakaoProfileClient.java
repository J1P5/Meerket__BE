package org.j1p5.infrastructure.kakao.client;

import org.j1p5.infrastructure.kakao.config.KakaoFeignClientConfig;
import org.j1p5.infrastructure.kakao.dto.KakaoProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-profile-client", url = "https://kapi.kakao.com", configuration = KakaoFeignClientConfig.class)
public interface KakaoProfileClient {

    @GetMapping("/v2/user/me")
    KakaoProfile getProfile(
            @RequestHeader("Authorization") String token,
            @RequestHeader("Content-Type") String contentType
    );
}
