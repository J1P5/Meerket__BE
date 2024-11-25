package org.j1p5.infrastructure.naver;

import org.j1p5.domain.auth.NaverToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naver-auth-client", url = "https://nid.naver.com")
public interface NaverAuthClient {

    @PostMapping("/oauth2.0/token")
    NaverToken getToken(@RequestParam(name = "response_type") String code,
                        @RequestParam(name = "client_id") String clientId,
                        @RequestParam(name = "redirect_uri") String redirectUri,
                        @RequestParam(name = "state") String state
    );
}
