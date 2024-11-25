package org.j1p5.api.auth.api;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.auth.dto.LoginRequest;
import org.j1p5.api.global.response.Response;
import org.j1p5.domain.auth.OauthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OauthApi {

    private final OauthService oauthService;

    @PostMapping
    public Response<Void> login(
            @RequestBody LoginRequest loginRequest
    ) {
        oauthService.login(loginRequest.code(), loginRequest.provider());
        return Response.onSuccess();
    }
}
