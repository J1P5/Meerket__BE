package org.j1p5.api.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.auth.AuthManager;
import org.j1p5.api.auth.dto.LoginRequest;
import org.j1p5.api.auth.dto.LoginResponse;
import org.j1p5.api.auth.dto.SessionInfo;
import org.j1p5.api.auth.usecase.OauthLoginUsecase;
import org.j1p5.api.global.response.Response;
import org.j1p5.domain.user.UserInfo;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OauthController {

    private final OauthLoginUsecase oauthLoginUsecase;
    private final AuthManager authManager;
    private final HttpSessionSecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    @PostMapping
    @Operation(summary = "소셜 로그인 API", description = "소셜 로그인 API. provider는 \"NAVER\", \"KAKAO\" 문자열을 입력해야 한다.")
    public Response<LoginResponse> login(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        UserInfo user = oauthLoginUsecase.login(loginRequest.code(), loginRequest.provider());

        SecurityContext context = authManager.setContext(SessionInfo.of(user.pk(), user.role()));
        securityContextRepository.saveContext(context, request, response);

        return Response.onSuccess(LoginResponse.from(user));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "로그아웃 API. 세션을 무효화시킨다.")
    public Response<Void> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return Response.onSuccess();
    }
}
