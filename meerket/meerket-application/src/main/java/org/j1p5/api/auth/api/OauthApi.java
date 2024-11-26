package org.j1p5.api.auth.api;

import jakarta.servlet.http.HttpServletRequest;
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
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    ) {
        Long userId = oauthService.login(loginRequest.code(), loginRequest.provider());

        request.getSession().setAttribute("id", userId);

        return Response.onSuccess();
    }
}
